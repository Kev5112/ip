package alterego.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Abstract base class for all task types (Todo, Deadline, Event).
 */
public abstract class Task {
    protected String taskName;
    protected boolean isDone;

    /**
     * Creates a task with the given name, initially not done.
     * @param taskName description of the task
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void setDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void setUndone() {
        this.isDone = false;
    }

    /**
     * Checks if task is done.
     * @return true if task is done, false otherwise
     */
    public boolean isDone() {
        return this.isDone;
    }

    protected String getCheckbox() {
        return this.isDone() ? "[X]" : "[ ]";
    }
    /**
     * Converts task to file storage format.
     * @return formatted string for file storage
     */
    public abstract String toFileFormat();

    /**
     * Gets task description.
     * @return task name
     */
    public String toString() {
        return taskName;
    }

    /**
     * Formats a date as "MMM d yyyy" (e.g., "Mar 15 2024").
     * @param date the date to format
     * @return formatted date string
     */
    public static String dateFormat(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    /**
     * Compares this task with another object for equality.
     * Two tasks are equal if they have same description and done status.
     * This method (including overriden one) is AI-generated
     * @param obj object to compare with
     * @return true if tasks are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Task other = (Task) obj;
        return isDone == other.isDone && Objects.equals(taskName, other.taskName);
    }

    /**
     * Returns hash code based on task description and done status.
     * This method (including overriden one) is AI-generated
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskName, isDone);
    }

}
