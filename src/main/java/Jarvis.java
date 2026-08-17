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
            } else if (command.startsWith("mark ")) {
                String taskNumberText = command.substring("mark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber >= 1 && taskNumber <= taskCount) {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [X] " + tasks[taskIndex].getDescription());
                    } else {
                        System.out.println("     There is no task with that number.");
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("     Please provide a valid task number.");
                }
            } else if (command.startsWith("unmark ")) {
                String taskNumberText = command.substring("unmark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber >= 1 && taskNumber <= taskCount) {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       [ ] " + tasks[taskIndex].getDescription());
                    } else {
                        System.out.println("     There is no task with that number.");
                    }
                } catch (NumberFormatException exception) {
                    System.out.println("     Please provide a valid task number.");
                }
            } else {
                Task task = parseTask(command);
                tasks[taskCount] = task;
                taskCount++;
                System.out.println("     Got it. I've added this task:");
                System.out.println("       " + task);
                System.out.println("     Now you have " + taskCount + " tasks in the list.");
            }

            System.out.println(SEPARATOR);
        }
    }

    /** Converts a task-creation command into its corresponding task subtype. */
    private static Task parseTask(String command) {
        if (command.startsWith("todo ")) {
            return new Todo(command.substring("todo ".length()));
        }

        if (command.startsWith("deadline ")) {
            String remainder = command.substring("deadline ".length());
            int byIndex = remainder.indexOf(" /by ");
            if (byIndex >= 0) {
                return new Deadline(remainder.substring(0, byIndex),
                        remainder.substring(byIndex + " /by ".length()));
            }
        }

        if (command.startsWith("event ")) {
            String remainder = command.substring("event ".length());
            int fromIndex = remainder.indexOf(" /from ");
            int toIndex = remainder.indexOf(" /to ", fromIndex + 1);
            if (fromIndex >= 0 && toIndex >= 0) {
                return new Event(remainder.substring(0, fromIndex),
                        remainder.substring(fromIndex + " /from ".length(), toIndex),
                        remainder.substring(toIndex + " /to ".length()));
            }
        }

        // Preserve the original behavior for an untyped command.
        return new Todo(command);
    }
}
