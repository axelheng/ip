package jarvis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Loads and saves Jarvis tasks in a file relative to the project directory. */
public class TaskStorage {
    /** Location of the human-readable task database. */
    private static final Path DATA_FILE = Paths.get("data", "jarvis.txt");

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

    /** Converts a task into one line of the storage format. */
    private String formatTask(Task task) {
        String type = task instanceof Deadline ? "D" : task instanceof Event ? "E" : "T";
        String details = "";
        if (task instanceof Deadline) {
            details = ((Deadline) task).getBy().toString();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            details = event.getFrom() + " " + event.getTo();
        }
        return type + " | " + (task.getStatusIcon().equals("X") ? "1" : "0") + " | "
                + task.getDescription() + (details.isEmpty() ? "" : " | " + details);
    }

    /** Converts one storage line into a task, or returns {@code null} when invalid. */
    private Task parseLine(String line) {
        try {
            String[] parts = line.split("\\s*\\|\\s*", -1);
            if (parts.length < 3 || (!parts[0].equals("T") && !parts[0].equals("D") && !parts[0].equals("E"))
                    || (!parts[1].equals("0") && !parts[1].equals("1")) || parts[2].trim().isEmpty()) {
                return null;
            }
            String description = parts[2].trim();
            Task task;
            if (parts[0].equals("D") && parts.length == 4 && !parts[3].trim().isEmpty()) {
                task = new Deadline(description, LocalDate.parse(parts[3].trim()));
            } else if (parts[0].equals("E") && parts.length == 4) {
                String eventDetails = parts[3].trim();
                int separator = eventDetails.lastIndexOf(' ');
                if (separator <= 0 || separator == eventDetails.length() - 1) {
                    return null;
                }
                String from = eventDetails.substring(0, separator);
                String to = eventDetails.substring(separator + 1);
                task = new Event(description, from, to);
            } else if (parts[0].equals("T") && parts.length == 3) {
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

}
