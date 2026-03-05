package alterego.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import alterego.contact.Contact;


/**
 * The testcases below are AI generated, with personal
 * modifications according to the intended behavior of ContactStorage
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


}
