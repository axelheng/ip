/**
 * A simple chatbot that greets the user and exits.
 */
public class Jarvis {
    private static final String SEPARATOR = "____________________________________________________________";

    /**
     * Prints Jarvis's introductory greeting and farewell message.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        System.out.println(SEPARATOR);
        System.out.println("Jarvis");
        System.out.println("Hello! I'm Jarvis.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}
