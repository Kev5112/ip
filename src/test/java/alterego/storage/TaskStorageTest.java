package alterego.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import alterego.task.Deadline;
import alterego.task.Event;
import alterego.task.Task;
import alterego.task.TaskList;
import alterego.task.ToDo;
import alterego.utils.DateUtils;

public class TaskStorageTest {
    private TaskStorage taskStorage = new TaskStorage("./data/alterego.AlterEgo1.txt");
    private TaskList taskList;

    @Test
    public void addLoadTest() throws IOException {
        taskStorage.clear();
        taskList = new TaskList(taskStorage);
        taskList.addToDo("a_task_name");
        taskList.addDeadline("a_task_name", "20-04-2026");
        taskList.addEvent("a_task_name", "20-04-2026", "20-04-2026");
        ArrayList<Task> taskLoad = taskStorage.loadTasks();

        ArrayList<Task> compare = new ArrayList<>();
        compare.add(new ToDo("a_task_name"));
        compare.add(new Deadline("a_task_name", DateUtils.parseDateFromInput("20-04-2026")));
        compare.add(new Event("a_task_name", DateUtils.parseDateFromInput("20-04-2026"), DateUtils.parseDateFromInput("20-04-2026")));
        assertEquals(compare, taskLoad);
    }
}
