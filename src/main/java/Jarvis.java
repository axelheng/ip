import java.util.Scanner;

/**
 * A simple chatbot that stores tasks, displays them on request, and exits
 * when asked.
 */
public class Jarvis {
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Prints Jarvis's introductory greeting, then processes commands until the
     * user enters {@code bye}. Commands other than {@code list} and
     * {@code bye} are stored as tasks in memory.
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
        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println("     " + (i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("     added: " + command);
            }

            System.out.println(SEPARATOR);
        }
    }
}
