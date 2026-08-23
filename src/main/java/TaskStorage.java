import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/** Loads and saves Jarvis tasks in a file relative to the project directory. */
public class TaskStorage {
    private static final Path DATA_FILE = Paths.get("data", "duke.txt");

    /** Loads all valid tasks from the data file, returning an empty list if it is unavailable. */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(DATA_FILE)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)) {
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException exception) {
            // Jarvis can still start with an empty list when the file cannot be read.
        }
        return tasks;
    }

    /** Saves the current tasks, creating the data directory when necessary. */
    public void save(List<Task> tasks) {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(DATA_FILE, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            // A storage failure should not terminate the chatbot.
        }
    }

    private String formatTask(Task task) {
        String type = task instanceof Deadline ? "D" : task instanceof Event ? "E" : "T";
        String details = "";
        String encodedDescription = encode(task.getDescription());
        if (task instanceof Deadline) {
            details = task.toString();
            details = encode(details.substring(details.indexOf("(by: ") + 6, details.length() - 1));
        } else if (task instanceof Event) {
            details = task.toString();
            int fromStart = details.indexOf("(from: ") + 7;
            int toStart = details.indexOf(" to: ", fromStart);
            details = encode(details.substring(fromStart, toStart)) + "|" + encode(details.substring(toStart + 5, details.length() - 1));
        }
        return type + "|" + (task.getStatusIcon().equals("X") ? "1" : "0") + "|" + encodedDescription + "|" + details;
    }

    private Task parseLine(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 4 || (!parts[0].equals("T") && !parts[0].equals("D") && !parts[0].equals("E"))) {
                return null;
            }
            String description = decode(parts[2]);
            Task task;
            if (parts[0].equals("D") && parts.length == 4) {
                task = new Deadline(description, decode(parts[3]));
            } else if (parts[0].equals("E") && parts.length == 5) {
                task = new Event(description, decode(parts[3]), decode(parts[4]));
            } else if (parts[0].equals("T") && parts.length == 4 && parts[3].isEmpty()) {
                task = new Todo(description);
            } else {
                return null;
            }
            task.restoreStatus(parts[1].equals("1"));
            return task;
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
