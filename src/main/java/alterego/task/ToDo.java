package alterego.task;

/**
 * Represents a Todo task with only a description, no date.
 */
public class ToDo extends Task {
    private String stringType = "[T]";

    /**
     * Creates a new Todo task.
     * @param taskName description of the todo task
     */
    public ToDo(String taskName) {
        super(taskName);
    }

    /**
     * Converts this todo task to file storage format.
     * Format: T | 1/0 | description
     * Format: T | 1/0 | description | assignedName|assignedRelationship
     * @return Formatted string for file storage
     */
    @Override
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("T | ").append(super.isDone() ? "1" : "0").append(" | ").append(super.getTaskName());

        if (super.getAssignedTo() != null) {
            sb.append(" | ").append(super.getAssignedTo().getName())
                    .append("|").append(super.getAssignedTo().getRelationship());
        }

        return sb.toString();
    }
    /**
     * Returns string representation for display.
     * Format: [T][X/""] description [-> contact]
     * @return formatted display string
     */
    @Override
    public String toString() {
        return this.stringType + super.getCheckbox() + " " + super.toString();
    }
}
