public abstract class Task {
    protected String taskName;
    protected boolean isDone;

    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
    }

    public void setDone() {
        this.isDone = true;
    }

    public void setUndone() {
        this.isDone = false;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getCheckbox() {
        return isDone ? "[X]" : "[ ]";
    }

    public abstract String getType();

    public String toString() {
        return getCheckbox() + " " + taskName;
    }
}
