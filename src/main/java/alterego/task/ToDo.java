package alterego.task;

public class ToDo extends Task {
    public ToDo(String taskName) {
        super(taskName);
    }

    @Override
    public String getType() {
        return "[T]";
    }

    @Override
    public String toFileFormat() {
        return "T | " + (super.isDone() ? "1" : "0") + " | " + super.toString();
    }

    @Override
    public String toString() {
        return this.getType() + super.getCheckbox() + " " + super.toString();
    }
}
