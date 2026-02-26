package alterego.task;

import alterego.utils.DateUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a Deadline task with a due date.
 */
public class Deadline extends Task {
    private LocalDate date;
    private String stringType = "[D]";

    /**
     * Creates a new Deadline task.
     * @param taskName description of the deadline
     * @param date due date for the deadline
     */
    public Deadline(String taskName, LocalDate date) {
        super(taskName);
        assert date != null : "Deadline date cannot be null";
        this.date = date;
    }

    /**
     * Converts deadline to format for file storage.
     * Format: D | 1/0 | description | date
     * Format: D | 1/0 | description | date | assignedName|assignedRelationship
     * @return formatted string for saving to file
     */
    @Override
    public String toFileFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("D | ").append(super.isDone() ? "1" : "0").append(" | ").append(super.getTaskName())
                .append(" | ").append(DateUtils.formatToString(date));

        if (super.getAssignedTo() != null) {
            sb.append(" | ").append(super.getAssignedTo().getName())
                    .append("|").append(super.getAssignedTo().getRelationship());
        }

        return sb.toString();
    }

    /**
     * Returns string representation for display.
     * Format: [D][X] description [→ contact] (by: MMM d yyyy)
     * @return formatted display string
     */
    @Override
    public String toString() {
        return this.stringType + super.getCheckbox() + " "
                + super.toString() + " (by: " + DateUtils.formatToString(date) + ")";
    }

    /**
     * Compares the equality of this and other object
     * Two deadlines are equal if they have same description, done status, and date.
     * @param obj object to compare with
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        assert super.equals(obj) : "should not reach here";
        Deadline other = (Deadline) obj;
        boolean result = Objects.equals(date, other.date);
        return result;
    }

    /**
     * Returns hash code based on task description, done status, and date.
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }
}
