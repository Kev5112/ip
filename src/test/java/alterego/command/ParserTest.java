package alterego.command;

import alterego.contact.ContactList;
import alterego.storage.ContactStorage;
import alterego.storage.TaskStorage;
import alterego.utils.AlterEgoException;
import alterego.task.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AI generated testcases, not for grading. Personal use only
 */
public class ParserTest {
    @TempDir
    Path tempDir;

    private Parser parser;
    private TaskList taskList;
    private TaskStorage taskStorage;
    private ContactList contactList;
    private ContactStorage contactStorage;

    @BeforeEach
    void setUp() {
        String testFilePathTasks = tempDir.resolve("tasks.txt").toString();
        String testFilePathContacts = tempDir.resolve("contacts.txt").toString();
        taskStorage = new TaskStorage(testFilePathTasks);
        contactStorage = new ContactStorage(testFilePathContacts);
        taskList = new TaskList(taskStorage);
        contactList = new ContactList(contactStorage, taskList);
        parser = new Parser(taskList, contactList);
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

        String result = parser.execute("delete t1");
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

    // ==================== CONTACT COMMAND TESTS ====================

    @Test
    void execute_contactCommand_validInput_addsContact() throws AlterEgoException {
        String result = parser.execute("contact John /as friend");
        assertTrue(result.contains("Added contact: John"));
        assertTrue(result.contains("friend"));

        String contactListResult = parser.execute("contactlist");
        assertTrue(contactListResult.contains("John"));
        assertTrue(contactListResult.contains("friend"));
    }

    @Test
    void execute_contactCommand_missingAs_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("contact John friend");
        });
    }

    @Test
    void execute_contactCommand_emptyName_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("contact  /as friend");
        });
    }

    @Test
    void execute_contactCommand_emptyRelationship_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("contact John /as ");
        });
    }

    @Test
    void execute_contactCommand_duplicateContact_throwsException() throws AlterEgoException {
        parser.execute("contact John /as friend");
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("contact John /as colleague");
        });
    }

    @Test
    void execute_contactlistCommand_emptyList_returnsNoContactsMessage() throws AlterEgoException {
        String result = parser.execute("contactlist");
        assertTrue(result.contains("no friends") || result.contains("no contacts"));
    }

    @Test
    void execute_contactlistCommand_withContacts_returnsContactList() throws AlterEgoException {
        parser.execute("contact John /as friend");
        parser.execute("contact Mary /as colleague");

        String result = parser.execute("contactlist");
        assertTrue(result.contains("John"));
        assertTrue(result.contains("friend"));
        assertTrue(result.contains("Mary"));
        assertTrue(result.contains("colleague"));
    }

    // ==================== ASSIGN COMMAND TESTS ====================

    @Test
    void execute_assignCommand_validInput_assignsTask() throws AlterEgoException {
        parser.execute("contact John /as friend");
        parser.execute("todo Read book");

        String result = parser.execute("assign 1 /to John");
        assertTrue(result.contains("assigned to John"));

        String listResult = parser.execute("list");
        assertTrue(listResult.contains("Read book"));
        assertTrue(listResult.contains("→ John"));
    }

    @Test
    void execute_assignCommand_missingTo_throwsException() {
        parser.execute("contact John /as friend");
        parser.execute("todo Read book");

        assertThrows(AlterEgoException.class, () -> {
            parser.execute("assign 1 John");
        });
    }

    @Test
    void execute_assignCommand_contactNotFound_throwsException() throws AlterEgoException {
        parser.execute("todo Read book");

        assertThrows(AlterEgoException.class, () -> {
            parser.execute("assign 1 /to Unknown");
        });
    }

    @Test
    void execute_assignCommand_invalidTaskNumber_throwsException() throws AlterEgoException {
        parser.execute("contact John /as friend");

        assertThrows(AlterEgoException.class, () -> {
            parser.execute("assign 99 /to John");
        });
    }

    @Test
    void execute_assignCommand_taskNumberWithExtraText_throwsException() throws AlterEgoException {
        parser.execute("contact John /as friend");
        parser.execute("todo Read book");

        assertThrows(AlterEgoException.class, () -> {
            parser.execute("assign 1abc /to John");
        });
    }

    // ==================== DELETE COMMAND WITH CONTACTS ====================

    @Test
    void execute_deleteCommand_deleteContact_unassignsFromTasks() throws AlterEgoException {
        parser.execute("contact John /as friend");
        parser.execute("todo Read book");
        parser.execute("assign 1 /to John");

        // Verify task is assigned
        String listBefore = parser.execute("list");
        assertTrue(listBefore.contains("→ John"));

        // Delete contact
        String deleteResult = parser.execute("delete c1");
        assertTrue(deleteResult.contains("removed this contact"));

        // Verify task is unassigned
        String listAfter = parser.execute("list");
        assertTrue(listAfter.contains("Read book"));
        assertFalse(listAfter.contains("→ John"));
    }

    @Test
    void execute_deleteCommand_deleteContact_updatesContactList() throws AlterEgoException {
        parser.execute("contact John /as friend");
        parser.execute("contact Mary /as colleague");

        assertEquals(2, contactList.getSize());

        parser.execute("delete c1");

        assertEquals(1, contactList.getSize());
        String contactListResult = parser.execute("contactlist");
        assertFalse(contactListResult.contains("John"));
        assertTrue(contactListResult.contains("Mary"));
    }

    @Test
    void execute_deleteCommand_invalidContactNumber_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("delete c99");
        });
    }

    @Test
    void execute_deleteCommand_missingPrefix_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            parser.execute("delete 1");
        });
    }


}
