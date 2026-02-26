package alterego.task;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import alterego.contact.Contact;
import alterego.storage.TaskStorage;
import alterego.utils.AlterEgoException;
import alterego.utils.DateUtils;
import alterego.utils.ExceptionCatcher;

/**
 * Manages task operations.
 */
public class TaskList {
    private String loadStatus = null;
    private ArrayList<Task> tasks;
    private Set<Task> taskSet;
    private TaskStorage taskStorage;

    public String getLoadStatus() {
        return loadStatus;
    }

    /**
     * Creates TaskList with given tasks and storage.
     * @param taskStorage storage handler
     */
    public TaskList(TaskStorage taskStorage) {
        assert taskStorage != null : "Storage cannot be null";
        try {
            this.tasks = taskStorage.loadTasks().getTasks();
            assert this.tasks != null : "loadTasks() method should not return null";
        } catch (FileNotFoundException e) {
            this.tasks = new ArrayList<Task>();
            loadStatus = "Warning: File not found. Creating a new list.";
        }
        this.taskStorage = taskStorage;
        this.taskSet = new HashSet<>(tasks);
    }

    /**
     * Adds a todo task to the list, then saves the task to storage.
     * Prints out the confirmation
     * @param taskName description of the todo task
     */
    public String addToDo(String taskName) {
        assert taskName != null : "Task name cannot be null";
        Task newTask = new ToDo(taskName);
        handleDuplicate(newTask);
        return addTask(newTask);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasks);
    }

    /**
     * Adds a deadline task to the list. Requires description and date as arguments.
     * Date should be a String with format yyyy-MM-dd.
     * Saves the task to storage immediately and prints out the confirmation.
     * @param taskName description of the deadline task
     * @param dateString deadline date in yyyy-MM-dd format
     */
    public String addDeadline(String taskName, String dateString) {
        assert taskName != null : "Task name cannot be null";
        assert dateString != null : "Date string cannot be null";
        LocalDate date = DateUtils.parseDateFromInput(dateString);
        Task newTask = new Deadline(taskName, date);
        handleDuplicate(newTask);
        return addTask(newTask);
    }

    /**
     * Adds an event task to the list. Requires description startDate, and endDate as arguments.
     * Date should be a String with format yyyy-MM-dd.
     * Saves the task to storage immediately and prints out the confirmation.
     * @param taskName description of the event task
     * @param fromDateString start date in yyyy-MM-dd format
     * @param toDateString end date in yyyy-MM-dd format
     */
    public String addEvent(String taskName, String fromDateString, String toDateString) {
        assert taskName != null : "Task name cannot be null";
        assert fromDateString != null && toDateString != null : "Date string cannot be null";
        LocalDate fromDate = DateUtils.parseDateFromInput(fromDateString);
        LocalDate toDate = DateUtils.parseDateFromInput(toDateString);
        if (toDate.isBefore(fromDate)) {
            throw new AlterEgoException("Error: End date cannot be before start date!");
        }
        Task newTask = new Event(taskName, fromDate, toDate);
        handleDuplicate(newTask);
        String warning = "";
        if (hasOverlap(fromDate, toDate)) {
            warning = "\nWarning: Overlapping timing!";
        }
        String message = addTask(newTask);
        return message + warning;
    }

    /**
     * Shows all tasks in a numbered list, or message if empty.
     */
    public String enumList() {
        if (tasks.isEmpty()) {
            return "No task. You're free to play. Yippie!";
        }

        return IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + "." + tasks.get(i) + "\n")
                .collect(Collectors.joining()) + "\n";
    }

    public String find(String keyword) {
        assert keyword != null : "null keyword should've been handled";
        if (tasks.isEmpty()) {
            return "No task. You're free to play. Yippie!";
        }

        String result = IntStream.range(0, tasks.size())
                .filter(i -> tasks.get(i).toString().contains(keyword))
                .mapToObj(i -> (i + 1) + "." + tasks.get(i) + "\n")
                .collect(Collectors.joining());

        return result.isEmpty() ? "No search result found." : result + "\n";
    }

    /**
     * Marks a task as done, update changes in storage, then prints confirmation.
     * @param taskNumber task number (1-based index)
     * @throws AlterEgoException if task number is invalid
     */
    public String mark(int taskNumber) throws AlterEgoException {
        assert taskNumber > 0 : "Task number should be positive";
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setDone();
        assert currTask.isDone() : "setDone() doesn't work";
        String successMessage = "Nice! I've marked this task as done:\n " + currTask;
        return ExceptionCatcher.catchIoException(() -> taskStorage.rewriteFile(tasks), successMessage);
    }

    /**
     * Marks a task as not done, update changes in storage, then prints confirmation.
     * @param taskNumber task number (1-based index)
     * @throws AlterEgoException if task number is invalid
     */
    public String unmark(int taskNumber) throws AlterEgoException {
        assert taskNumber > 0 : "Task number should be positive";
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setUndone();
        assert !currTask.isDone() : "setUndone() doesn't work";
        String successMessage = "OK, I've marked this task as not done yet:\n " + currTask;
        return ExceptionCatcher.catchIoException(() -> taskStorage.rewriteFile(tasks), successMessage);
    }

    /**
     * Deletes a task, update changes in storage, then prints confirmation with updated count.
     * @param taskNumber task number (1-based index)
     * @throws AlterEgoException if task number is invalid
     */
    public String delete(int taskNumber) throws AlterEgoException {
        assert taskNumber > 0 : "Task number should be positive";
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task removedTask = tasks.remove(taskNumber - 1);
        taskSet.remove(removedTask);
        String successMessage = "Noted. I've removed this task:\n " + removedTask + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
        return ExceptionCatcher.catchIoException(() -> taskStorage.rewriteFile(tasks), successMessage);
    }

    public String assignTask(int taskNumber, Contact contact) {
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task task = tasks.get(taskNumber - 1);
        task.assignTo(contact);
        String message = task + " assigned to " + contact;
        return ExceptionCatcher.catchIoException(() -> taskStorage.rewriteFile(tasks), message);
    }

    public void unassignTask(Contact contact) {
        for (Task task : tasks) {
            if (contact.equals(task.getAssignedTo())) {
                task.assignTo(null);
            }
        }
        ExceptionCatcher.catchIoException(() -> taskStorage.rewriteFile(tasks), null);
    }

    /**
     * Clears all tasks, clears storage, and shows confirmation.
     */
    public String clear() {
        tasks = new ArrayList<Task>();
        taskSet = new HashSet<>();
        String successMessage = "Cleared data from storage. You have 0 task now.";
        return ExceptionCatcher.catchIoException(taskStorage::clear, successMessage);
    }

    public int getSize() {
        return tasks.size();
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    private void handleDuplicate(Task task) throws AlterEgoException {
        if (taskSet.contains(task)) {
            throw new AlterEgoException("Task already exists!");
        }
        taskSet.add(task);
    }

    private String addTask(Task newTask) {
        tasks.add(newTask);
        String message = "Got it. I've added this task:\n " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.\n";
        return ExceptionCatcher.catchIoException(() -> taskStorage.addNewTask(newTask), message);
    }

    private boolean hasOverlap(LocalDate newFrom, LocalDate newTo) {
        long overlapCount = tasks.stream()
                .filter(task -> task instanceof Event)
                .map(task -> (Event) task)
                .filter(event -> newTo.isAfter(event.getFromDate()) && newFrom.isBefore(event.getToDate()))
                .count();
        return overlapCount > 0;
    }

}
