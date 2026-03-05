package alterego.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import alterego.list.TaskList;
import alterego.task.Task;
import alterego.utils.AlterEgoException;

public class TaskStorageTest {

    @TempDir
    Path tempDir;

    private TaskStorage taskStorage;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("tasks.txt").toString();
        taskStorage = new TaskStorage(testFilePath);
    }

    @Test
    void testSaveAndLoadTasks() throws AlterEgoException {
        // Create a TaskList and add various tasks
        TaskList taskList = new TaskList(taskStorage);

        // Add different types of tasks
        taskList.addToDo("read book");
        taskList.addDeadline("return book", "20-04-2026");
        taskList.addEvent("conference", "20-04-2026", "22-04-2026");

        // Get the tasks ArrayList
        ArrayList<Task> originalTasks = taskList.getTasks();

        // Get the loaded ArrayList
        TaskList newTaskList = new TaskList(taskStorage);
        ArrayList<Task> loadedTasks = newTaskList.getTasks();

        // Verify same number of tasks
        assertEquals(originalTasks.size(), loadedTasks.size(),
                "Number of tasks should match after load");

        // Verify each task type and content
        for (int i = 0; i < originalTasks.size(); i++) {
            Task original = originalTasks.get(i);
            Task loaded = loadedTasks.get(i);

            // Each content has to match
            assertEquals(original, loaded, "Task content should match " + i);
        }
    }
}
