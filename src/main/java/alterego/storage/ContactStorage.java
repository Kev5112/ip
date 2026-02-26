package alterego.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import alterego.contact.Contact;
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
     * Clears all contacts from storage file.
     * @throws IOException if file write operation fails
     */
    public void clear() throws IOException {
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
        FileWriter fw = new FileWriter(filePath);
        for (Contact contact : contacts) {
            fw.write(contact.getName() + "|" + contact.getRelationship() + System.lineSeparator());
        }
        fw.close();

        File f = new File(filePath);
    }

    /**
     * Loads contacts from storage file.
     * Expects each line in format: name|relationship
     * @return List of loaded contacts
     * @throws FileNotFoundException if storage file does not exist
     * @throws AlterEgoException if file format is invalid
     */
    public ArrayList<Contact> loadContacts() throws FileNotFoundException, AlterEgoException {
        ArrayList<Contact> contacts = new ArrayList<>();
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String[] parts = s.nextLine().split("\\|");
            if (parts.length != 2) {
                throw new AlterEgoException("Problem with file. "
                        + "Please edit manually or perform 'clear'");
            }
            contacts.add(new Contact(parts[0].trim(), parts[1].trim()));
        }
        return contacts;
    }
}