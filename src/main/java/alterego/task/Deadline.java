package alterego.task;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a Deadline task with a due date.
 */
public class Deadline extends Task {
    private LocalDate date;

    /**
     * Creates a new Deadline task.
     * @param taskName description of the deadline
     * @param date due date for the deadline
     */
    public Deadline(String taskName, LocalDate date) {
        super(taskName);
        this.date = date;
    }

    private String stringType() {
        return "[D]";
    }

    /**
     * Converts deadline to format for file storage.
     * Format: "D | 1/0 | description | date"
     * @return formatted string for saving to file
     */
    @Override
    public String toFileFormat() {
        return "D | " + (super.isDone() ? "1" : "0") + " | " + super.toString()
                + " | " + dateFormat(date);
    }

    /**
     * Returns string representation for display.
     * Format: "[D][X/""] description (by: MMM d yyyy)"
     * @return formatted display string
     */
    @Override
    public String toString() {
        return this.stringType() + super.getCheckbox() + " "
                + super.toString() + " (by: " + dateFormat(date) + ")";
    }

    /**
     * Compares this deadline with another object for equality.
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

        Deadline other = (Deadline) obj;
        return Objects.equals(date, other.date);
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
