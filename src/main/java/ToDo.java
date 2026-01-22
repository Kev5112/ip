public class ToDo extends Task {
    public ToDo(String taskName) {
        super(taskName);
    }

    @Override
    public String getType() {
        return "[T]";
    }

    @Override
    public String toString() {
        return this.getType() + super.toString();
    }
}
