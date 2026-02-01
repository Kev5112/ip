package alterego.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import alterego.task.Deadline;
import alterego.task.Event;
import alterego.task.Task;
import alterego.task.ToDo;
import alterego.ui.Ui;

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
        this.filePath = path;
    }

    /**
     * Clears all tasks from storage file.
     */
    public void clear() {
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            Ui.show("Error");
        }
    }

    /**
     * Overwrites storage file with current task list/state.
     * @param tasks list of tasks to save
     */
    public void rewriteFile(ArrayList<Task> tasks) {
        try {
            FileWriter fw = new FileWriter(filePath);
            for (Task task : tasks) {
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            Ui.show("Error");
        }
    }

    /**
     * Appends a single task to storage file.
     * @param task task to append to file
     */
    public void addNewTask(Task task) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            fw.write(task.toFileFormat() + System.lineSeparator());
            fw.close();
        } catch (IOException e) {
            Ui.show("Error");
        }
    }

    /**
     * Loads tasks from storage file.
     * @return list of loaded tasks, empty if file doesn't exist
     */
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            File f = new File(filePath);
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                String nextString = s.nextLine().trim();
                if (nextString.isEmpty()) {
                    continue;
                }
                Task nextTask = parseTask(nextString);
                if (nextTask != null) {
                    tasks.add(nextTask);
                }
            }
        } catch (FileNotFoundException e) {
            Ui.show("Error");
        }

        return tasks;
    }

    private Task parseTask(String line) {
        Task task = null;
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }
        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            return null;
        }
        if (parts[0].equals("T")) {
            task = new ToDo(parts[2]);
            if (parts[1].equals("1")) {
                task.setDone();
            }
        } else if (parts[0].equals("D")) {
            task = new Deadline(parts[2],
                    LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("MMM d yyyy")));
            if (parts[1].equals("1")) {
                task.setDone();
            }
        } else if (parts[0].equals("E")) {
            String[] dates = parts[3].split(" -> ");
            task = new Event(parts[2],
                    LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("MMM d yyyy")),
                    LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("MMM d yyyy")));
            if (parts[1].equals("1")) {
                task.setDone();
            }
        }
        return task;
    }
}
