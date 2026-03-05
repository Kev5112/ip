package alterego;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The testcases below are created with the help of AI for syntax,
 * and personally modified to align with the code logic
 */
public class AlterEgoTest {
    @TempDir
    Path tempDir;

    private AlterEgo alterEgo;
    private String taskFilePath;
    private String contactFilePath;

    @BeforeEach
    void setUp() {
        taskFilePath = tempDir.resolve("tasks.txt").toString();
        contactFilePath = tempDir.resolve("contacts.txt").toString();
    }

    private void createValidTaskFile() throws IOException {
        FileWriter fw = new FileWriter(taskFilePath);
        fw.write("T | 0 | read book\n");
        fw.write("D | 1 | return book | Apr 01 2026\n");
        fw.write("E | 0 | conference | Apr 01 2026 -> Apr 03 2026\n");
        fw.close();
    }

    private void createValidContactFile() throws IOException {
        FileWriter fw = new FileWriter(contactFilePath);
        fw.write("john|bro\n");
        fw.write("ethan|friend\n");
        fw.write("jean|friend\n");
        fw.close();
    }

    private void createInvalidTaskFile() throws IOException {
        FileWriter fw = new FileWriter(taskFilePath);
        fw.write("T | 0 | read book\n");
        fw.write("D | 1 | return book | Apr 01 2026\n");
        fw.write("INVALID\n"); // Invalid format
        fw.write("E | 0 | conference | Apr 01 2026 -> Apr 03 2026\n");
        fw.write("T | 2 | invalid done status\n"); // Invalid done status (not 0 or 1)
        fw.write("D | 1 | missing deadline | \n"); // Missing date
        fw.close();
    }

    @Test
    void getLoadStatusTest_getResponseTest_validData() throws IOException {
        // Setup: Create valid task file
        createValidTaskFile();
        createValidContactFile();

        // Initialize AlterEgo to load the valid data
        alterEgo = new AlterEgo(taskFilePath, contactFilePath);

        // Check load status (should be null for valid data)
        assertNull(alterEgo.getLoadStatus(), "Load status should be null for valid data");

        // Verify initial list contains 3 tasks
        String listResponse = alterEgo.getResponse("list");
        assertTrue(listResponse.contains("1.[T][ ] read book"), "Should contain Read book");
        assertTrue(listResponse.contains("2.[D][X] return book"), "Should contain Return book");
        assertTrue(listResponse.contains("3.[E][ ] conference"), "Should contain Conference");

        // Add a new todo
        String addResponse = alterEgo.getResponse("todo write tests");
        assertTrue(addResponse.contains("Got it"), "Should confirm addition");
        assertTrue(addResponse.contains("write tests"), "Should contain new task");

        // Verify list now has 4 tasks
        listResponse = alterEgo.getResponse("list");
        assertTrue(listResponse.contains("1.[T][ ] read book"), "Should contain Read book");
        assertTrue(listResponse.contains("2.[D][X] return book"), "Should contain Return book");
        assertTrue(listResponse.contains("3.[E][ ] conference"), "Should contain Conference");
        assertTrue(listResponse.contains("4.[T][ ] write tests"), "Should contain new task");

        // Delete the second task (Return book)
        String deleteResponse = alterEgo.getResponse("delete t2");
        assertTrue(deleteResponse.contains("removed this task"), "Should confirm deletion");
        assertTrue(deleteResponse.contains("return book"), "Should mention deleted task");

        // Verify list now has 3 tasks (and order shifted)
        listResponse = alterEgo.getResponse("list");
        assertTrue(listResponse.contains("1.[T][ ] read book"), "Read book should still be first");
        assertTrue(listResponse.contains("2.[E][ ] conference"), "Conference should now be second");
        assertTrue(listResponse.contains("3.[T][ ] write tests"), "Write tests should be third");
        assertFalse(listResponse.contains("Return book"), "Return book should be gone");

        // Test bye command
        String byeResponse = alterEgo.getResponse("bye");
        assertEquals("Bye. Don't forget to hydrate! (and touch grass)", byeResponse,
                "Bye message should match");
    }

    @Test
    void getLoadStatusTest_getResponseTest_invalidData() throws IOException {
        // Setup: Create invalid task file but valid contact file
        createInvalidTaskFile();
        createValidContactFile();

        // Initialize AlterEgo to load the data
        alterEgo = new AlterEgo(taskFilePath, contactFilePath);

        // Check load status contains warning (load status should contain problematic line numbers)
        String loadStatus = alterEgo.getLoadStatus();
        assertNotNull(loadStatus, "Load status should not be null for invalid data");
        assertTrue(loadStatus.contains("line 3") && loadStatus.contains("line 5")
                && loadStatus.contains("line 6"),
                "Load status should contain warning message");

        // Verify list contains only valid tasks (ignores corrupted lines)
        String listResponse = alterEgo.getResponse("list");

        // Should contain valid tasks
        assertTrue(listResponse.contains("1.[T][ ] read book"), "Should contain valid Read book");
        assertTrue(listResponse.contains("2.[D][X] return book"), "Should contain valid Return book");
        assertTrue(listResponse.contains("3.[E][ ] conference"), "Should contain valid Conference");

        // Should NOT contain invalid tasks (count should be 3, not more)
        assertFalse(listResponse.contains("invalid done status"),
                "Should not contain task with invalid done status");
        assertFalse(listResponse.contains("missing deadline"),
                "Should not contain incomplete deadline");

        // Verify the list size is correct (only 3 valid tasks)
        String[] lines = listResponse.split("\n");
        int taskCount = 0;
        for (String line : lines) {
            if (!line.isBlank()) {
                taskCount++;
            }
        }
        assertEquals(3, taskCount, "Should have exactly 3 tasks (invalid ones ignored)");

        // Verify the list still works correctly with operations
        String addResponse = alterEgo.getResponse("todo new valid task");
        assertTrue(addResponse.contains("Got it"), "Should be able to add new task");

        listResponse = alterEgo.getResponse("list");
        assertTrue(listResponse.contains("4.[T][ ] new valid task"),
                "New task should be added after valid tasks");

        // Mark first task as done
        String markResponse = alterEgo.getResponse("mark 1");
        assertTrue(markResponse.contains("marked this task as done"),
                "Should be able to mark task as done");

        // Verify task was marked
        listResponse = alterEgo.getResponse("list");
        assertTrue(listResponse.contains("1.[T][X] read book"),
                "Read book should be marked as done");

        // Test find command still works
        String findResponse = alterEgo.getResponse("find conference");
        assertTrue(findResponse.contains("conference"), "Find should still work");

        // Test assign command works
        String assignResponse = alterEgo.getResponse("assign 4 /to john");
        assertTrue(assignResponse.contains("assigned to john"),
                "Should be able to assign task to contact");
        listResponse = alterEgo.getResponse("list");
        assertTrue(listResponse.contains("new valid task [→ john]"),
                "Task should show assignment to john");

        // Test bye
        String byeResponse = alterEgo.getResponse("bye");
        assertEquals("Bye. Don't forget to hydrate! (and touch grass)", byeResponse,
                "Bye message should match");
    }

    @Test
    void getLoadStatusTest_getResponseTest_validContacts() throws IOException {
        // Setup: Create valid task and contact files
        createValidTaskFile();
        createValidContactFile();

        // Initialize AlterEgo to load the valid data
        alterEgo = new AlterEgo(taskFilePath, contactFilePath);

        // Check load status (should be null for valid data)
        assertNull(alterEgo.getLoadStatus(), "Load status should be null for valid data");

        // Verify initial contact list contains 3 contacts
        String contactListResponse = alterEgo.getResponse("contactlist");
        assertTrue(contactListResponse.contains("1.john (bro)"), "Should contain john");
        assertTrue(contactListResponse.contains("2.ethan (friend)"), "Should contain ethan");
        assertTrue(contactListResponse.contains("3.jean (friend)"), "Should contain jean");

        // Add a new contact
        String addContactResponse = alterEgo.getResponse("contact michael /as colleague");
        assertTrue(addContactResponse.contains("Got it"), "Should confirm addition");
        assertTrue(addContactResponse.contains("michael"), "Should contain new contact");
        assertTrue(addContactResponse.contains("colleague"), "Should contain relationship");

        // Verify contact list now has 4 contacts
        contactListResponse = alterEgo.getResponse("contactlist");
        assertTrue(contactListResponse.contains("1.john (bro)"), "Should contain john");
        assertTrue(contactListResponse.contains("2.ethan (friend)"), "Should contain ethan");
        assertTrue(contactListResponse.contains("3.jean (friend)"), "Should contain jean");
        assertTrue(contactListResponse.contains("4.michael (colleague)"), "Should contain new contact");

        // Add tasks and assign to contacts
        String todoResponse = alterEgo.getResponse("todo review code");
        assertTrue(todoResponse.contains("Got it"), "Should add task");

        // Assign task to john
        String assignResponse = alterEgo.getResponse("assign 4 /to john");
        assertTrue(assignResponse.contains("assigned to john"), "Should assign task to john");

        // Verify task shows assignment
        String listResponse = alterEgo.getResponse("list");
        assertTrue(listResponse.contains("review code [→ john]"), "Task should show assignment");

        // Test deleting a contact
        String deleteContactResponse = alterEgo.getResponse("delete c2"); // Delete ethan
        assertTrue(deleteContactResponse.contains("removed this contact"),
                "Should confirm contact deletion");
        assertTrue(deleteContactResponse.contains("ethan"), "Should mention deleted contact");

        // Verify contact list updated (should be 3 contacts now)
        contactListResponse = alterEgo.getResponse("contactlist");
        assertTrue(contactListResponse.contains("1.john (bro)"), "john should still be first");
        assertTrue(contactListResponse.contains("2.jean (friend)"), "jean should now be second");
        assertTrue(contactListResponse.contains("3.michael (colleague)"), "michael should be third");
        assertFalse(contactListResponse.contains("ethan"), "ethan should be gone");

        // Test bye
        String byeResponse = alterEgo.getResponse("bye");
        assertEquals("Bye. Don't forget to hydrate! (and touch grass)", byeResponse,
                "Bye message should match");
    }
}
