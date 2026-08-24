package jarvis;

import java.util.List;

/** Represents the command that ends the Jarvis session. */
public class ExitCommand extends Command {
    /** Displays the goodbye message and completes the session. */
    @Override
    public void execute(List<Task> tasks, Ui ui, TaskStorage storage) {
        ui.showGoodbye();
    }

    /** Returns true because this command ends the application. */
    @Override
    public boolean isExit() {
        return true;
    }
}
