package jarvis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

/** Tests console task-list rendering. */
class UiTest {
    @Test
    void showTasks_varargs_rendersAllTasksInOrder() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        System.setOut(new PrintStream(output));

        try {
            new Ui().showTasks(new Todo("first"), new Todo("second"));
        } finally {
            System.setOut(originalOutput);
        }

        assertEquals("     Here are the tasks in your list:\n"
                + "     1.[T][ ] first\n"
                + "     2.[T][ ] second\n", output.toString());
    }
}
