
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PomodoroTracker {

    private static final Path FILE =
            Paths.get("data", "pomodoro_sessions.txt");

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Save a completed study session to a file
    public void recordSession(int minutes) {

        if (minutes <= 0) {
            System.out.println("Session duration must be positive.");
            return;
        }

        try {
            Files.createDirectories(FILE.getParent());

            String timestamp =
                    LocalDateTime.now().format(FORMATTER);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    FILE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                writer.write(timestamp + "|" + minutes);
                writer.newLine();
            }

            System.out.println("Study session saved successfully!");

        } catch (IOException e) {
            System.out.println("Could not save study session: "
                    + e.getMessage());
        }
    }

    // Load all saved sessions
    public List<String> getSessionHistory() {

        List<String> history = new ArrayList<>();

        if (!Files.exists(FILE)) {
            return history;
        }

        try (BufferedReader reader = Files.newBufferedReader(FILE)) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                if (parts.length == 2) {
                    try {
                        int minutes = Integer.parseInt(parts[1]);

                        if (minutes > 0) {
                            history.add(parts[0] + " | "
                                    + minutes + " minutes");
                        }
                    } catch (NumberFormatException e) {
                        // Ignore malformed records
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read session history: "
                    + e.getMessage());
        }

        return history;
    }

    // Calculate actual study minutes across saved sessions
    public int getTotalStudyMinutes() {

        int total = 0;

        for (String session : getSessionHistory()) {

            int separator = session.lastIndexOf(" | ");

            if (separator >= 0) {
                String duration = session.substring(
                        separator + 3,
                        session.length() - " minutes".length());

                try {
                    total += Integer.parseInt(duration);
                } catch (NumberFormatException e) {
                    // Ignore malformed records
                }
            }
        }

        return total;
    }

    // Display persistent study history
    public void printSessionHistory() {

        List<String> history = getSessionHistory();

        System.out.println("\n======= POMODORO SESSION HISTORY =======");

        if (history.isEmpty()) {
            System.out.println("No completed study sessions yet.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ". " + history.get(i));
            }
        }

        int totalMinutes = getTotalStudyMinutes();

        System.out.println("----------------------------------------");
        System.out.println("Total completed sessions: " + history.size());
        System.out.println("Actual study time: "
                + (totalMinutes / 60) + " hour(s), "
                + (totalMinutes % 60) + " minute(s)");
        System.out.println("========================================");
    }
}
