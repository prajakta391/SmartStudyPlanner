# Smart Study Planner — Algorithmic Task Scheduler

A Java console application that uses a **Greedy Algorithm + Priority Queue** to automatically schedule study tasks in the most efficient order — sorted by priority and estimated time. Built to explore how classical scheduling algorithms from Operating Systems apply to real-world planning problems.

---

## What This Project Does

Most students plan their study manually and inefficiently. This tool applies the same logic used in **CPU process scheduling** to organise tasks automatically. Given a list of subjects, deadlines, and priorities, it computes the optimal study order to minimise wasted time and missed deadlines.

---

## Core Algorithm — How Scheduling Works

The scheduler uses a **Greedy approach with a Priority Queue (max-heap)**:

1. All pending tasks are inserted into a Priority Queue
2. The comparator orders tasks by: **Priority first → Shorter duration second**
3. Tasks are extracted in greedy optimal order
4. Conflict detection checks if same-deadline tasks exceed 8 hours (1 study day)

This directly maps to the **Shortest Job First (SJF)** and **Priority Scheduling** algorithms studied in Operating Systems — an active area of research in resource optimisation.

---

## Features

- Add tasks with subject, priority, deadline, and estimated hours
- Get an automatically optimised study schedule using Greedy algorithm
- Detect deadline conflicts when two tasks clash on the same day
- Mark tasks as completed and track progress
- View study statistics — hours remaining, days needed, high priority count
- All data saved to file and loaded across sessions (persistent storage)
- Handles up to 200 tasks without performance issues

---

## Project Structure

```
StudyPlanner/
├── src/
│   ├── Main.java          # Entry point, full menu interface
│   ├── Task.java          # Task model with Comparable for Priority Queue
│   ├── Scheduler.java     # Greedy algorithm, conflict detection, stats
│   └── DataManager.java   # File I/O — save and load tasks
├── data/
│   └── tasks.txt          # Auto-generated persistent storage
└── README.md
```

---

## How to Run

### Requirements
- Java JDK 8 or above
- Any terminal (PowerShell, Command Prompt, VS Code terminal)

### Steps

```bash
# Step 1: Clone the repository
git clone https://github.com/VijayaDurgaKondeti/SmartStudyPlanner.git
cd SmartStudyPlanner

# Step 2: Compile
javac -d out src\Task.java src\Scheduler.java src\DataManager.java src\Main.java

# Step 3: Run
java -cp out Main
```

### Sample Output

```
╔══════════════════════════════════════════════════╗
║     Smart Study Planner v1.0                     ║
║     Greedy Task Scheduler | Kondeti Vijaya Durga ║
╚══════════════════════════════════════════════════╝

  MAIN MENU
  1. Add a new task
  2. View all tasks
  3. Get optimised study schedule
  4. Mark task as completed
  5. Check for deadline conflicts
  6. View study statistics
  7. Delete a task
  8. Exit

─── Optimised Study Schedule ───
  Slot 1: DSA Assignment         | Mathematics     | HIGH   | Deadline: 05-06-2026 | 3 hrs | [PENDING]
  Slot 2: OS Lab Exam Prep       | OS              | HIGH   | Deadline: 06-06-2026 | 4 hrs | [PENDING]
  Slot 3: DBMS Mini Project      | DBMS            | MEDIUM | Deadline: 10-06-2026 | 5 hrs | [PENDING]
  Slot 4: Python Assignment      | Programming     | LOW    | Deadline: 15-06-2026 | 2 hrs | [PENDING]

─── Deadline Conflicts Detected ───
  CONFLICT: "DSA Assignment" and "OS Lab Exam Prep" both due on 05-06-2026
  — total 9 hrs exceeds 1-day limit (8 hrs).
  SUGGESTION: Move "OS Lab Exam Prep" deadline one day earlier to spread the load.
```

---

## Tech Stack

- **Language:** Java (JDK 8+)
- **Concepts:** Greedy Algorithms, Priority Queue, OOP, File I/O, Comparable Interface
- **Tools:** VS Code, Git, GitHub

---

## Connection to CS Research

The scheduling logic in this project directly maps to well-studied problems in computer science:

- **Priority Scheduling** — used in OS process management
- **Greedy algorithm correctness** — provably optimal for certain scheduling objectives
- **Conflict detection** — related to interval scheduling and resource allocation problems
- **Shortest Job First (SJF)** — minimises average waiting time, proven optimal for throughput

These are active research topics in algorithm design and systems optimisation — areas directly relevant to Google Research.

---

## Author

**Kondeti Vijaya Durga**
B.Tech AIML — Mohan Babu University
[LinkedIn](https://linkedin.com/in/kondeti-vijaya-durga) | [GitHub](https://github.com/VijayaDurgaKondeti)