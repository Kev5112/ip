package alterego.contact;

import alterego.storage.ContactStorage;
import alterego.storage.TaskStorage;
import alterego.task.TaskList;
import alterego.utils.AlterEgoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AI generated testcase, for personal use onlu
 */
public class ContactListTest {

    @TempDir
    Path tempDir;

    private ContactList contactList;
    private ContactStorage contactStorage;
    private TaskStorage taskStorage;
    private TaskList taskList;
    private String contactFilePath;
    private String taskFilePath;

    @BeforeEach
    void setUp() {
        contactFilePath = tempDir.resolve("contacts.txt").toString();
        taskFilePath = tempDir.resolve("tasks.txt").toString();
        contactStorage = new ContactStorage(contactFilePath);
        taskStorage = new TaskStorage(taskFilePath);
        taskList = new TaskList(taskStorage);
        contactList = new ContactList(contactStorage, taskList);
    }

    @Test
    void constructor_noExistingContacts_createsEmptyList() {
        assertNotNull(contactList);
        assertEquals(0, contactList.getSize());
        assertNotNull(contactList.getLoadStatus());
    }

    @Test
    void constructor_withExistingContacts_loadsContacts() throws IOException {
        // Create contacts file
        try (FileWriter fw = new FileWriter(contactFilePath)) {
            fw.write("John|friend\n");
            fw.write("Mary|colleague\n");
        }

        ContactList loadedList = new ContactList(contactStorage, taskList);

        assertEquals(2, loadedList.getSize());
        assertNotNull(loadedList.findContact("John"));
        assertNotNull(loadedList.findContact("Mary"));
    }

    @Test
    void addContact_validInput_addsContact() throws AlterEgoException {
        String result = contactList.addContact("John", "friend");

        assertTrue(result.contains("added this contact"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("friend"));
        assertEquals(1, contactList.getSize());
    }

    @Test
    void addContact_duplicateName_throwsException() throws AlterEgoException {
        contactList.addContact("John", "friend");

        assertThrows(AlterEgoException.class, () -> {
            contactList.addContact("John", "colleague");
        });
    }

    @Test
    void addContact_persistence_savesToFile() throws AlterEgoException, IOException {
        contactList.addContact("John", "friend");

        // Create new ContactList to load from file
        ContactList newList = new ContactList(contactStorage, taskList);

        assertEquals(1, newList.getSize());
        assertNotNull(newList.findContact("John"));
    }

    @Test
    void delete_validIndex_deletesContact() throws AlterEgoException {
        contactList.addContact("John", "friend");
        contactList.addContact("Mary", "colleague");

        assertEquals(2, contactList.getSize());

        String result = contactList.delete(1);

        assertTrue(result.contains("removed this contact"));
        assertTrue(result.contains("John"));
        assertEquals(1, contactList.getSize());
        assertNull(contactList.findContact("John"));
        assertNotNull(contactList.findContact("Mary"));
    }

    @Test
    void delete_invalidIndex_throwsException() {
        assertThrows(AlterEgoException.class, () -> {
            contactList.delete(1);
        });
    }

    @Test
    void delete_persistence_updatesFile() throws AlterEgoException, IOException {
        contactList.addContact("John", "friend");
        contactList.addContact("Mary", "colleague");

        contactList.delete(1);

        // Create new ContactList to load from file
        ContactList newList = new ContactList(contactStorage, taskList);

        assertEquals(1, newList.getSize());
        assertNull(newList.findContact("John"));
        assertNotNull(newList.findContact("Mary"));
    }

    @Test
    void enumContact_emptyList_returnsNoContactsMessage() {
        String result = contactList.enumContact();

        assertTrue(result.contains("no friends") || result.contains("no contacts"));
    }

    @Test
    void enumContact_withContacts_returnsNumberedList() throws AlterEgoException {
        contactList.addContact("John", "friend");
        contactList.addContact("Mary", "colleague");

        String result = contactList.enumContact();

        assertTrue(result.contains("1.John (friend)"));
        assertTrue(result.contains("2.Mary (colleague)"));
    }

    @Test
    void findContact_exactMatch_returnsContact() throws AlterEgoException {
        contactList.addContact("John", "friend");
        contactList.addContact("Jonathan", "colleague");

        Contact found = contactList.findContact("John");

        assertNotNull(found);
        assertEquals("John", found.getName());
        assertEquals("friend", found.getRelationship());
    }

    @Test
    void findContact_partialMatch_returnsFirstMatch() throws AlterEgoException {
        contactList.addContact("Jonathan", "colleague");
        contactList.addContact("John", "friend");

        Contact found = contactList.findContact("John");

        assertNotNull(found);
        // Could be either, depending on implementation
        assertTrue(found.getName().contains("John"));
    }

    @Test
    void findContact_caseInsensitive_returnsContact() throws AlterEgoException {
        contactList.addContact("John", "friend");

        Contact found = contactList.findContact("JOHN");

        assertNotNull(found);
        assertEquals("John", found.getName());
    }

    @Test
    void findContact_notFound_returnsNull() throws AlterEgoException {
        contactList.addContact("John", "friend");

        Contact found = contactList.findContact("Mary");

        assertNull(found);
    }

    @Test
    void findContact_nullInput_returnsNull() {
        assertNull(contactList.findContact(null));
    }

    @Test
    void findContact_emptyInput_returnsNull() {
        assertNull(contactList.findContact(""));
    }

    @Test
    void setBestFriend_contactNotInList_throwsException() {
        Contact john = new Contact("John", "bestfriend");

        assertThrows(AlterEgoException.class, () -> {
            contactList.setBestFriend(john);
        });
    }

    @Test
    void _returnsCorrectCount() throws AlterEgoException {
        assertEquals(0, contactList.getSize());

        contactList.addContact("John", "friend");
        assertEquals(1, contactList.getSize());

        contactList.addContact("Mary", "colleague");
        assertEquals(2, contactList.getSize());

        contactList.delete(1);
        assertEquals(1, contactList.getSize());
    }

    @Test
    void integration_taskAssignment_contactExtracted() throws AlterEgoException, IOException {
        // Create tasks with contacts
        Contact john = new Contact("John", "friend");
        Contact mary = new Contact("Mary", "colleague");

        taskList.addToDo("Read book");
        taskList.addToDo("Write code");
        contactList.addContact(john);
        contactList.addContact(mary);
        taskList.assignTask(1, john);
        taskList.assignTask(2, mary);

        // Create ContactList - should extract contacts from tasks
        ContactList newContactList = new ContactList(contactStorage, taskList);

        assertEquals(2, newContactList.getSize());
        assertNotNull(newContactList.findContact("John"));
        assertNotNull(newContactList.findContact("Mary"));
    }

    @Test
    void integration_deleteContact_unassignsFromTasks() throws AlterEgoException, IOException {
        // Add contacts
        contactList.addContact("John", "friend");
        contactList.addContact("Mary", "colleague");

        // Find contacts
        Contact john = contactList.findContact("John");
        Contact mary = contactList.findContact("Mary");

        // Create tasks
        taskList.addToDo("Read book");
        taskList.addToDo("Write code");
        taskList.addToDo("Review");

        // Assign contacts to tasks (using 1-based indexing)
        taskList.assignTask(1, john);
        taskList.assignTask(2, john);
        taskList.assignTask(3, mary);

        // Verify assignments before deletion
        assertEquals(john, taskList.getTask(0).getAssignedTo());
        assertEquals(john, taskList.getTask(1).getAssignedTo());
        assertEquals(mary, taskList.getTask(2).getAssignedTo());

        // Delete John (first contact)
        contactList.delete(1);

        // Verify tasks previously assigned to John are now unassigned
        assertNull(taskList.getTask(0).getAssignedTo());
        assertNull(taskList.getTask(1).getAssignedTo());

        // Verify Mary's task still assigned
        assertNotNull(taskList.getTask(2).getAssignedTo());
        assertEquals("Mary", taskList.getTask(2).getAssignedTo().getName());
    }
}