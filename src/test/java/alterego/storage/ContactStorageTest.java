package alterego.storage;

import alterego.contact.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AI generated testcases, not for grading. Personal use only
 */
public class ContactStorageTest {

    @TempDir
    Path tempDir;

    private ContactStorage contactStorage;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        testFilePath = tempDir.resolve("contacts.txt").toString();
        contactStorage = new ContactStorage(testFilePath);
    }

    @Test
    void constructor_validPath_createsInstance() {
        assertNotNull(contactStorage);
    }

    @Test
    void constructor_nullPath_usesNullPath() {
        // Should not throw exception - path can be null?
        // If your constructor allows null, test behavior
        ContactStorage storage = new ContactStorage(null);
        assertNotNull(storage);
    }

    @Test
    void rewriteFile_emptyContactsList_createsEmptyFile() throws IOException {
        ArrayList<Contact> emptyList = new ArrayList<>();
        contactStorage.rewriteFile(emptyList);

        File file = new File(testFilePath);
        assertTrue(file.exists());
        assertEquals(0, file.length());
    }

    @Test
    void rewriteFile_singleContact_writesCorrectFormat() throws IOException {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John", "friend"));

        contactStorage.rewriteFile(contacts);

        // Read file directly to verify format
        File file = new File(testFilePath);
        try (java.util.Scanner scanner = new java.util.Scanner(file)) {
            assertTrue(scanner.hasNextLine());
            String line = scanner.nextLine();
            assertEquals("John|friend", line);
            assertFalse(scanner.hasNextLine());
        }
    }

    @Test
    void rewriteFile_multipleContacts_writesAllContacts() throws IOException {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John", "friend"));
        contacts.add(new Contact("Mary", "colleague"));
        contacts.add(new Contact("Bob", "bestfriend"));

        contactStorage.rewriteFile(contacts);

        // Read file and verify all contacts
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(testFilePath);
        try (java.util.Scanner scanner = new java.util.Scanner(file)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }

        assertEquals(3, lines.size());
        assertTrue(lines.contains("John|friend"));
        assertTrue(lines.contains("Mary|colleague"));
        assertTrue(lines.contains("Bob|bestfriend"));
    }

    @Test
    void rewriteFile_contactWithSpecialCharacters_handlesCorrectly() throws IOException {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John Doe", "best friend"));
        contacts.add(new Contact("Mary-Jane", "co-worker"));

        contactStorage.rewriteFile(contacts);

        // Read and verify
        ArrayList<Contact> loaded = contactStorage.loadContacts();
        assertEquals(2, loaded.size());
        assertEquals("John Doe", loaded.get(0).getName());
        assertEquals("best friend", loaded.get(0).getRelationship());
        assertEquals("Mary-Jane", loaded.get(1).getName());
        assertEquals("co-worker", loaded.get(1).getRelationship());
    }

    @Test
    void loadContacts_fileExistsWithValidContacts_returnsContacts() throws IOException {
        // Create test file manually
        try (FileWriter fw = new FileWriter(testFilePath)) {
            fw.write("John|friend\n");
            fw.write("Mary|colleague\n");
            fw.write("Bob|bestfriend\n");
        }

        ArrayList<Contact> loaded = contactStorage.loadContacts();

        assertEquals(3, loaded.size());

        // Verify first contact
        Contact john = loaded.get(0);
        assertEquals("John", john.getName());
        assertEquals("friend", john.getRelationship());

        // Verify second contact
        Contact mary = loaded.get(1);
        assertEquals("Mary", mary.getName());
        assertEquals("colleague", mary.getRelationship());

        // Verify third contact
        Contact bob = loaded.get(2);
        assertEquals("Bob", bob.getName());
        assertEquals("bestfriend", bob.getRelationship());
    }

    @Test
    void loadContacts_fileExistsWithEmptyLines_skipsEmptyLines() throws IOException {
        try (FileWriter fw = new FileWriter(testFilePath)) {
            fw.write("John|friend\n");
            fw.write("\n");
            fw.write("Mary|colleague\n");
            fw.write("   \n");
            fw.write("Bob|bestfriend\n");
        }

        ArrayList<Contact> loaded = contactStorage.loadContacts();

        assertEquals(3, loaded.size());
    }

    @Test
    void loadContacts_fileExistsWithExtraSpaces_trimsCorrectly() throws IOException {
        try (FileWriter fw = new FileWriter(testFilePath)) {
            fw.write("  John  |  friend  \n");
            fw.write("Mary|colleague\n");
        }

        ArrayList<Contact> loaded = contactStorage.loadContacts();

        assertEquals(2, loaded.size());
        assertEquals("John", loaded.get(0).getName());
        assertEquals("friend", loaded.get(0).getRelationship());
    }

    @Test
    void loadContacts_fileExistsWithMalformedLines_skipsMalformedLines() throws IOException {
        try (FileWriter fw = new FileWriter(testFilePath)) {
            fw.write("John|friend\n");
            fw.write("MalformedLine\n");
            fw.write("Mary|colleague|extra\n");
            fw.write("Bob|bestfriend\n");
        }

        ArrayList<Contact> loaded = contactStorage.loadContacts();

        // Should only load valid lines (2 parts)
        assertEquals(2, loaded.size());
    }

    @Test
    void loadContacts_fileDoesNotExist_throwsFileNotFoundException() {
        // File doesn't exist in temp dir
        assertThrows(FileNotFoundException.class, () -> {
            contactStorage.loadContacts();
        });
    }

    @Test
    void loadContacts_emptyFile_returnsEmptyList() throws IOException {
        // Create empty file
        new File(testFilePath).createNewFile();

        ArrayList<Contact> loaded = contactStorage.loadContacts();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty());
    }

    @Test
    void rewriteFile_overwritesExistingContent() throws IOException {
        // First write some contacts
        ArrayList<Contact> firstBatch = new ArrayList<>();
        firstBatch.add(new Contact("John", "friend"));
        contactStorage.rewriteFile(firstBatch);

        // Then overwrite with different contacts
        ArrayList<Contact> secondBatch = new ArrayList<>();
        secondBatch.add(new Contact("Mary", "colleague"));
        secondBatch.add(new Contact("Bob", "bestfriend"));
        contactStorage.rewriteFile(secondBatch);

        // Load and verify only second batch exists
        ArrayList<Contact> loaded = contactStorage.loadContacts();
        assertEquals(2, loaded.size());
        assertEquals("Mary", loaded.get(0).getName());
        assertEquals("Bob", loaded.get(1).getName());
    }

    @Test
    void rewriteFile_createsNewFileIfNotExists() throws IOException {
        File file = new File(testFilePath);
        assertFalse(file.exists());

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John", "friend"));
        contactStorage.rewriteFile(contacts);

        assertTrue(file.exists());
    }

    @Test
    void integration_writeThenLoad_returnsSameContacts() throws IOException {
        ArrayList<Contact> original = new ArrayList<>();
        original.add(new Contact("John", "friend"));
        original.add(new Contact("Mary", "colleague"));
        original.add(new Contact("Bob", "bestfriend"));

        // Write
        contactStorage.rewriteFile(original);

        // Load
        ArrayList<Contact> loaded = contactStorage.loadContacts();

        // Compare
        assertEquals(original.size(), loaded.size());
        for (int i = 0; i < original.size(); i++) {
            assertEquals(original.get(i).getName(), loaded.get(i).getName());
            assertEquals(original.get(i).getRelationship(), loaded.get(i).getRelationship());
        }
    }

    @Test
    void loadContacts_veryLargeFile_handlesCorrectly() throws IOException {
        // Create a file with many contacts
        try (FileWriter fw = new FileWriter(testFilePath)) {
            for (int i = 0; i < 1000; i++) {
                fw.write("Person" + i + "|relationship" + i + "\n");
            }
        }

        ArrayList<Contact> loaded = contactStorage.loadContacts();

        assertEquals(1000, loaded.size());
        assertEquals("Person500", loaded.get(500).getName());
        assertEquals("relationship500", loaded.get(500).getRelationship());
    }
}