import java.time.LocalDate;

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
}
