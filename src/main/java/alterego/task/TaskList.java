package alterego.task;

import alterego.*;
import alterego.storage.Storage;
import alterego.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    public TaskList(ArrayList<Task> tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }

    public void addToDo(String taskName) {
        Task newTask = new ToDo(taskName);
        tasks.add(newTask);
        storage.addNewTask(newTask);
        Ui.show("Got it. I've added this task:\n "
                + newTask + "\n" + "Now you have " + tasks.size()
                + " tasks in the list.\n");
    }

    public void addDeadline(String taskName, String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            Task newTask = new Deadline(taskName, date);
            tasks.add(newTask);
            storage.addNewTask(newTask);
            Ui.show("Got it. I've added this task:\n "
                    + newTask + "\n" + "Now you have " + tasks.size()
                    + " tasks in the list.\n");
        } catch (DateTimeParseException e) {
            Ui.show("Invalid date format. Proper format: yyyy-MM-dd");
        }
    }

    public void addEvent(String taskName, String fromDateString, String toDateString) {
        try {
            LocalDate fromDate = LocalDate.parse(fromDateString);
            LocalDate toDate = LocalDate.parse(toDateString);
            Task newTask = new Event(taskName, fromDate, toDate);
            tasks.add(newTask);
            storage.addNewTask(newTask);
            Ui.show("Got it. I've added this task:\n "
                    + newTask + "\n" + "Now you have " + tasks.size()
                    + " tasks in the list.\n");
        } catch (DateTimeParseException e) {
            Ui.show("Invalid date format. Proper format: yyyy-MM-dd");
        }
    }

    public void enumList() {
        if (tasks.isEmpty()) {
            Ui.show("No task. You're free to play. Yippie!");
        } else {
            String accum = "";
            for (int i = 0; i < tasks.size(); i++) {
                Task currTask = tasks.get(i);
                accum += (i + 1) + "." + currTask + "\n";
            }
            Ui.show(accum);
        }
    }

    public void mark(int taskNumber) throws AlterEgoException {
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setDone();
        storage.rewriteFile(tasks);
        Ui.show("Nice! I've marked this task as done:\n " + currTask);
    }

    public void unmark(int taskNumber) throws AlterEgoException {
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setUndone();
        storage.rewriteFile(tasks);
        Ui.show("OK, I've marked this task as not done yet:\n " + currTask);
    }

    public void delete(int taskNumber) throws AlterEgoException {
        if (taskNumber > tasks.size()) {
            throw new AlterEgoException("There's only " + tasks.size() + " tasks here!");
        }
        Task removedTask = tasks.remove(taskNumber - 1);
        storage.rewriteFile(tasks);
        Ui.show("Noted. I've removed this task:\n " + removedTask + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.");
    }

    public void clear() {
        tasks = new ArrayList<Task>();
        storage.clear();
        Ui.show("Cleared data from storage. You have 0 task now.");
    }

}
