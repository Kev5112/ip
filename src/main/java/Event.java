public class Event extends Task {
    private String fromDate;
    private String toDate;
    public Event(String TaskName, String fromDate, String toDate) {
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
                + " | " + fromDate + " -> " + toDate;
    }

    @Override
    public String toString() {
        return this.getType() + super.getCheckbox() + " "
                + super.toString()
                + " (from: " + fromDate + " to: " + toDate + ")";
    }
}
