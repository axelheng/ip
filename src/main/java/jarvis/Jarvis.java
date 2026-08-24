package jarvis;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * A simple chatbot that stores typed tasks, displays them on request, and
 * exits when asked.
 */
public class Jarvis {
    /**
     * Prints Jarvis's introductory greeting, then processes commands until the
     * user enters {@code bye}. Tasks are created with {@code todo},
     * {@code deadline}, or {@code event} commands and can be removed with
     * {@code delete}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();
        TaskStorage storage = new TaskStorage();
        List<Task> tasks = storage.load();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showSeparator();

            if (command.equals("bye")) {
                Command exitCommand = new ExitCommand();
                try {
                    exitCommand.execute(tasks, ui, storage);
                } catch (JarvisException exception) {
                    ui.showError(exception);
                }
                if (exitCommand.isExit()) {
                    break;
                }
            }

            if (command.equals("list")) {
                ui.showTasks(tasks);
            } else if (command.equals("delete") || command.startsWith("delete ")) {
                try {
                    int taskNumber = parseTaskNumber(command, "delete");
                    if (taskNumber < 1 || taskNumber > tasks.size()) {
                        throw new JarvisException("There is no task with that number.");
                    }
                    Task removedTask = tasks.remove(taskNumber - 1);
                    storage.save(tasks);
                    System.out.println("     Noted. I've removed this task:");
                    System.out.println("       " + removedTask);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                } catch (JarvisException exception) {
                    ui.showError(exception);
                }
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                try {
                    int taskNumber = parseTaskNumber(command, "mark");
                    if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                        int taskIndex = taskNumber - 1;
                        tasks.get(taskIndex).markAsDone();
                        storage.save(tasks);
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [X] " + tasks.get(taskIndex).getDescription());
                    } else {
                        throw new JarvisException("There is no task with that number.");
                    }
                } catch (JarvisException exception) {
                    ui.showError(exception);
                }
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                try {
                    int taskNumber = parseTaskNumber(command, "unmark");
                    if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                        int taskIndex = taskNumber - 1;
                        tasks.get(taskIndex).markAsNotDone();
                        storage.save(tasks);
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       [ ] " + tasks.get(taskIndex).getDescription());
                    } else {
                        throw new JarvisException("There is no task with that number.");
                    }
                } catch (JarvisException exception) {
                    ui.showError(exception);
                }
            } else {
                try {
                    Task task = parseTask(command);
                    tasks.add(task);
                    storage.save(tasks);
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + task);
                    System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
                } catch (JarvisException exception) {
                    ui.showError(exception);
                }
            }

            ui.showSeparator();
        }
    }

    /** Converts a task-creation command into its corresponding task subtype. */
    private static Task parseTask(String command) throws JarvisException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.equals("todo") ? "" : command.substring("todo ".length());
            return new Todo(requireDescription(description, "todo"));
        }

        if (command.startsWith("deadline ")) {
            String remainder = command.substring("deadline ".length());
            int byIndex = remainder.indexOf(" /by ");
            if (byIndex >= 0) {
                String description = requireDescription(remainder.substring(0, byIndex), "deadline");
                String by = requirePart(remainder.substring(byIndex + " /by ".length()), "deadline date");
                try {
                    return new Deadline(description, LocalDate.parse(by));
                } catch (DateTimeParseException exception) {
                    throw new JarvisException("A deadline date must use yyyy-mm-dd, for example: 2019-10-15");
                }
            }
            throw new JarvisException("A deadline needs a description and a date, for example: deadline report /by Friday");
        }

        if (command.startsWith("event ")) {
            String remainder = command.substring("event ".length());
            int fromIndex = remainder.indexOf(" /from ");
            int toIndex = remainder.indexOf(" /to ", fromIndex + 1);
            if (fromIndex >= 0 && toIndex >= 0) {
                String description = requireDescription(remainder.substring(0, fromIndex), "event");
                String from = requirePart(remainder.substring(fromIndex + " /from ".length(), toIndex), "event start time");
                String to = requirePart(remainder.substring(toIndex + " /to ".length()), "event end time");
                return new Event(description, from, to);
            }
            throw new JarvisException("An event needs a description, start time, and end time, for example: event meeting /from 2pm /to 3pm");
        }

        throw new JarvisException("I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /** Parses the numeric argument of a task-list action. */
    private static int parseTaskNumber(String command, String action) throws JarvisException {
        String taskNumberText = command.substring(action.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new JarvisException("Please provide a task number after " + action + ".");
        }
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new JarvisException("Please provide a valid task number after " + action + ".");
        }
    }

    /** Validates the description portion of a task-creation command. */
    private static String requireDescription(String description, String command) throws JarvisException {
        return requirePart(description, command + " description");
    }

    /** Validates and trims a required command component. */
    private static String requirePart(String value, String partName) throws JarvisException {
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            throw new JarvisException("A " + partName + " cannot be empty.");
        }
        return trimmedValue;
    }

}
