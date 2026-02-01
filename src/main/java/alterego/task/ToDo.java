package alterego.task;

/**
 * Represents a Todo task with only a description, no date.
 */
public class ToDo extends Task {

    /**
     * Creates a new Todo task.
     * @param taskName description of the todo task
     */
    public ToDo(String taskName) {
        super(taskName);
    }

    private String stringType() {
        return "[T]";
    }

    /**
     * Converts task to format for file storage.
     * Format: "T | 1/0 | description"
     * @return formatted string for saving to file
     */
    @Override
    public String toFileFormat() {
        return "T | " + (super.isDone() ? "1" : "0") + " | " + super.toString();
    }

    /**
     * Returns string representation for display.
     * Format: "[T][X/""] description"
     * @return formatted display string
     */
    @Override
    public String toString() {
        return this.stringType() + super.getCheckbox() + " " + super.toString();
    }
}
