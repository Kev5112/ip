package alterego.task;

import java.util.Objects;

import alterego.contact.Contact;
import alterego.data.Storable;
import alterego.utils.AlterEgoException;

/**
 * Represents abstract base class for all task types (Todo, Deadline, Event).
 */
public abstract class Task implements Storable {
    private String taskName;
    private boolean isDone;
    private Contact assignedTo;

    /**
     * Creates a task with the given name, initially not done.
     * @param taskName description of the task
     */
    public Task(String taskName) {
        assert taskName != null : "Task name cannot be null";
        if (taskName.isBlank()) {
            throw new AlterEgoException("Task name cannot be blank");
        }
        this.taskName = taskName;
        this.isDone = false;
        this.assignedTo = null;
    }

    /**
     * Assigns a contact to this task.
     * @param contact Contact to assign
     */
    public void assignTo(Contact contact) {
        this.assignedTo = contact;
    }

    /**
     * Returns the contact assigned to this task.
     * @return Assigned contact, or null if unassigned
     */
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

    /**
     * Returns the checkbox representation of task status.
     * @return "[X]" if done, "[ ]" if not done
     */
    protected String getCheckbox() {
        return this.isDone() ? "[X]" : "[ ]";
    }

    /**
     * Converts task to file storage format.
     * @return formatted string for file storage
     */
    @Override
    public abstract String toFileFormat();

    /**
     * Returns task name with its assigned contact.
     * Format: description [-> contact]
     * @return String representation
     */
    @Override
    public String toString() {
        String assignment = (assignedTo != null) ? " [→ " + assignedTo.getName() + "]" : "";
        return taskName + assignment;
    }

    /**
     * Returns the task name.
     * @return Task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Compares the equality of this with another object
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
