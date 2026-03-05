package alterego.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import alterego.contact.Contact;
import alterego.list.TaskList;
import alterego.storage.TaskStorage;
import alterego.utils.AlterEgoException;


/**
 * The testcases below are AI generated, with personal
 * modifications according to the intended behavior of the Parser
 */
public class TaskListTest {

    @TempDir
    Path tempDir;

    private TaskList taskList;
    private TaskStorage taskStorage;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("tasks.txt").toString();
        taskStorage = new TaskStorage(testFilePath);
        taskList = new TaskList(taskStorage);
    }

    @Test
    void constructor_fileExists_createsTaskList() {
        assertNotNull(taskList);
        assertEquals(0, taskList.getSize());
    }

    @Test
    void constructor_fileNotFound_createsEmptyListWithWarning() {
        // File doesn't exist
        TaskList newList = new TaskList(taskStorage);
        assertNotNull(newList.getLoadStatus());
        assertTrue(newList.getLoadStatus().contains("Warning"));
        assertEquals(0, newList.getSize());
    }

    @Test
    void addToDo_validInput_addsTask() throws AlterEgoException {
        String result = taskList.addToDo("Read book");

        assertTrue(result.contains("Got it"));
        assertTrue(result.contains("Read book"));
        assertEquals(1, taskList.getSize());
    }

    @Test
    void addToDo_nullTaskName_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> {
            taskList.addToDo(null);
        });
    }

    @Test
    void addToDo_emptyTaskName_addsTask() throws AlterEgoException {
        String result = taskList.addToDo("");

        assertTrue(result.contains("Got it"));
        assertEquals(1, taskList.getSize());
    }

    @Test
    void addDeadline_validDate_addsDeadline() throws AlterEgoException {
        String result = taskList.addDeadline("Return book", "01-02-2024");

        assertTrue(result.contains("Got it"));
        assertTrue(result.contains("Return book"));
        assertEquals(1, taskList.getSize());

        Task task = taskList.getTask(0);
        assertTrue(task instanceof Deadline);
    }

    @Test
    void addDeadline_invalidDate_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            taskList.addDeadline("Return book", "invalid-date");
        });
    }

    @Test
    void addDeadline_nullDateString_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> {
            taskList.addDeadline("Return book", null);
        });
    }

    @Test
    void addEvent_validDates_addsEvent() throws AlterEgoException {
        String result = taskList.addEvent("Conference", "01-12-2024", "03-12-2024");

        assertTrue(result.contains("Got it"));
        assertTrue(result.contains("Conference"));
        assertEquals(1, taskList.getSize());

        Task task = taskList.getTask(0);
        assertTrue(task instanceof Event);
    }

    @Test
    void addEvent_endDateBeforeStartDate_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            taskList.addEvent("Conference", "03-12-2024", "01-12-2024"); // end before start
        });
    }

    @Test
    void addEvent_overlappingDates_showsWarning() throws AlterEgoException {
        taskList.addEvent("Conference", "01-12-2024", "03-12-2024");
        String result = taskList.addEvent("Workshop", "02-12-2024", "04-12-2024"); // overlaps

        assertTrue(result.contains("Overlapping"));
    }

    @Test
    void addEvent_nonOverlappingDates_noWarning() throws AlterEgoException {
        taskList.addEvent("Conference", "01-12-2024", "03-12-2024");
        String result = taskList.addEvent("Workshop", "04-12-2024", "06-12-2024"); // no overlap

        assertFalse(result.contains("Overlapping"));
    }

    @Test
    void enumList_emptyList_returnsNoTaskMessage() {
        String result = taskList.enumList();
        assertEquals("No task. You're free to play. Yippie!", result);
    }

    @Test
    void enumList_withTasks_returnsNumberedList() throws AlterEgoException {
        taskList.addToDo("Read book");
        taskList.addToDo("Write code");

        String result = taskList.enumList();
        System.out.print(result);

        assertTrue(result.contains("Read book"));
        assertTrue(result.contains("Write code"));
    }

    @Test
    void find_existingKeyword_returnsMatchingTasks() throws AlterEgoException {
        taskList.addToDo("Read book");
        taskList.addToDo("Buy book");
        taskList.addToDo("Write code");

        String result = taskList.find("book");

        assertTrue(result.contains("Read book"));
        assertTrue(result.contains("Buy book"));
        assertFalse(result.contains("Write code"));
    }

    @Test
    void find_nonExistingKeyword_returnsNoResultMessage() throws AlterEgoException {
        taskList.addToDo("Read book");

        String result = taskList.find("xyz");

        assertEquals("No search result found.", result);
    }

    @Test
    void find_emptyList_returnsNoTaskMessage() {
        String result = taskList.find("anything");
        assertEquals("No task. You're free to play. Yippie!", result);
    }

    @Test
    void find_nullKeyword_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> {
            taskList.find(null);
        });
    }

    @Test
    void mark_validTaskNumber_marksTaskAsDone() throws AlterEgoException {
        taskList.addToDo("Read book");

        String result = taskList.mark(1);

        assertTrue(result.contains("marked this task as done"));

        Task task = taskList.getTask(0);
        assertTrue(task.isDone());
    }

    @Test
    void mark_invalidTaskNumber_throwsException() throws AlterEgoException {
        taskList.addToDo("Read book");

        assertThrows(AlterEgoException.class, () -> {
            taskList.mark(2);
        });
    }

    @Test
    void mark_taskNumberZero_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> {
            taskList.mark(0);
        });
    }

    @Test
    void unmark_validTaskNumber_unmarksTask() throws AlterEgoException {
        taskList.addToDo("Read book");
        taskList.mark(1);

        String result = taskList.unmark(1);

        assertTrue(result.contains("marked this task as not done"));

        Task task = taskList.getTask(0);
        assertFalse(task.isDone());
    }

    @Test
    void unmark_invalidTaskNumber_throwsException() throws AlterEgoException {
        taskList.addToDo("Read book");

        assertThrows(AlterEgoException.class, () -> {
            taskList.unmark(2);
        });
    }

    @Test
    void delete_validTaskNumber_deletesTask() throws AlterEgoException {
        taskList.addToDo("Read book");
        taskList.addToDo("Write code");

        String result = taskList.delete(1);

        assertTrue(result.contains("removed this task"));
        assertTrue(result.contains("Read book"));
        assertEquals(1, taskList.getSize());

        String listResult = taskList.enumList();
        assertTrue(listResult.contains("Write code"));
        assertFalse(listResult.contains("Read book"));
    }

    @Test
    void delete_invalidTaskNumber_throwsException() throws AlterEgoException {
        taskList.addToDo("Read book");

        assertThrows(AlterEgoException.class, () -> {
            taskList.delete(2);
        });
    }

    @Test
    void assignTask_validInput_assignsContact() throws AlterEgoException {
        taskList.addToDo("Read book");
        Contact contact = new Contact("John", "friend");

        String result = taskList.assignTask(1, contact);

        assertTrue(result.contains("assigned to John"));

        Task task = taskList.getTask(0);
        assertEquals(contact, task.getAssignedTo());
    }

    @Test
    void assignTask_invalidTaskNumber_throwsException() {
        Contact contact = new Contact("John", "friend");

        assertThrows(AlterEgoException.class, () -> {
            taskList.assignTask(1, contact);
        });
    }

    @Test
    void assignTask_nullContact_assignsNull() throws AlterEgoException {
        taskList.addToDo("Read book");

        String result = taskList.assignTask(1, null);

        assertTrue(result.contains("assigned to null") || result.contains("null"));

        Task task = taskList.getTask(0);
        assertNull(task.getAssignedTo());
    }

    @Test
    void unassignTask_removesContactFromAllTasks() throws AlterEgoException {
        Contact john = new Contact("John", "friend");
        Contact mary = new Contact("Mary", "colleague");

        taskList.addToDo("Read book");
        taskList.addToDo("Write code");
        taskList.addToDo("Review PR");

        taskList.assignTask(1, john);
        taskList.assignTask(2, john);
        taskList.assignTask(3, mary);

        taskList.unassignTask(john);

        assertNull(taskList.getTask(0).getAssignedTo());
        assertNull(taskList.getTask(1).getAssignedTo());
        assertEquals(mary, taskList.getTask(2).getAssignedTo());
    }

    @Test
    void clear_emptiesTaskList() throws AlterEgoException {
        taskList.addToDo("Read book");
        taskList.addToDo("Write code");

        assertEquals(2, taskList.getSize());

        String result = taskList.clear();

        assertTrue(result.contains("Cleared"));
        assertEquals(0, taskList.getSize());
        assertEquals("No task. You're free to play. Yippie!", taskList.enumList());
    }

    @Test
    void getSize_returnsCorrectCount() throws AlterEgoException {
        assertEquals(0, taskList.getSize());

        taskList.addToDo("Read book");
        assertEquals(1, taskList.getSize());

        taskList.addToDo("Write code");
        assertEquals(2, taskList.getSize());

        taskList.delete(1);
        assertEquals(1, taskList.getSize());
    }

    @Test
    void getTask_validIndex_returnsTask() throws AlterEgoException {
        taskList.addToDo("Read book");

        Task task = taskList.getTask(0);

        assertTrue(task.toString().contains("Read book"));
    }

    @Test
    void getTask_invalidIndex_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.getTask(1);
        });
    }

    @Test
    void getTasks_returnsCopyNotOriginal() throws AlterEgoException {
        taskList.addToDo("Read book");

        ArrayList<Task> taskCopy = taskList.getTasks();
        taskCopy.clear(); // Modify the copy

        assertEquals(1, taskList.getSize()); // Original unchanged
    }

    @Test
    void handleDuplicate_preventsDuplicateTasks() throws AlterEgoException {
        taskList.addToDo("Read book");

        assertThrows(AlterEgoException.class, () -> {
            taskList.addToDo("Read book");
        });
    }

    @Test
    void integration_persistence_betweenSaves() throws AlterEgoException, IOException {
        // Add tasks
        taskList.addToDo("Read book");
        taskList.addDeadline("Return book", "01-12-2024");

        // Create new TaskList (simulating restart)
        TaskList newTaskList = new TaskList(taskStorage);

        assertEquals(2, newTaskList.getSize());

        String listResult = newTaskList.enumList();
        System.out.println(listResult);
        assertTrue(listResult.contains("Read book"));
        assertTrue(listResult.contains("Return book"));
    }

    @Test
    void integration_withContacts_savesCorrectly() throws AlterEgoException, IOException {
        Contact john = new Contact("John", "friend");

        taskList.addToDo("Read book");
        taskList.assignTask(1, john);

        // Create new TaskList (simulating restart)
        TaskList newTaskList = new TaskList(taskStorage);

        assertEquals(1, newTaskList.getSize());
        Task loadedTask = newTaskList.getTask(0);
        assertNotNull(loadedTask.getAssignedTo());
        assertEquals("John", loadedTask.getAssignedTo().getName());
    }
}
