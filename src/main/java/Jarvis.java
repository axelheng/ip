import java.util.Scanner;

/**
 * A simple chatbot that stores typed tasks, displays them on request, and
 * exits when asked.
 */
public class Jarvis {
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Prints Jarvis's introductory greeting, then processes commands until the
     * user enters {@code bye}. Tasks are created with {@code todo},
     * {@code deadline}, or {@code event} commands.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("Jarvis");
        System.out.println("Hello! I'm Jarvis.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                break;
            }

            if (command.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.equals("mark") || command.startsWith("mark ")) {
                try {
                    int taskNumber = parseTaskNumber(command, "mark");
                    if (taskNumber >= 1 && taskNumber <= taskCount) {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [X] " + tasks[taskIndex].getDescription());
                    } else {
                        throw new JarvisException("There is no task with that number.");
                    }
                } catch (JarvisException exception) {
                    printError(exception);
                }
            } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                try {
                    int taskNumber = parseTaskNumber(command, "unmark");
                    if (taskNumber >= 1 && taskNumber <= taskCount) {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       [ ] " + tasks[taskIndex].getDescription());
                    } else {
                        throw new JarvisException("There is no task with that number.");
                    }
                } catch (JarvisException exception) {
                    printError(exception);
                }
            } else {
                try {
                    Task task = parseTask(command);
                    tasks[taskCount] = task;
                    taskCount++;
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + task);
                    System.out.println("     Now you have " + taskCount + " tasks in the list.");
                } catch (JarvisException exception) {
                    printError(exception);
                }
            }

            System.out.println(SEPARATOR);
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
                return new Deadline(description, by);
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

        throw new JarvisException("I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or bye.");
    }

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

    private static String requireDescription(String description, String command) throws JarvisException {
        return requirePart(description, command + " description");
    }

    private static String requirePart(String value, String partName) throws JarvisException {
        String trimmedValue = value.trim();
        if (trimmedValue.isEmpty()) {
            throw new JarvisException("A " + partName + " cannot be empty.");
        }
        return trimmedValue;
    }

    private static void printError(JarvisException exception) {
        System.out.println("     Oops: " + exception.getMessage());
    }
}
