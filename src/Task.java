public class Task implements Comparable<Task> {

    private String name;
    private String subject;
    private int priority;      // 1 = Low, 2 = Medium, 3 = High
    private String deadline;   // Format: DD-MM-YYYY
    private int estimatedHours;
    private boolean completed;

    public Task(String name, String subject, int priority, String deadline, int estimatedHours) {
        this.name = name;
        this.subject = subject;
        this.priority = priority;
        this.deadline = deadline;
        this.estimatedHours = estimatedHours;
        this.completed = false;
    }

    // Greedy comparator — higher priority comes first
    // If same priority, shorter estimated time comes first (minimises waiting)
    @Override
    public int compareTo(Task other) {
        if (other.priority != this.priority) {
            return other.priority - this.priority; // Higher priority first
        }
        return this.estimatedHours - other.estimatedHours; // Shorter task first
    }

    // ─── Getters ──────────────────────────────────────
    public String getName()         { return name; }
    public String getSubject()      { return subject; }
    public int getPriority()        { return priority; }
    public String getDeadline()     { return deadline; }
    public int getEstimatedHours()  { return estimatedHours; }
    public boolean isCompleted()    { return completed; }

    // ─── Setters ──────────────────────────────────────
    public void setCompleted(boolean completed) { this.completed = completed; }

    // Priority label for display
    public String getPriorityLabel() {
        switch (priority) {
            case 3: return "HIGH  ";
            case 2: return "MEDIUM";
            case 1: return "LOW   ";
            default: return "UNKNOWN";
        }
    }

    // Save to file format: name|subject|priority|deadline|hours|completed
    public String toFileString() {
        return name + "|" + subject + "|" + priority + "|" + deadline + "|" +
               estimatedHours + "|" + completed;
    }

    // Load from file format
    public static Task fromFileString(String line) {
        String[] parts = line.split("\\|");
        Task t = new Task(parts[0], parts[1], Integer.parseInt(parts[2]),
                          parts[3], Integer.parseInt(parts[4]));
        t.setCompleted(Boolean.parseBoolean(parts[5]));
        return t;
    }

    @Override
    public String toString() {
        String status = completed ? "[DONE]" : "[PENDING]";
        return String.format("  %-25s | %-15s | %s | Deadline: %s | %d hrs | %s",
                name, subject, getPriorityLabel(), deadline, estimatedHours, status);
    }
}