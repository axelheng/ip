package jarvis;

import java.util.List;
import java.util.Scanner;

/** Handles all console input and output for Jarvis. */
public class Ui {
    /** Text printed between user interactions. */
    private static final String SEPARATOR = "____________________________________________________________";
    /** Reads commands from standard input. */
    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Jarvis's introductory greeting. */
    public void showWelcome() {
        showSeparator();
        System.out.println("Jarvis");
        System.out.println("Hello! I'm Jarvis.");
        System.out.println("What can I do for you?");
        showSeparator();
    }

    /** Returns whether another command is available from the user. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads and returns the next command from the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the standard separator between interactions. */
    public void showSeparator() {
        System.out.println(SEPARATOR);
    }

    /** Displays Jarvis's goodbye message. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        showSeparator();
    }

    /** Displays the tasks currently in the list. */
    public void showTasks(List<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays an error message caused by invalid user input. */
    public void showError(JarvisException exception) {
        System.out.println("     Oops: " + exception.getMessage());
    }
}
