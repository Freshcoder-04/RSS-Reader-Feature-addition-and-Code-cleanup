import pandas as pd
import re

with open("/Users/masumi/Desktop/Study/Sem6/Software Engineering/Projects/Project1/Issues-final/project-1-team-27/reader-core_lizard_report.txt", "r") as file:
    lines = file.readlines()

data = []
for line in lines:
    match = re.search(r'(\d+)\s+(\d+)\s+\d+\s+\d+\s+\d+\s+(.+?)::(.+)', line)
    if match:
        nloc, ccn, filename, function = match.groups()
        data.append([filename, function, int(ccn)])

df = pd.DataFrame(data, columns=["File", "Function", "CCN"])

excel_filename = "/Users/masumi/Desktop/Study/Sem6/Software Engineering/Projects/Project1/Issues-final/project-1-team-27/reader-core_cc.xlsx"
df.to_excel(excel_filename, index=False)

print(f"CCN extracted and saved in {excel_filename}")