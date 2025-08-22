import time
import subprocess
from github import Github
from dotenv import load_dotenv
load_dotenv()
import os
import sys
sys.path.append('/mnt/ai_env/lib/python3.10/site-packages')

GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")

# Repository details
REPO_OWNER = "mananchichra"
REPO_NAME = "distributed_ddes"

#  GitHub API client
g = Github(GITHUB_TOKEN)
repo = g.get_repo(f"{REPO_OWNER}/{REPO_NAME}")

# Store the last known commit SHA
last_commit_sha = None

def check_for_new_commit():
    global last_commit_sha
    latest_commit = repo.get_commits()[0]  # Get latest commit

    if last_commit_sha is None:
        last_commit_sha = latest_commit.sha  # Initialize commit tracking
        print(f"Initial commit set: {last_commit_sha}")

    elif latest_commit.sha != last_commit_sha:
        print(f"New push detected! Running script.py")
        last_commit_sha = latest_commit.sha  # Update last commit
        
        # Run your script when a new push is detected
        subprocess.run(["python", "script.py"])

# Polling loop
while True:
    check_for_new_commit()
    time.sleep(100)  # Wait 60 seconds before checking again
