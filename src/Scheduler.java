
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Scheduler {

    // 1. Schedule pending tasks by priority and duration
    public static List<Task> getSchedule(List<Task> tasks) {
        PriorityQueue<Task> pq = new PriorityQueue<>();

        for (Task task : tasks) {
            if (!task.isCompleted()) {
                pq.add(task);
            }
        }

        List<Task> schedule = new ArrayList<>();

        while (!pq.isEmpty()) {
            schedule.add(pq.poll());
        }

        return schedule;
    }

    // 2. Priority-aware time-budget scheduling using dynamic programming
    public static void scheduleWithinTime(
            List<Task> tasks, int availableMinutes) {

        if (availableMinutes <= 0) {
            System.out.println("Available time must be positive.");
            return;
        }

        List<Task> pendingTasks = getSchedule(tasks);
        int n = pendingTasks.size();

        if (n == 0) {
            System.out.println("No pending tasks available.");
            return;
        }

        int[] duration = new int[n];
        int[] value = new int[n];

        for (int i = 0; i < n; i++) {
            Task task = pendingTasks.get(i);
            duration[i] = task.getEstimatedHours() * 60;
            value[i] = task.getPriority() * 100;
        }

        int[][] dp = new int[n + 1][availableMinutes + 1];

        for (int i = 1; i <= n; i++) {
            for (int time = 0; time <= availableMinutes; time++) {
                dp[i][time] = dp[i - 1][time];

                if (duration[i - 1] <= time) {
                    int candidate = value[i - 1]
                            + dp[i - 1][time - duration[i - 1]];

                    dp[i][time] = Math.max(dp[i][time], candidate);
                }
            }
        }

        List<Task> selected = new ArrayList<>();
        int remaining = availableMinutes;

        for (int i = n; i > 0; i--) {
            if (dp[i][remaining] != dp[i - 1][remaining]) {
                selected.add(pendingTasks.get(i - 1));
                remaining -= duration[i - 1];
            }
        }

        System.out.println("\n--- PRIORITY-AWARE STUDY PLAN ---");

        int usedMinutes = 0;

        for (int i = selected.size() - 1; i >= 0; i--) {
            Task task = selected.get(i);
            int minutes = task.getEstimatedHours() * 60;
            usedMinutes += minutes;

            System.out.println(
                    (selected.size() - i) + ". " + task.getName()
                    + " | Subject: " + task.getSubject()
                    + " | Priority: " + task.getPriorityLabel()
                    + " | Duration: " + minutes + " minutes"
            );
        }

        if (selected.isEmpty()) {
            System.out.println("No tasks fit within your time budget.");
        }

        System.out.println("\nAvailable time: " + availableMinutes + " minutes");
        System.out.println("Time scheduled: " + usedMinutes + " minutes");
        System.out.println("Time remaining: "
                + (availableMinutes - usedMinutes) + " minutes");
        System.out.println("Priority score: " + dp[n][availableMinutes]);
    }

    // 3. Generate a seven-day study timetable
    public static void generateWeeklyTimetable(
            List<Task> tasks, int dailyHours) {

        if (dailyHours <= 0) {
            System.out.println("Daily study hours must be positive.");
            return;
        }

        List<Task> pendingTasks = getSchedule(tasks);

        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks available.");
            return;
        }

        int dailyLimit = dailyHours * 60;
        int day = 1;
        int usedToday = 0;
        int scheduledCount = 0;

        System.out.println("\n======= YOUR WEEKLY STUDY TIMETABLE =======");

        for (Task task : pendingTasks) {
            int taskMinutes = task.getEstimatedHours() * 60;

            if (taskMinutes > dailyLimit) {
                System.out.println("\nCannot schedule \"" + task.getName()
                        + "\" because it exceeds the daily study limit.");
                continue;
            }

            if (usedToday + taskMinutes > dailyLimit) {
                day++;
                usedToday = 0;
            }

            if (day > 7) {
                break;
            }

            if (usedToday == 0) {
                System.out.println("\nDay " + day + ":");
            }

            System.out.println("  - " + task.getName()
                    + " | Subject: " + task.getSubject()
                    + " | Priority: " + task.getPriorityLabel()
                    + " | Duration: " + taskMinutes + " minutes");

            usedToday += taskMinutes;
            scheduledCount++;
        }

        System.out.println("\n==========================================");
        System.out.println("Tasks scheduled: " + scheduledCount
                + " / " + pendingTasks.size());

        if (scheduledCount < pendingTasks.size()) {
            System.out.println("Some tasks could not fit into this week's plan.");
        }

        System.out.println("Daily study limit: " + dailyHours + " hour(s)");
        System.out.println("==========================================");
    }

    // 4. Detect deadline conflicts
    public static List<String> detectConflicts(List<Task> tasks) {
        List<String> conflicts = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                Task a = tasks.get(i);
                Task b = tasks.get(j);

                if (!a.isCompleted() && !b.isCompleted()
                        && a.getDeadline().equals(b.getDeadline())) {

                    int totalHours =
                            a.getEstimatedHours() + b.getEstimatedHours();

                    if (totalHours > 8) {
                        conflicts.add(
                                "CONFLICT: \"" + a.getName()
                                + "\" and \"" + b.getName()
                                + "\" share deadline " + a.getDeadline()
                                + ". Total: " + totalHours + " hours."
                        );
                    }
                }
            }
        }

        return conflicts;
    }

    // 5. Display study statistics
    public static void printStats(List<Task> tasks) {
        int total = tasks.size();
        int completed = 0;
        int totalHours = 0;
        int remainingHours = 0;
        int highPriority = 0;

        for (Task task : tasks) {
            totalHours += task.getEstimatedHours();

            if (task.isCompleted()) {
                completed++;
            } else {
                remainingHours += task.getEstimatedHours();

                if (task.getPriority() == 3) {
                    highPriority++;
                }
            }
        }

        System.out.println("\n--- STUDY STATISTICS ---");
        System.out.println("Total tasks: " + total);
        System.out.println("Completed: " + completed);
        System.out.println("Pending: " + (total - completed));
        System.out.println("High-priority tasks remaining: " + highPriority);
        System.out.println("Total study hours: " + totalHours);
        System.out.println("Remaining study hours: " + remainingHours);
        System.out.println("Estimated days needed (8 hours/day): "
                + (int) Math.ceil(remainingHours / 8.0));
    }

    // 6. Study Progress Dashboard
    public static void printProgressDashboard(List<Task> tasks) {
        int totalTasks = tasks.size();
        int completedTasks = 0;
        int pendingTasks = 0;

        int totalStudyHours = 0;
        int completedStudyHours = 0;
        int pendingStudyHours = 0;

        Map<String, int[]> subjectProgress = new TreeMap<>();

        for (Task task : tasks) {
            totalStudyHours += task.getEstimatedHours();

            if (task.isCompleted()) {
                completedTasks++;
                completedStudyHours += task.getEstimatedHours();
            } else {
                pendingTasks++;
                pendingStudyHours += task.getEstimatedHours();
            }

            String subject = task.getSubject();

            // [0] = total tasks, [1] = completed tasks
            int[] counts = subjectProgress.computeIfAbsent(
                    subject, key -> new int[2]);

            counts[0]++;

            if (task.isCompleted()) {
                counts[1]++;
            }
        }

        double completionPercentage = totalTasks == 0
                ? 0.0
                : completedTasks * 100.0 / totalTasks;

        System.out.println("\n========== STUDY PROGRESS DASHBOARD ==========");
        System.out.println("Total tasks          : " + totalTasks);
        System.out.println("Completed tasks      : " + completedTasks);
        System.out.println("Pending tasks        : " + pendingTasks);

        System.out.printf("Task completion      : %.1f%%%n",
                completionPercentage);

        System.out.println("----------------------------------------------");
        System.out.println("Total planned hours  : " + totalStudyHours);
        System.out.println("Completed task hours : " + completedStudyHours);
        System.out.println("Pending task hours   : " + pendingStudyHours);

        System.out.println("----------------------------------------------");
        System.out.println("SUBJECT-WISE PROGRESS");

        if (subjectProgress.isEmpty()) {
            System.out.println("No subjects available.");
        } else {
            for (Map.Entry<String, int[]> entry
                    : subjectProgress.entrySet()) {

                String subject = entry.getKey();
                int total = entry.getValue()[0];
                int completed = entry.getValue()[1];

                double percentage = completed * 100.0 / total;

                System.out.printf(
                        "%s: %d/%d tasks completed (%.1f%%)%n",
                        subject, completed, total, percentage);
            }
        }

        System.out.println("==============================================");
    }

    // 7. Dashboard including actual Pomodoro study time
    public static void printProgressDashboard(
            List<Task> tasks, int actualStudyMinutes) {

        printProgressDashboard(tasks);

        System.out.println("\n--- ACTUAL POMODORO STUDY TIME ---");
        System.out.println("Actual study time: "
                + (actualStudyMinutes / 60) + " hour(s), "
                + (actualStudyMinutes % 60) + " minute(s)");
    }
}
