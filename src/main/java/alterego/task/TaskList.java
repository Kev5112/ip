package alterego.task;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import alterego.AlterEgoException;
import alterego.storage.Storage;
import alterego.ui.Ui;

/**
 * Manages task operations.
 */
public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    /**
     * Creates TaskList with given tasks and storage.
     * @param storage storage handler
     */
    public TaskList(Storage storage) {
        try {
            this.tasks = storage.loadTasks();
        } catch (FileNotFoundException e) {
            this.tasks = new ArrayList<Task>();
        }
        this.storage = storage;
    }

    /**
     * Adds a todo task to the list, then saves the task to storage.
     * Prints out the confirmation
     * @param taskName description of the todo task
     */
    public String addToDo(String taskName) {
        Task newTask = new ToDo(taskName);
        tasks.add(newTask);
        String successMessage = Ui.decorate("Got it. I've added this task:\n "
                + newTask + "\n" + "Now you have " + tasks.size()
                + " tasks in the list.\n");
        return modifyStorage(() -> storage.addNewTask(newTask), successMessage);
    }

    /**
     * Adds a deadline task to the list. Requires description and date as arguments.
     * Date should be a String with format yyyy-MM-dd.
     * Saves the task to storage immediately and prints out the confirmation.
     * @param taskName description of the deadline task
     * @param dateString deadline date in yyyy-MM-dd format
     */
    public String addDeadline(String taskName, String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            Task newTask = new Deadline(taskName, date);
            tasks.add(newTask);
            String successMessage = Ui.decorate("Got it. I've added this task:\n "
                    + newTask + "\n" + "Now you have " + tasks.size()
                    + " tasks in the list.\n");
            return modifyStorage(() -> storage.addNewTask(newTask), successMessage);
        } catch (DateTimeParseException e) {
            return Ui.decorate("Invalid date format. Proper format: yyyy-MM-dd");
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
        try {
            LocalDate fromDate = LocalDate.parse(fromDateString);
            LocalDate toDate = LocalDate.parse(toDateString);
            Task newTask = new Event(taskName, fromDate, toDate);
            tasks.add(newTask);
            String successMessage = Ui.decorate("Got it. I've added this task:\n "
                    + newTask + "\n" + "Now you have " + tasks.size()
                    + " tasks in the list.\n");
            return modifyStorage(() -> storage.addNewTask(newTask), successMessage);
        } catch (DateTimeParseException e) {
            return Ui.decorate("Invalid date format. Proper format: yyyy-MM-dd");
        }
    }

    /**
     * Shows all tasks in a numbered list, or message if empty.
     */
    public String enumList() {
        if (tasks.isEmpty()) {
            return Ui.decorate("No task. You're free to play. Yippie!");
        } else {
            String accum = "";
            for (int i = 0; i < tasks.size(); i++) {
                Task currTask = tasks.get(i);
                accum += (i + 1) + "." + currTask + "\n";
            }
            return Ui.decorate(accum);
        }
    }

    public String find(String keyword) {
        if (tasks.isEmpty()) {
            return Ui.decorate("No task. You're free to play. Yippie!");
        } else {
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
                return Ui.decorate("No search result found.");
            }
            return Ui.decorate(accum);
        }
    }

    /**
     * Marks a task as done, update changes in storage, then prints confirmation.
     * @param taskNumber task number (1-based index)
     * @throws AlterEgoException if task number is invalid
     */
    public String mark(int taskNumber) throws AlterEgoException {
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setDone();
        String successMessage = Ui.decorate("Nice! I've marked this task as done:\n " + currTask);
        return modifyStorage(() -> storage.rewriteFile(tasks), successMessage);
    }

    /**
     * Marks a task as not done, update changes in storage, then prints confirmation.
     * @param taskNumber task number (1-based index)
     * @throws AlterEgoException if task number is invalid
     */
    public String unmark(int taskNumber) throws AlterEgoException {
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setUndone();
        String successMessage = Ui.decorate("OK, I've marked this task as not done yet:\n " + currTask);
        return modifyStorage(() -> storage.rewriteFile(tasks), successMessage);
    }

    /**
     * Deletes a task, update changes in storage, then prints confirmation with updated count.
     * @param taskNumber task number (1-based index)
     * @throws AlterEgoException if task number is invalid
     */
    public String delete(int taskNumber) throws AlterEgoException {
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task removedTask = tasks.remove(taskNumber - 1);
        String successMessage = Ui.decorate("Noted. I've removed this task:\n " + removedTask + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.");
        return modifyStorage(() -> storage.rewriteFile(tasks), successMessage);
    }

    /**
     * Clears all tasks, clears storage, and shows confirmation.
     */
    public String clear() {
        tasks = new ArrayList<Task>();
        String successMessage = Ui.decorate("Cleared data from storage. You have 0 task now.");
        return modifyStorage(storage::clear, successMessage);
    }

    private String modifyStorage(FileOperation fileOperation, String successMessage) {
        try {
            fileOperation.execute();
            return successMessage;
        } catch (IOException e) {
            return Ui.decorate("Error: IO exception");
        }
    }

    @FunctionalInterface
    private interface FileOperation {
        void execute() throws IOException;
    }
}
