import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static List<Task> tasks = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // Load saved tasks on startup
        tasks = DataManager.loadTasks();

        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     Smart Study Planner v1.0                     ║");
        System.out.println("║     Greedy Task Scheduler | Kondeti Vijaya Durga ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("  " + tasks.size() + " task(s) loaded from previous session.\n");

        boolean running = true;

        while (running) {
            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("  MAIN MENU");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("  1. Add a new task");
            System.out.println("  2. View all tasks");
            System.out.println("  3. Get optimised study schedule");
            System.out.println("  4. Mark task as completed");
            System.out.println("  5. Check for deadline conflicts");
            System.out.println("  6. View study statistics");
            System.out.println("  7. Delete a task");
            System.out.println("  8. Exit");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.print("  Choose (1-8): ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1: addTask();            break;
                case 2: viewAllTasks();       break;
                case 3: viewSchedule();       break;
                case 4: markCompleted();      break;
                case 5: checkConflicts();     break;
                case 6: Scheduler.printStats(tasks); break;
                case 7: deleteTask();         break;
                case 8:
                    DataManager.saveTasks(tasks);
                    System.out.println("\n  Goodbye! Study hard!");
                    running = false;
                    break;
                default:
                    System.out.println("  Invalid choice. Enter 1-8.");
            }
        }

        sc.close();
    }

    // ─── ADD TASK ──────────────────────────────────────────────────
    private static void addTask() {
        System.out.println("\n  ─── Add New Task ───");

        System.out.print("  Task name       : ");
        String name = sc.nextLine().trim();

        System.out.print("  Subject         : ");
        String subject = sc.nextLine().trim();

        System.out.print("  Priority (1=Low, 2=Medium, 3=High) : ");
        int priority = sc.nextInt();
        sc.nextLine();

        System.out.print("  Deadline (DD-MM-YYYY) : ");
        String deadline = sc.nextLine().trim();

        System.out.print("  Estimated hours  : ");
        int hours = sc.nextInt();
        sc.nextLine();

        Task t = new Task(name, subject, priority, deadline, hours);
        tasks.add(t);
        DataManager.saveTasks(tasks);

        System.out.println("\n  Task added and saved!");
    }

    // ─── VIEW ALL TASKS ────────────────────────────────────────────
    private static void viewAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\n  No tasks found. Add some tasks first.");
            return;
        }

        System.out.println("\n  ─── All Tasks ───");
        System.out.println(String.format("  %-3s %-25s %-15s %-8s %-12s %-5s %s",
                "#", "Task Name", "Subject", "Priority", "Deadline", "Hrs", "Status"));
        System.out.println("  " + "─".repeat(85));

        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("  %-3d %-25s %-15s %-8s %-12s %-5d %s",
                    (i + 1), t.getName(), t.getSubject(), t.getPriorityLabel(),
                    t.getDeadline(), t.getEstimatedHours(),
                    t.isCompleted() ? "[DONE]" : "[PENDING]"));
        }
    }

    // ─── OPTIMISED SCHEDULE ────────────────────────────────────────
    private static void viewSchedule() {
        if (tasks.isEmpty()) {
            System.out.println("\n  No tasks to schedule.");
            return;
        }

        List<Task> schedule = Scheduler.getSchedule(tasks);

        if (schedule.isEmpty()) {
            System.out.println("\n  All tasks are completed! Great work!");
            return;
        }

        System.out.println("\n  ─── Optimised Study Schedule ───");
        System.out.println("  (Sorted by priority and estimated time — Greedy Algorithm)");
        System.out.println("  " + "─".repeat(85));

        int slot = 1;
        for (Task t : schedule) {
            System.out.println("  Slot " + slot++ + ": " + t);
        }

        System.out.println("\n  Study this in the above order for maximum efficiency.");
    }

    // ─── MARK COMPLETED ────────────────────────────────────────────
    private static void markCompleted() {
        viewAllTasks();
        if (tasks.isEmpty()) return;

        System.out.print("\n  Enter task number to mark as done: ");
        int num = sc.nextInt();
        sc.nextLine();

        if (num < 1 || num > tasks.size()) {
            System.out.println("  Invalid number.");
            return;
        }

        Task t = tasks.get(num - 1);
        if (t.isCompleted()) {
            System.out.println("  This task is already completed.");
        } else {
            t.setCompleted(true);
            DataManager.saveTasks(tasks);
            System.out.println("  \"" + t.getName() + "\" marked as completed!");
        }
    }

    // ─── CHECK CONFLICTS ───────────────────────────────────────────
    private static void checkConflicts() {
        List<String> conflicts = Scheduler.detectConflicts(tasks);

        if (conflicts.isEmpty()) {
            System.out.println("\n  No deadline conflicts found. Your schedule looks good!");
        } else {
            System.out.println("\n  ─── Deadline Conflicts Detected ───");
            for (String c : conflicts) {
                System.out.println(c);
            }
        }
    }

    // ─── DELETE TASK ───────────────────────────────────────────────
    private static void deleteTask() {
        viewAllTasks();
        if (tasks.isEmpty()) return;

        System.out.print("\n  Enter task number to delete: ");
        int num = sc.nextInt();
        sc.nextLine();

        if (num < 1 || num > tasks.size()) {
            System.out.println("  Invalid number.");
            return;
        }

        String name = tasks.get(num - 1).getName();
        tasks.remove(num - 1);
        DataManager.saveTasks(tasks);
        System.out.println("  \"" + name + "\" deleted.");
    }
}