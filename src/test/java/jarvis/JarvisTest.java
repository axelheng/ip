package jarvis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/** Tests command parsing and validation performed by Jarvis. */
class JarvisTest {
    @Test
    void parseTask_todoCommand_returnsTodoTask() throws JarvisException {
        Task task = Jarvis.parseTask("todo read book");

        assertInstanceOf(Todo.class, task);
        assertEquals("read book", task.getDescription());
    }

    @Test
    void parseTask_deadlineCommand_returnsDeadlineTask() throws JarvisException {
        Task task = Jarvis.parseTask("deadline submit report /by 2026-09-20");

        Deadline deadline = assertInstanceOf(Deadline.class, task);
        assertEquals(LocalDate.of(2026, 9, 20), deadline.getBy());
    }

    @Test
    void parseTask_eventCommand_returnsEventTask() throws JarvisException {
        Task task = Jarvis.parseTask("event project meeting /from Mon 2pm /to Mon 4pm");

        Event event = assertInstanceOf(Event.class, task);
        assertEquals("Mon 2pm", event.getFrom());
        assertEquals("Mon 4pm", event.getTo());
    }

    @Test
    void parseTask_unknownCommand_throwsJarvisException() {
        JarvisException exception = assertThrows(JarvisException.class, () ->
                Jarvis.parseTask("unknown command"));

        assertEquals("I don't recognize that command. Try todo, deadline, event, list, find, mark, "
                + "unmark, delete, snooze, or bye.", exception.getMessage());
    }

    @Test
    void parseTaskNumber_nonNumericArgument_throwsJarvisException() {
        JarvisException exception = assertThrows(JarvisException.class, () ->
                Jarvis.parseTaskNumber("delete nope", "delete"));

        assertEquals("Please provide a valid task number after delete.", exception.getMessage());
    }

    @Test
    void parseTaskNumber_zeroArgument_throwsJarvisException() {
        assertThrows(JarvisException.class,
                () -> Jarvis.parseTaskNumber("delete 0", "delete"));
    }

    @Test
    void parseTask_blankCommand_throwsJarvisException() {
        assertThrows(JarvisException.class, () -> Jarvis.parseTask("   "));
    }
}
