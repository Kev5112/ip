package alterego.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import alterego.contact.Contact;
import alterego.contact.ContactList;
import alterego.utils.AlterEgoException;

/**
 * Handles file storage operations for contacts.
 */
public class ContactStorage {
    private final String filePath;

    /**
     * Sets a file corresponding to the path as the storage.
     * @param path File path for storing contacts
     */
    public ContactStorage(String path) {
        this.filePath = path;
    }

    /**
     * Ensures the parent directory exists before file operations.
     * Creates directories if they don't exist.
     * AI generated last minute bug fixes.
     * @throws IOException if directories cannot be created
     */
    private void ensureDirectoryExists() throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir.getPath());
            }
        }
    }

    /**
     * Clears all contacts from storage file.
     * @throws IOException if file write operation fails
     */
    public void clear() throws IOException {
        ensureDirectoryExists();
        FileWriter fw = new FileWriter(filePath);
        fw.write("");
        fw.close();
    }

    /**
     * Rewrites storage file with current contact list.
     * Format: name|relationship
     * @param contacts List of contacts to save
     * @throws IOException if file write operation fails
     */
    public void rewriteFile(ArrayList<Contact> contacts) throws IOException {
        ensureDirectoryExists();
        FileWriter fw = new FileWriter(filePath);
        for (Contact contact : contacts) {
            fw.write(contact.getName() + "|" + contact.getRelationship() + System.lineSeparator());
        }
        fw.close();
    }

    /**
     * Loads contacts from storage file.
     * Expects each line in format: name|relationship
     * @throws FileNotFoundException if storage file does not exist
     * @throws AlterEgoException if file format is invalid
     */
    public void loadContacts(ContactList contacts) throws FileNotFoundException, AlterEgoException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        int lineCount = 0;
        while (s.hasNextLine()) {
            lineCount += 1;
            String line = s.nextLine().trim();
            Contact newContact = null;
            try {
                newContact = parseFromFile(line);
            } catch (AlterEgoException e) {
                contacts.addLoadStatus("Contact storage problem: Problem with line "
                        + lineCount + ". " + e.getMessage() + "\n");
            }
            if (newContact != null) {
                contacts.loadContact(newContact);
            }
        }
    }

    private Contact parseFromFile(String line) throws AlterEgoException {
        String[] parts = line.split("\\|");
        if (parts.length != 2) {
            throw new AlterEgoException("Please edit manually or perform 'clear'.");
        }
        String name = parts[0].trim();
        String relationship = parts[1].trim();
        return new Contact(name, relationship);
    }
}
