import java.time.LocalDate;

public class Event extends Task {
    private LocalDate fromDate;
    private LocalDate toDate;
    public Event(String TaskName, LocalDate fromDate, LocalDate toDate) {
        super(TaskName);
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
}
