# file-search-engine

A **multithreaded file search tool** in Java that searches files by **name or extension** under a given directory.  
Built with **clean code practices**, **design patterns**, and **concurrency** using Java’s `ExecutorService`.

---

## Features
- Recursively search all files under a root directory.
- Search for files by **full name** (e.g., `report.txt`) or **extension** (e.g., `.md`, `.java`).
- Multithreaded scanning using a fixed thread pool.
- Prompts the user when a match is found:
  - Option to copy the file to another directory.
- Final search summary:
  - Total files scanned
  - Number of matches found
  - Time taken

---

## Project Structure
```
file-search-engine/
├── src/
│   ├── Main.java
│   ├── config/
│   │   └── AppConfig.java
│   ├── core/
│   │   ├── FileSearchService.java
│   │   ├── SearchTask.java
│   │   └── SearchTaskFactory.java
│   ├── strategy/
│   │   ├── SearchStrategy.java
│   │   └── FileNameSearch.java
│   └── utils/
│       ├── FileUtils.java
│       └── Logger.java
└── README.md
```

---

## How to Run
1. Clone or download this repository.
2. Navigate to the project directory.
3. Compile the source files:
   ```bash
   javac -d out src/**/*.java
   ```
4. Run the application:
   ```bash
   java -cp out Main
   ```

---

## Example Usage
```
========================================
     file-search-engine (Java) - by name
========================================
Enter root directory path: ./test-data
Enter file name or extension to search (e.g., report.txt or .md): .md
Using a fixed thread pool of size: 4
[INFO] 2025-09-30 21:20:14 - Starting search in: ./test-data
[INFO] 2025-09-30 21:20:14 - Looking for file name/ext: ".md"
[INFO] 2025-09-30 21:20:15 - File matched: ./test-data/readme.md
Do you want to copy this file to another directory? (yes/no): yes
Enter destination directory: ./results
[INFO] 2025-09-30 21:20:15 - Copied file to: ./results/readme.md

------------- SUMMARY -------------
Root directory : ./test-data
File name/ext  : .md
Files scanned  : 12
Matches found  : 1
Time taken     : 134 ms
-----------------------------------
```

---

##  Configurations
You can adjust settings in [`AppConfig.java`](src/config/AppConfig.java):
- `threadPoolSize` → number of worker threads (default: 4)

---

##  Design Patterns Used
- **Singleton** → `Logger` (single global logging instance)
- **Factory** → `SearchTaskFactory` (creates tasks for the executor)
- **Strategy** → `SearchStrategy` & `FileNameSearch` (different search rules could be added later)

---

## Requirements
- Java 8 or later
- No external libraries (uses only core Java)

---

##  Possible Extensions
- Support for multiple patterns at once (e.g., `.md` **and** `.txt`)
- Regex-based file name matching
- Exclusion filters (e.g., ignore `.class`, `.exe`)
- GUI front-end
