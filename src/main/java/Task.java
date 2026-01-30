import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        return this.isDone() ? "[X]" : "[ ]";
    }

    public abstract String getType();

    public abstract String toFileFormat();

    public String toString() {
        return taskName;
    }

    public static String dateFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }
}
