package alterego.task;

import java.time.LocalDate;
import java.util.Objects;

public class Event extends Task {
    private LocalDate fromDate;
    private LocalDate toDate;
    public Event(String taskName, LocalDate fromDate, LocalDate toDate) {
        super(taskName);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String getType() {
        return "[E]";
    }

    @Override
    public String toFileFormat() {
        return "E | " + (super.isDone() ? "1" : "0") + " | " + super.toString()
                + " | " + dateFormat(fromDate) + " -> " + dateFormat(toDate);
    }

    @Override
    public String toString() {
        return this.getType() + super.getCheckbox() + " "
                + super.toString()
                + " (from: " + dateFormat(fromDate) + " to: " + dateFormat(toDate) + ")";
    }

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

        Event other = (Event) obj;
        return Objects.equals(fromDate, other.fromDate) && Objects.equals(toDate, other.toDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromDate, toDate);
    }
}
