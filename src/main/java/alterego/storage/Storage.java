package alterego.storage;

import alterego.task.Deadline;
import alterego.task.Event;
import alterego.task.Task;
import alterego.task.ToDo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Storage {
    private final Path filePath;

    public Storage(String path) {
        this.filePath = Path.of(path);
    }

    public void clear() {
        try {
            Files.writeString(filePath, "",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void rewriteFile(ArrayList<Task> Tasks) {
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            System.out.println("Error");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)){
            for (Task task : Tasks) {
                writer.write(task.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addNewTask(Task task) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, task.toFileFormat() + "\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
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
