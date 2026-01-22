public class Deadline extends Task {
    private String date;

    public Deadline(String TaskName, String date) {
        super(TaskName);
        this.date = date;
    }

    @Override
    public String getType() {
        return "[D]";
    }

    @Override
    public String toString() {
        return this.getType() + super.toString() + " (by: " + date + ")";
    }
}
