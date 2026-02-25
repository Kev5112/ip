package alterego.command;

import alterego.utils.AlterEgoException;
import alterego.storage.Storage;
import alterego.task.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @TempDir
    Path tempDir;

    private Parser parser;
    private TaskList taskList;
    private Storage storage;

    @BeforeEach
    void setUp() {
        String testFilePath = tempDir.resolve("tasks.txt").toString();
        storage = new Storage(testFilePath);
        taskList = new TaskList(storage);
        parser = new Parser(taskList);
    }

    @Test
    void execute_blankInput_returnsEmptyString() throws AlterEgoException {
        assertEquals("", parser.execute("   "));
        assertEquals("", parser.execute(""));
    }

    @Test
    void execute_listCommand_returnsTaskMessage() throws AlterEgoException {
        String result = parser.execute("list");
        assertTrue(result.contains("No task"));
        result = parser.execute("todo Read book");
        result += parser.execute("todo Buy book");
        result += parser.execute("todo Write code");
        assertTrue(result.contains("Read book"));
        assertTrue(result.contains("Buy book"));
        assertTrue(result.contains("Write code"));
    }

    @Test
    void execute_todoCommand_validInput_addsTodo() throws AlterEgoException {
        String result = parser.execute("todo Read book");
        assertTrue(result.contains("Read book"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("Read book"));
    }

    @Test
    void execute_todoCommand_missingDescription_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("todo");
        });
    }

    @Test
    void execute_deadlineCommand_validInput_addsDeadline() throws AlterEgoException {
        String result = parser.execute("deadline Return book /by 01-12-2024");
        assertTrue(result.contains("Return book"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("Return book"));
        assertTrue(listResult.contains("by:"));
    }

    @Test
    void execute_deadlineCommand_missingBy_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("deadline Return book");
        });
    }

    @Test
    void execute_eventCommand_validInput_addsEvent() throws AlterEgoException {
        String result = parser.execute("event Conference /from 01-12-2024 /to 03-12-2024");
        assertTrue(result.contains("Conference"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("Conference"));
        assertTrue(listResult.contains("from:"));
        assertTrue(listResult.contains("to:"));
    }

    @Test
    void execute_eventCommand_validInput_overlappingEvent() throws AlterEgoException {
        parser.execute("event Conference /from 01-12-2024 /to 03-12-2024");
        String result = parser.execute("event Conference /from 01-12-2025 /to 03-12-2025");
        assertFalse(result.contains("Overlapping"));
        result = parser.execute("event Conference /from 01-12-2024 /to 05-12-2024");
        assertTrue(result.contains("Overlapping"));
    }

    @Test
    void execute_eventCommand_missingFrom_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("event Conference /to 2024-12-03");
        });
    }

    @Test
    void execute_markCommand_validInput_marksTask() throws AlterEgoException {
        parser.execute("todo Read book");
        String result = parser.execute("mark 1");
        assertTrue(result.contains("marked this task as done"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("[X]"));
    }

    @Test
    void execute_markCommand_invalidNumber_throwsException() {
        parser.execute("todo Read book");
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("mark 2");
        });
    }

    @Test
    void execute_unmarkCommand_validInput_unmarksTask() throws AlterEgoException {
        parser.execute("todo Read book");
        parser.execute("mark 1");
        String result = parser.execute("unmark 1");
        assertTrue(result.contains("marked this task as not done"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("[ ]"));
    }

    @Test
    void execute_deleteCommand_validInput_deletesTask() throws AlterEgoException {
        parser.execute("todo Read book");
        parser.execute("todo Write code");

        String result = parser.execute("delete 1");
        assertTrue(result.contains("removed this task"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("Write code"));
        assertFalse(listResult.contains("Read book"));
    }

    @Test
    void execute_findCommand_matchingKeyword_returnsMatches() throws AlterEgoException {
        parser.execute("todo Read book");
        parser.execute("todo Buy book");
        parser.execute("todo Write code");

        String result = parser.execute("find book");
        assertTrue(result.contains("Read book"));
        assertTrue(result.contains("Buy book"));
        assertFalse(result.contains("Write code"));
    }

    @Test
    void execute_findCommand_noMatches_returnsNoResultMessage() throws AlterEgoException {
        parser.execute("todo Read book");
        String result = parser.execute("find xyz");
        assertEquals("No search result found.", result);
    }

    @Test
    void execute_unknownCommand_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("unknown command");
        });
    }

    @Test
    void execute_clearCommand_clearsAllTasks() throws AlterEgoException {
        parser.execute("todo Read book");
        parser.execute("todo Write code");

        String result = parser.execute("clear");
        assertTrue(result.contains("Cleared"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("No task"));
    }
}
