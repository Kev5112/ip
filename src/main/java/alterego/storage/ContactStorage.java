package alterego.storage;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import alterego.contact.Contact;
import alterego.utils.AlterEgoException;

/**
 * Handles file storage operations for contacts.
 */
public class ContactStorage extends Storage<Contact> {

    /**
     * Sets a file corresponding to the path as the storage.
     * @param path File path for storing contacts
     */
    public ContactStorage(String path) {
        super(path);
    }

    /**
     * Clears all contacts from storage file.
     * @throws IOException if file write operation fails
     */
    public void clear() throws IOException {
        ensureDirectoryExists();
        FileWriter fw = createFileWriter();
        fw.write("");
        fw.close();
    }

    public Contact parseFromFile(String line) throws AlterEgoException {
        String[] parts = line.split("\\|");
        if (parts.length != 2) {
            throw new AlterEgoException("Please edit manually or perform 'clear'.");
        }
        String name = parts[0].trim();
        String relationship = parts[1].trim();
        return new Contact(name, relationship);
    }
}
