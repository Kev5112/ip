package alterego.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import alterego.task.Deadline;
import alterego.task.Event;
import alterego.task.Task;
import alterego.task.TaskList;
import alterego.task.ToDo;




public class StorageTest {
    private Storage storage = new Storage("./data/alterego.AlterEgo.txt");
    private TaskList taskList = new TaskList(new ArrayList<>(), storage);

    @Test
    public void addLoadTest() {
        storage.clear();
        taskList.addToDo("a_task_name");
        taskList.addDeadline("a_task_name", "2026-04-20");
        taskList.addEvent("a_task_name", "2026-04-20", "2026-04-20");
        ArrayList<Task> taskLoad = storage.loadTasks();

        ArrayList<Task> compare = new ArrayList<>();
        compare.add(new ToDo("a_task_name"));
        compare.add(new Deadline("a_task_name", LocalDate.parse("2026-04-20")));
        compare.add(new Event("a_task_name", LocalDate.parse("2026-04-20"), LocalDate.parse("2026-04-20")));
        assertEquals(compare, taskLoad);
    }
}
