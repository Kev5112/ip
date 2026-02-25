package alterego.task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import alterego.AlterEgoException;
import alterego.storage.Storage;

/**
 * Manages task operations.
 */
public class TaskList {
    private String loadStatus = null;
    private ArrayList<Task> tasks;
    private Storage storage;

    public String getLoadStatus() {
        return loadStatus;
    }

    /**
     * Creates TaskList with given tasks and storage.
     * @param storage storage handler
     */
    public TaskList(Storage storage) {
        assert storage != null : "Storage cannot be null";
        try {
            this.tasks = storage.loadTasks();
            assert this.tasks != null : "loadTasks() method should not return null";
        } catch (FileNotFoundException e) {
            this.tasks = new ArrayList<Task>();
            loadStatus = "Warning: File not found. Creating a new list.";
        }
        this.storage = storage;
    }

    /**
     * Adds a todo task to the list, then saves the task to storage.
     * Prints out the confirmation
     * @param taskName description of the todo task
     */
    public String addToDo(String taskName) {
        assert taskName != null : "Task name cannot be null";
        Task newTask = new ToDo(taskName);
        assert !newTask.isDone() : "New task should start as not done";
        return addTask(newTask);
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
        try {
            LocalDate date = LocalDate.parse(dateString);
            Task newTask = new Deadline(taskName, date);
            assert !newTask.isDone() : "New task should start as not done";
            return addTask(newTask);
        } catch (DateTimeParseException e) {
            return "Invalid date format. Proper format: yyyy-MM-dd";
        }
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
        try {
            LocalDate fromDate = LocalDate.parse(fromDateString);
            LocalDate toDate = LocalDate.parse(toDateString);
            Task newTask = new Event(taskName, fromDate, toDate);
            assert !newTask.isDone() : "New task should start as not done";
            return addTask(newTask);
        } catch (DateTimeParseException e) {
            return "Invalid date format. Proper format: yyyy-MM-dd";
        }
    }

    /**
     * Shows all tasks in a numbered list, or message if empty.
     */
    public String enumList() {
        if (tasks.isEmpty()) {
            return "No task. You're free to play. Yippie!";
        }

        String accum = "";
        for (int i = 0; i < tasks.size(); i++) {
            Task currTask = tasks.get(i);
            accum += (i + 1) + "." + currTask + "\n";
        }
        return accum;
    }

    public String find(String keyword) {
        assert keyword != null : "null keyword should've been handled";
        if (tasks.isEmpty()) {
            return "No task. You're free to play. Yippie!";
        }

        String accum = "";
        int j = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task currTask = tasks.get(i);
            if (currTask.toString().contains(keyword)) {
                accum += (j + 1) + "." + currTask + "\n";
                j++;
            }
        }
        if (j == 0) {
            return "No search result found.";
        }
        return accum;
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
        return ioExceptionCatcher(() -> storage.rewriteFile(tasks), successMessage);
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
        return ioExceptionCatcher(() -> storage.rewriteFile(tasks), successMessage);
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
        String successMessage = "Noted. I've removed this task:\n " + removedTask + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
        return ioExceptionCatcher(() -> storage.rewriteFile(tasks), successMessage);
    }

    /**
     * Clears all tasks, clears storage, and shows confirmation.
     */
    public String clear() {
        tasks = new ArrayList<Task>();
        String successMessage = "Cleared data from storage. You have 0 task now.";
        return ioExceptionCatcher(storage::clear, successMessage);
    }

    private String addTask(Task newTask) {
        tasks.add(newTask);
        String message = "Got it. I've added this task:\n " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.\n";
        return ioExceptionCatcher(() -> storage.addNewTask(newTask), message);
    }

    private String ioExceptionCatcher(FileOperation fileOperation, String successMessage) {
        try {
            fileOperation.execute();
            return successMessage;
        } catch (IOException e) {
            return "Error: IO exception";
        }
    }

    @FunctionalInterface
    private interface FileOperation {
        void execute() throws IOException;
    }
}
