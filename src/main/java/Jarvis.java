import java.util.Scanner;

/**
 * A simple chatbot that echoes commands and exits when asked.
 */
public class Jarvis {
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Prints Jarvis's introductory greeting, then processes commands until the
     * user enters {@code bye}.
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
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(SEPARATOR);
                break;
            }

            System.out.println("     " + command);
            System.out.println(SEPARATOR);
        }
    }
}
