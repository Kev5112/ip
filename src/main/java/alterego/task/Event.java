package alterego.task;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an Event task with start and end dates.
 */
public class Event extends Task {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String stringType = "[E]";

    /**
     * Creates a new Event task.
     * @param taskName description of the event
     * @param fromDate start date of the event
     * @param toDate end date of the event
     */
    public Event(String taskName, LocalDate fromDate, LocalDate toDate) {
        super(taskName);
        assert fromDate != null && fromDate != null : "Event date cannot be null";
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    /**
     * Converts event to format for file storage.
     * Format: "E | 1/0 | description | fromDate -> toDate"
     * @return formatted string for saving to file
     */
    @Override
    public String toFileFormat() {
        return "E | " + (super.isDone() ? "1" : "0") + " | " + super.toString()
                + " | " + dateFormat(fromDate) + " -> " + dateFormat(toDate);
    }

    /**
     * Returns string representation for display.
     * Format: "[E][X/""] description (from: MMM d yyyy to: MMM d yyyy)"
     * @return formatted display string
     */

    @Override
    public String toString() {
        return this.stringType + super.getCheckbox() + " "
                + super.toString()
                + " (from: " + dateFormat(fromDate) + " to: " + dateFormat(toDate) + ")";
    }

    /**
     * Compares this event with another object for equality.
     * Two events are equal if they have same description, done status, and dates.
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
        Event other = (Event) obj;
        boolean result = Objects.equals(fromDate, other.fromDate) && Objects.equals(toDate, other.toDate);
        return result;
    }

    /**
     * Returns hash code based on task description, done status, and dates.
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromDate, toDate);
    }
}
