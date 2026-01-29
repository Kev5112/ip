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
    public String toFileFormat() {
        return "D | " + (super.isDone() ? "1" : "0") + " | " + super.toString()
                + " | " + date;
    }

    @Override
    public String toString() {
        return this.getType() + super.getCheckbox() + " " +
                super.toString() + " (by: " + date + ")";
    }
}
