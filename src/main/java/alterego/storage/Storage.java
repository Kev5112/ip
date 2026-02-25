package alterego.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import alterego.utils.AlterEgoException;
import alterego.task.Deadline;
import alterego.task.Event;
import alterego.task.Task;
import alterego.task.ToDo;
import alterego.utils.DateUtils;

/**
 * Handles file storage operations for tasks.
 */
public class Storage {
    private final String filePath;

    /**
     * Set a file corresponding to the path as the storage.
     * @param path file path for storing tasks
     */
    public Storage(String path) {
        assert path != null : "File path cannot be null";
        this.filePath = path;
    }

    /**
     * Clears all tasks from storage file.
     */
    public void clear() throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write("");
        fw.close();
    }

    /**
     * Overwrites storage file with current task list/state.
     * @param tasks list of tasks to save
     */
    public void rewriteFile(ArrayList<Task> tasks) throws IOException {
        assert tasks != null : "List of tasks cannot be null";
        FileWriter fw = new FileWriter(filePath);
        for (Task task : tasks) {
            assert task != null : "Task in list should not be null";
            fw.write(task.toFileFormat() + System.lineSeparator());
        }
        fw.close();

        File f = new File(filePath);
        assert f.exists() : "File should exist after writing";
        assert f.length() > 0 || tasks.isEmpty()
                : "File should have content if tasks not empty";
    }

    /**
     * Appends a single task to storage file.
     * @param task task to append to file
     */
    public void addNewTask(Task task) throws IOException {
        assert task != null : "Task to add cannot be null";
        FileWriter fw = new FileWriter(filePath, true);
        fw.write(task.toFileFormat() + System.lineSeparator());
        fw.close();

        File f = new File(filePath);
        assert f.exists() : "File should exist after appending";
    }

    /**
     * Loads tasks from storage file.
     * @return list of loaded tasks, empty if file doesn't exist
     */
    public ArrayList<Task> loadTasks() throws FileNotFoundException, AlterEgoException {
        ArrayList<Task> tasks = new ArrayList<>();
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String nextString = s.nextLine().trim();
            if (nextString.isEmpty()) {
                continue;
            }
            Task nextTask = parseTask(nextString);
            assert nextTask != null : "Should not return null after parsing";
            tasks.add(nextTask);
        }

        return tasks;
    }

    private Task parseTask(String line) throws AlterEgoException {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            throw new AlterEgoException("Problem with file: Not enough arguments. "
                    + "Please edit manually or perform 'clear'");
        }
        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            throw new AlterEgoException("Problem with file: task doneness should be '0' or '1'. "
                    + "Please edit manually or perform 'clear'");
        }

        switch (parts[0]) {
        case "T":
            return handleTodo(parts);
        case "D":
            return handleDeadline(parts);
        case "E":
            return handleEvent(parts);
        default:
            throw new AlterEgoException("Problem with file: task type should be 'T', 'D', or 'E'. "
                    + "Please edit manually or perform 'clear'");
        }
    }

    private Task handleTodo(String[] parts) throws AlterEgoException {
        if (parts.length != 3) {
            throw new AlterEgoException("Problem with file: Todo needs exactly 3 parts separated by '|' "
                    + "Please edit manually or perform 'clear'");
        }

        ToDo todo = new ToDo(parts[2]);
        if (parts[1].equals("1")) {
            todo.setDone();
        }
        return todo;
    }

    private Task handleDeadline(String[] parts) throws AlterEgoException {
        if (parts.length != 4) {
            throw new AlterEgoException("Problem with file: Deadline needs exactly 4 parts separated by '|' "
                    + "Please edit manually or perform 'clear'");
        }

        LocalDate date = DateUtils.parseDateFromFile(parts[3]);
        Deadline deadline = new Deadline(parts[2], date);

        if (parts[1].equals("1")) {
            deadline.setDone();
        }
        return deadline;
    }

    private Task handleEvent(String[] parts) throws AlterEgoException {
        if (parts.length != 4) {
            throw new AlterEgoException("Problem with file: Event needs exactly 4 parts separated by '|' "
                    + "Please edit manually or perform 'clear'");
        }

        String[] dates = parts[3].split(" -> ");
        if (dates.length != 2) {
            throw new AlterEgoException("Event should have two dates separated by ' -> '");
        }

        LocalDate fromDate = DateUtils.parseDateFromFile(dates[0]);
        LocalDate toDate = DateUtils.parseDateFromFile(dates[1]);
        Event event = new Event(parts[2], fromDate, toDate);
        if (parts[1].equals("1")) {
            event.setDone();
        }
        return event;
    }
}
