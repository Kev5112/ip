package alterego.storage;

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
     * Parses a single line from the storage file into a storable object.
     * @param line The line read from the file
     * @return The parsed contact object
     * @throws AlterEgoException if the line format is invalid or cannot be parsed
     */
    @Override
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
