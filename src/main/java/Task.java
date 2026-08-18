/** Represents a task and whether it has been completed. */
public class Task {
    private final String description;
    private TaskStatus status;

    /**
     * Creates a new incomplete task.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        status = TaskStatus.DONE;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        status = TaskStatus.NOT_DONE;
    }

    /**
     * Returns the status icon used when displaying this task.
     *
     * @return {@code X} for a done task, otherwise a blank space
     */
    public String getStatusIcon() {
        return status == TaskStatus.DONE ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the one-letter marker used for this task type.
     *
     * @return {@code T} for a basic task
     */
    protected String getTypeIcon() {
        return "T";
    }

    /**
     * Returns the task in the format used by the command-line interface.
     *
     * @return the type marker, completion marker, and description
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
