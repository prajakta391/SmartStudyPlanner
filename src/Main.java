
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static List<Task> tasks = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static PomodoroTracker pomodoroTracker = new PomodoroTracker();

    public static void main(String[] args) {

        tasks = DataManager.loadTasks();

        System.out.println("==========================================");
        System.out.println("       SMART STUDY PLANNER v1.3");
        System.out.println("       Study Time-Budget Scheduler");
        System.out.println("==========================================");
        System.out.println(tasks.size() + " task(s) loaded.\n");

        boolean running = true;

        while (running) {
            System.out.println("\n------------- MAIN MENU ----------------");
            System.out.println("1. Add a new task");
            System.out.println("2. View all tasks");
            System.out.println("3. Get optimised study schedule");
            System.out.println("4. Mark task as completed");
            System.out.println("5. Check for deadline conflicts");
            System.out.println("6. View study statistics");
            System.out.println("7. Delete a task");
            System.out.println("8. Create time-budget study plan");
            System.out.println("9. Start Pomodoro study timer");
            System.out.println("10. Generate weekly timetable");
            System.out.println("11. Study progress dashboard");
            System.out.println("12. Exit");
            System.out.println("----------------------------------------");
            System.out.print("Choose (1-12): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addTask();
                    break;

                case 2:
                    viewAllTasks();
                    break;

                case 3:
                    viewSchedule();
                    break;

                case 4:
                    markCompleted();
                    break;

                case 5:
                    checkConflicts();
                    break;

                case 6:
                    Scheduler.printStats(tasks);
                    pomodoroTracker.printSessionHistory();
                    break;

                case 7:
                    deleteTask();
                    break;

                case 8:
                    createTimeBudgetPlan();
                    break;

                case 9:
                    startPomodoro();
                    break;

                case 10:
                    System.out.print("Daily study limit (hours): ");
                    int dailyHours = sc.nextInt();
                    sc.nextLine();

                    Scheduler.generateWeeklyTimetable(tasks, dailyHours);
                    break;

                case 11:
                    Scheduler.printProgressDashboard(
                            tasks,
                            pomodoroTracker.getTotalStudyMinutes()
                    );
                    break;

                case 12:
                    DataManager.saveTasks(tasks);
                    System.out.println("Goodbye! Happy studying!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1-12.");
            }
        }

        sc.close();
    }

    // Add a new task
    private static void addTask() {
        System.out.println("\n--- Add New Task ---");

        System.out.print("Task name: ");
        String name = sc.nextLine().trim();

        System.out.print("Subject: ");
        String subject = sc.nextLine().trim();

        System.out.print("Priority (1=Low, 2=Medium, 3=High): ");
        int priority = sc.nextInt();
        sc.nextLine();

        if (priority < 1 || priority > 3) {
            System.out.println("Priority must be 1, 2, or 3.");
            return;
        }

        System.out.print("Deadline (DD-MM-YYYY): ");
        String deadline = sc.nextLine().trim();

        System.out.print("Estimated hours: ");
        int hours = sc.nextInt();
        sc.nextLine();

        if (hours <= 0) {
            System.out.println("Hours must be greater than zero.");
            return;
        }

        Task task = new Task(name, subject, priority, deadline, hours);
        tasks.add(task);
        DataManager.saveTasks(tasks);

        System.out.println("Task added successfully!");
    }

    // View all tasks
    private static void viewAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found. Add a task first.");
            return;
        }

        System.out.println("\n--- All Tasks ---");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    // View optimised schedule
    private static void viewSchedule() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        List<Task> schedule = Scheduler.getSchedule(tasks);

        if (schedule.isEmpty()) {
            System.out.println("All tasks are completed!");
            return;
        }

        System.out.println("\n--- Optimised Study Schedule ---");

        int slot = 1;

        for (Task task : schedule) {
            System.out.println("Slot " + slot++ + ": " + task);
        }
    }

    // Mark a task as completed
    private static void markCompleted() {
        viewAllTasks();

        if (tasks.isEmpty()) {
            return;
        }

        System.out.print("Enter task number to mark as completed: ");
        int number = sc.nextInt();
        sc.nextLine();

        if (number < 1 || number > tasks.size()) {
            System.out.println("Invalid task number.");
            return;
        }

        Task task = tasks.get(number - 1);

        if (task.isCompleted()) {
            System.out.println("Task is already completed.");
        } else {
            task.setCompleted(true);
            DataManager.saveTasks(tasks);

            System.out.println(task.getName() + " marked as completed!");
        }
    }

    // Check deadline conflicts
    private static void checkConflicts() {
        List<String> conflicts = Scheduler.detectConflicts(tasks);

        if (conflicts.isEmpty()) {
            System.out.println("No deadline conflicts found.");
        } else {
            System.out.println("\n--- Deadline Conflicts ---");

            for (String conflict : conflicts) {
                System.out.println(conflict);
            }
        }
    }

    // Delete a task
    private static void deleteTask() {
        viewAllTasks();

        if (tasks.isEmpty()) {
            return;
        }

        System.out.print("Enter task number to delete: ");
        int number = sc.nextInt();
        sc.nextLine();

        if (number < 1 || number > tasks.size()) {
            System.out.println("Invalid task number.");
            return;
        }

        String name = tasks.remove(number - 1).getName();
        DataManager.saveTasks(tasks);

        System.out.println(name + " deleted successfully.");
    }

    // Create a time-budget study plan
    private static void createTimeBudgetPlan() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available. Add tasks first.");
            return;
        }

        System.out.print("Available study time (minutes): ");
        int minutes = sc.nextInt();
        sc.nextLine();

        if (minutes <= 0) {
            System.out.println("Enter a positive number of minutes.");
            return;
        }

        Scheduler.scheduleWithinTime(tasks, minutes);
    }

    // Pomodoro study timer menu
    private static void startPomodoro() {

        boolean continueSessions = true;

        while (continueSessions) {

            System.out.println("\n--- POMODORO STUDY TIMER ---");
            System.out.println("1. Start 25-minute study session");
            System.out.println("2. Take 5-minute break");
            System.out.println("3. View session summary");
            System.out.println("4. Return to main menu");
            System.out.println("5. View saved session history");
            System.out.println("----------------------------------------");
            System.out.print("Choose (1-5): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1: {
                    System.out.println("\nFocus time! Keep studying.");

                    boolean completed = runTimer(25 * 60);

                    if (completed) {
                        pomodoroTracker.recordSession(25);

                        System.out.println("Study session completed!");
                    } else {
                        System.out.println(
                                "Session interrupted; not recorded.");
                    }
                    break;
                }

                case 2: {
                    System.out.println("\nRelax! Your break starts now.");

                    boolean breakCompleted = runTimer(5 * 60);

                    if (breakCompleted) {
                        System.out.println(
                                "Break finished. Ready to study?");
                    } else {
                        System.out.println("Break timer interrupted.");
                    }
                    break;
                }

                case 3:
                    pomodoroTracker.printSessionHistory();
                    break;

                case 4:
                    continueSessions = false;
                    break;

                case 5:
                    pomodoroTracker.printSessionHistory();
                    break;

                default:
                    System.out.println("Invalid choice. Enter 1-5.");
            }
        }
    }

    // Run a countdown timer
    private static boolean runTimer(int totalSeconds) {

        try {
            for (int remaining = totalSeconds;
                    remaining > 0;
                    remaining--) {

                if (remaining % 60 == 0 || remaining <= 10) {
                    int minutes = remaining / 60;
                    int seconds = remaining % 60;

                    System.out.printf(
                            "\rTime remaining: %02d:%02d ",
                            minutes,
                            seconds
                    );

                    System.out.flush();
                }

                Thread.sleep(1000);
            }

            System.out.println("\rTime remaining: 00:00");
            System.out.println("Time is up!");

            return true;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("\nTimer interrupted.");

            return false;
        }
    }
}
