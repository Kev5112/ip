package alterego.task;

import java.time.LocalDate;
import java.util.Objects;

public class Deadline extends Task {
    private LocalDate date;

    public Deadline(String TaskName, LocalDate date) {
        super(TaskName);
        this.date = date;
    }

    @Override
    public String getType() {
        return "[D]";
    }

    @Override
    public String toFileFormat() {
        return "D | " + (super.isDone() ? "1" : "0") + " | " + super.toString()
                + " | " + dateFormat(date);
    }

    @Override
    public String toString() {
        return this.getType() + super.getCheckbox() + " " +
                super.toString() + " (by: " + dateFormat(date) + ")";
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

        Deadline other = (Deadline) obj;
        return Objects.equals(date, other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), date);
    }
}
