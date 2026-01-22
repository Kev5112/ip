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
    public String toString() {
        return this.getType() + super.toString() + " (from: " + fromDate + " to: " + toDate + ")";
    }
}
