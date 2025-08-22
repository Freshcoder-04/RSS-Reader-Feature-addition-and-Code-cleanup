from dotenv import load_dotenv
load_dotenv()
import os
import json
import sys
import re
sys.path.append('/mnt/ai_env/lib/python3.10/site-packages')
# sys.path.append('/mnt/ai_env/jre')
import google.generativeai as genai
from github import Github
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain.output_parsers import JsonOutputKeyToolsParser
# from langchain.prompts import ChatPromptTemplate
import subprocess
from langgraph.graph import END, StateGraph
from langchain_core.prompts import ChatPromptTemplate

GEMINI_API_KEY = os.getenv('GEMINI_API_KEY')
genai.configure(api_key=GEMINI_API_KEY)
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
REPO_NAME = "mananchichra/osa-shell"
BASE_BRANCH = "main"

# LangChain's Gemini Wrapper
model = ChatGoogleGenerativeAI(model="gemini-pro", google_api_key=GEMINI_API_KEY)

# LangChain's JSON output parser
issue_parser = JsonOutputKeyToolsParser(key_name="issues")
refactor_parser = JsonOutputKeyToolsParser(key_name="refactored_code")

# Get Python Files from Repository
# def get_repo_files(repo_path):
#     files = {}
#     for root, _, filenames in os.walk(repo_path):
#         for filename in filenames:
#             if filename.endswith(".java"):
#                 file_path = os.path.join(root, filename)
#                 with open(file_path, "r", encoding="utf-8") as f:
#                     files[file_path] = f.read()
#     return files
def get_repo_files(repo_path):
    files = {}
    
    # Collect Java files from the repository
    for root, _, filenames in os.walk(repo_path):
        for filename in filenames:
            if filename.endswith(".java"):
                file_path = os.path.join(root, filename)
                with open(file_path, "r", encoding="utf-8") as f:
                    files[file_path] = f.read()

    # Ensure at least two files are returned (if available)
    if len(files) < 2:
        print("⚠️ Warning: Less than two Java files found. Design smell detection may be limited.")

    return files



# def ran_pmd(file_path):
#     """Run PMD analysis and return detected issues."""
#     pmd_report = "pmd_report.xml"  # Output file for PMD
#     cmd = f"pmd -d {file_path} -R rulesets/java/quickstart.xml -f json -r {pmd_report}"
#     subprocess.run(cmd, shell=True)

#     with open(pmd_report, "r") as f:
#         pmd_results = json.load(f)
    
#     return [issue["description"] for issue in pmd_results.get("violations", [])]



def run_pmd(file_path):
    """Run PMD analysis and return detected issues."""
    pmd_report = "pmd_report.json"
    cmd = [
        "pmd", "check",
        "--dir", file_path,
        "--rulesets", "rulesets/java/quickstart.xml",
        "--format", "json",
        "--report-file", pmd_report
    ]

    try:
        print(f" Running PMD on {file_path}...")
        result = subprocess.run(cmd, check=False, capture_output=True, text=True)

        # Allow exit code 4 (violations found)
        if result.returncode not in (0, 4):  
            print(f"PMD Execution Failed: {result.stderr}")
            return []

        if not os.path.exists(pmd_report) or os.stat(pmd_report).st_size == 0:
            print("PMD report file is missing or empty.")
            return []

        with open(pmd_report, "r") as f:
            pmd_results = json.load(f)

        return [issue["description"] for issue in pmd_results.get("violations", [])]

    except json.JSONDecodeError:
        print("PMD Output is not valid JSON. Check for errors.")
    except Exception as e:
        print(f" Unexpected Error: {e}")

    return []




def run_checkstyle(file_path):
    """Run Checkstyle analysis and return detected issues."""
    checkstyle_report = "checkstyle_report.json"
    cmd = f"checkstyle -c /google_checks.xml {file_path} -f json > {checkstyle_report}"
    subprocess.run(cmd, shell=True)

    with open(checkstyle_report, "r") as f:
        checkstyle_results = json.load(f)
    
    return [issue["message"] for issue in checkstyle_results]


def detect_smells(state):
    file_path, code = state["file_path"], state["code"]

    # Get all files for context
    repo_path = os.path.dirname(file_path)
    files = get_repo_files(repo_path)

    if len(files) > 1:
        other_file_path, other_code = next(
            (fp, fc) for fp, fc in files.items() if fp != file_path
        )
        print(f"\n Including additional file for design analysis: {other_file_path}\n")
    else:
        other_code = ""

    print(f"\n Scanning for design smells in: {file_path}\n")
    pmd_issues = run_pmd(file_path)

    prompt = ChatPromptTemplate.from_messages([
        ("human", """
        You are an expert analyzing **Java** code for design smells.
        Analyze the following c**Java** code and list any **design smells**.
        Return a **valid JSON** response.

        **Primary Code:**
        {code}

        **Additional Context (if available):**
        {other_code}

        Expected JSON format:
        {{
          "issues": ["Issue 1", "Issue 2", ...]
        }}
        """)
    ])
    
    formatted_prompt = prompt.format_messages(code=code, other_code=other_code)

    try:
        response = model.invoke(formatted_prompt)
        raw_content = response.content.strip()
        raw_content = re.sub(r"^```json|```$", "", raw_content).strip()
        structured_output = json.loads(raw_content) 
        issues = structured_output.get("issues", [])
    except Exception as e:
        print(f">> Error: Failed to parse JSON response: {e}")
        issues = []
        
    all_issues = pmd_issues + issues
    print(f"**Detected Smells:** {all_issues}\n")

    # Fix: Include `all_files` in return
    return {
        "file_path": file_path,
        "code": code,
        "issues": all_issues,
        "all_files": files  # Pass all_files forward
    }




def apply_refactoring(state):
    all_files = state["all_files"]  # Dictionary of {file_path: code}
    target_file = state["file_path"]  # The main file being refactored
    detected_issues = state["issues"]  # Issues for this file

    # Select an additional file for context (if available)
    context_files = {fp: code for fp, code in all_files.items() if fp != target_file}
    
    if len(context_files) == 0:
        print("⚠️ Only one file available. Limited design smell refactoring.")
        context_code = ""  # No additional file available
    else:
        # Select one additional file to provide context (for cross-file smells)
        additional_file_path, additional_code = next(iter(context_files.items()))
        context_code = f"\n\n### Additional Context from {additional_file_path} ###\n{additional_code}"
    

    print(f"🔹 Refactoring {target_file} with context from {len(context_files)} other file(s)...")

    prompt = ChatPromptTemplate.from_messages([
        ("human", """
        You are an expert refactoring assistant.
        Refactor and **Modify** the following **Java** code to **remove design smells**:
        {code}

        **Detected issues:**
        {issues}

        {context_code}

        
        **Rules:**
        - Follow best practices for the **Java** language.
        - **Ensure logic between files remains intact.**  Do not remove or rename functions/methods if they are used elsewhere.
        - Maintain **cross-file dependencies**. If a function in File A is used in File B, it must still work after refactoring.
        - Keep the code **modular, maintainable, and efficient**.
        - Follow best practices for the specified language.
        - **Return only valid JSON**—no explanations, no extra text, no markdown.
        
        Return **ONLY valid JSON with format**:
        {{
          "refactored_code": "<new improved code>"
        }}
        """)
    ])
    
    formatted_prompt = prompt.format_messages(
        code=all_files[target_file], 
        issues=json.dumps(detected_issues, indent=2), 
        context_code=context_code
    )

    try:
        response = model.invoke(formatted_prompt)
        print("🔹 Raw Response:", response.content)

        # Strip unwanted formatting markers (```json)
        raw_content = response.content.strip()
        raw_content = re.sub(r"^```json|```$", "", raw_content).strip()

        # Ensure JSON formatting is correct
        raw_content = raw_content.replace("\n", "").replace("\t", "").replace("\r", "")
        raw_content = re.sub(r'([{,])\s*([a-zA-Z0-9_]+)\s*:', r'\1"\2":', raw_content)

        if raw_content.count('"') % 2 != 0:
            raw_content += '"'

        if not raw_content.startswith("{"):
            raw_content = "{" + raw_content
        if not raw_content.endswith("}"):
            raw_content += "}"

        print(">>> Formatted JSON:", raw_content)

        structured_output = json.loads(raw_content)
        refactored_code = structured_output.get("refactored_code", "")

        return {"file_path": target_file, "refactored_code": refactored_code}

    except Exception as e:
        print(f">>>> Error: Failed to parse JSON response: {e}")
        return {"file_path": target_file, "refactored_code": all_files[target_file]}





# Save Refactored Files Locally
def save_refactored_files(state):
    file_path, refactored_code = state["file_path"], state["refactored_code"]

    with open(file_path, "w", encoding="utf-8") as f:
        f.write(refactored_code)

    print(f">> Saved refactored file: {file_path}\n")
    return state

#  Commit & Push Changes
def commit_and_push_changes(refactored_files):
    branch_name = "refactor-branch3"

    os.chdir("./check_repo")
    # Check if branch exists, delete if necessary
    os.system(f"git branch -D {branch_name} 2>/dev/null || true")

    os.system(f"git checkout -b {branch_name}")
    os.system("git add .")
    os.system('git commit -m "Automated refactoring: Fixed design smells"')
    os.system(f"git push origin {branch_name}")

#  Create GitHub Pull Request Using PyGitHub
def create_pull_request():
    g = Github(GITHUB_TOKEN)
    repo = g.get_repo(REPO_NAME)

    # Check if PR already exists
    existing_prs = repo.get_pulls(state="open", head="refactor-branch")
    if any(pr for pr in existing_prs):
        print(">> Pull request already exists.")
        return

    pr = repo.create_pull(
        title="Automated Refactoring: Fixed Design Smells",
        body="This PR contains automated refactoring based on detected design smells.",
        head="refactor-branch",
        base=BASE_BRANCH
    )
    print(f">> Pull request created: {pr.html_url}")

# **LangGraph Workflow**
workflow = StateGraph(dict)

workflow.add_node("detect_smells", detect_smells)
workflow.add_node("apply_refactoring", apply_refactoring)
workflow.add_node("save_files", save_refactored_files)

workflow.set_entry_point("detect_smells")
workflow.add_edge("detect_smells", "apply_refactoring")
workflow.add_edge("apply_refactoring", "save_files")
workflow.add_edge("save_files", END)

graph = workflow.compile()


def run_tests(file_path):
    """
    Runs unit tests for the given file.
    Returns True if tests pass, False otherwise.
    """
    import subprocess

    if file_path.endswith(".py"):
        result = subprocess.run(["pytest"], capture_output=True, text=True)
    elif file_path.endswith(".java"):
        result = subprocess.run(["mvn", "test"], capture_output=True, text=True)
    elif file_path.endswith(".cpp"):
        result = subprocess.run(["make", "test"], capture_output=True, text=True)
    else:
        return None  # Unsupported language

    return result.returncode == 0  # True if tests pass




#  **Run the Pipeline**
def main():
    # repo_path = "/mnt/spring25/se_1_pipeline/check_repo"
    repo_path = "/mnt/OSA/a3"
    files = get_repo_files(repo_path)  # Get all files in repo
    refactored_files = {}

    for file_path, code in files.items():
        result = graph.invoke({
            "file_path": file_path,
            "code": code,
            "all_files": files  #  Pass all files to state
        })
        refactored_files[file_path] = result["refactored_code"]

    # Verify refactoring doesn't break functionality
    # if run_tests(refactored_file):
    #     print("Refactoring preserved functionality!")
    # else:
    #     print("Tests failed! Check for breaking changes.")

    # Push Changes and Create Pull Request
    commit_and_push_changes(refactored_files)
    create_pull_request()

if __name__ == "__main__":
    main()
