import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String FILE_PATH = "data/tasks.txt";

    // Save all tasks to file
    public static void saveTasks(List<Task> tasks) {
        try {
            File folder = new File("data");
            if (!folder.exists()) folder.mkdirs();

            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
            for (Task t : tasks) {
                bw.write(t.toFileString());
                bw.newLine();
            }
            bw.close();
            System.out.println("\n  Tasks saved successfully.");
        } catch (IOException e) {
            System.out.println("  Error saving tasks: " + e.getMessage());
        }
    }

    // Load all tasks from file
    public static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return tasks;

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    tasks.add(Task.fromFileString(line));
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("  Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }
}