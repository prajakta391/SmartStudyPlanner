import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Scheduler {

    // ─── CORE GREEDY SCHEDULING ALGORITHM ─────────────────────────
    // Uses a Priority Queue (min-heap adapted to max-priority)
    // to schedule tasks in optimal order.
    // This is the same principle used in CPU process scheduling.

    public static List<Task> getSchedule(List<Task> tasks) {
        // Add all pending tasks into priority queue
        // PriorityQueue uses Task.compareTo() — higher priority comes first
        PriorityQueue<Task> pq = new PriorityQueue<>();

        for (Task t : tasks) {
            if (!t.isCompleted()) {
                pq.add(t);
            }
        }

        // Pull tasks out in greedy order
        List<Task> schedule = new ArrayList<>();
        while (!pq.isEmpty()) {
            schedule.add(pq.poll());
        }

        return schedule;
    }

    // ─── CONFLICT DETECTION ────────────────────────────────────────
    // Detects if two tasks have the same deadline AND total hours
    // would exceed 8 hours in a day (realistic daily study limit)

    public static List<String> detectConflicts(List<Task> tasks) {
        List<String> conflicts = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                Task a = tasks.get(i);
                Task b = tasks.get(j);

                if (!a.isCompleted() && !b.isCompleted()) {
                    if (a.getDeadline().equals(b.getDeadline())) {
                        int totalHours = a.getEstimatedHours() + b.getEstimatedHours();
                        if (totalHours > 8) {
                            conflicts.add("  CONFLICT: \"" + a.getName() + "\" and \""
                                + b.getName() + "\" both due on " + a.getDeadline()
                                + " — total " + totalHours + " hrs exceeds 1-day limit (8 hrs)."
                                + "\n  SUGGESTION: Move \"" + b.getName()
                                + "\" deadline one day earlier to spread the load.");
                        }
                    }
                }
            }
        }

        return conflicts;
    }

    // ─── STUDY STATS ───────────────────────────────────────────────
    public static void printStats(List<Task> tasks) {
        int total = tasks.size();
        int completed = 0;
        int totalHours = 0;
        int remainingHours = 0;
        int highPriority = 0;

        for (Task t : tasks) {
            totalHours += t.getEstimatedHours();
            if (t.isCompleted()) {
                completed++;
            } else {
                remainingHours += t.getEstimatedHours();
                if (t.getPriority() == 3) highPriority++;
            }
        }

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  STUDY STATISTICS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  Total tasks        : " + total);
        System.out.println("  Completed          : " + completed);
        System.out.println("  Pending            : " + (total - completed));
        System.out.println("  High priority left : " + highPriority);
        System.out.println("  Total study hours  : " + totalHours + " hrs");
        System.out.println("  Remaining hours    : " + remainingHours + " hrs");
        System.out.println("  Days needed (8h/d) : ~" + (int) Math.ceil(remainingHours / 8.0) + " days");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}