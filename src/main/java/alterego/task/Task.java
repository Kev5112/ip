package alterego.task;

import alterego.contact.Contact;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Abstract base class for all task types (Todo, Deadline, Event).
 */
public abstract class Task {
    private String taskName;
    private boolean isDone;
    private Contact assignedTo;

    /**
     * Creates a task with the given name, initially not done.
     * @param taskName description of the task
     */
    public Task(String taskName) {
        assert taskName != null : "Task name cannot be null";
        this.taskName = taskName;
        this.isDone = false;
        this.assignedTo = null;
    }

    public void assignTo(Contact contact) {
        this.assignedTo = contact;
    }

    public Contact getAssignedTo() {
        return assignedTo;
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
    @Override
    public String toString() {
        String assignment = (assignedTo != null) ? " [→ " + assignedTo.getName() + "]" : "";
        return taskName + assignment;
    }

    public String getTaskName() {
        return taskName;
    }

    /**
     * Compares this task with another object for equality.
     * Two tasks are equal if they have same description and done status.
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
        boolean result = isDone == other.isDone && Objects.equals(taskName, other.taskName);
        return result;
    }

    /**
     * Returns hash code based on task description and done status.
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskName, isDone);
    }

}
