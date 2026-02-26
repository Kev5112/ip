package alterego.contact;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import alterego.storage.ContactStorage;
import alterego.task.TaskList;
import alterego.utils.AlterEgoException;
import alterego.utils.ExceptionCatcher;

/**
 * Manages contact operations
 */
public class ContactList {
    private String loadStatus = null;
    private Set<Contact> contactSet;
    private ArrayList<Contact> contacts;
    private TaskList tasks;
    private ContactStorage contactStorage;

    /**
     * Creates a ContactList with the given storage handler and task list.
     * Loads existing contacts from storage or creates a new list if file not found.
     * @param contactStorage Storage handler for contacts
     * @param taskList Task list for managing contact assignments
     */
    public ContactList(ContactStorage contactStorage, TaskList taskList) {
        this.contactStorage = contactStorage;
        try {
            this.contacts = contactStorage.loadContacts();
            this.contactSet = new HashSet<>(contacts);
            this.tasks = taskList;
            assert this.contacts != null : "loadTasks() method should not return null";
        } catch (FileNotFoundException e) {
            this.contacts = new ArrayList<>();
            this.contactSet = new HashSet<>();
            this.tasks = taskList;
            loadStatus = "Warning: Contact data not found. Creating a new list.";
        }
    }

    /**
     * Returns the load status message from initialization.
     * @return Status message if file was not found, null otherwise
     */
    public String getLoadStatus() {
        return loadStatus;
    }

    /**
     * Clears all contacts from list and storage.
     */
    public void clear() {
        contacts = new ArrayList<>();
        contactSet = new HashSet<>();
        ExceptionCatcher.catchIoException(contactStorage::clear, "");
    }

    /**
     * Creates and adds a new contact with the given name and relationship.
     * @param personName Name of the contact
     * @param relationship Relationship to the contact
     * @return Confirmation message
     */
    public String addContact(String personName, String relationship) {
        Contact contact = new Contact(personName, relationship);
        return addContact(contact);
    }

    /**
     * Adds an existing contact object to the list.
     * @param contact Contact to add
     * @return Confirmation message
     */
    public String addContact(Contact contact) {
        handleDuplicate(contact);
        contacts.add(contact);
        String successMessage = "Got it. I've added this contact:\n " + contact
                + "\nNow you have " + contacts.size() + " contacts in the list.\n";
        return ExceptionCatcher.catchIoException(() -> contactStorage.rewriteFile(contacts), successMessage);
    }

    /**
     * Deletes a contact by index and updates storage.
     * Also removes contact from any task assignments.
     * @param contactNumber 1-based contact index
     * @return Confirmation message with updated contact count
     * @throws AlterEgoException if contact number is invalid
     */
    public String delete(int contactNumber) throws AlterEgoException {
        if (contactNumber > contacts.size()) {
            throw new AlterEgoException("There's only " + contacts.size() + " contacts here!");
        }
        Contact removedContact = contacts.remove(contactNumber - 1);
        contactSet.remove(removedContact);
        tasks.unassignTask(removedContact);
        String successMessage = "Noted. I've removed this contact:\n " + removedContact + "\n"
                + "Now you have " + contacts.size() + " contacts in the list.";
        return ExceptionCatcher.catchIoException(() -> contactStorage.rewriteFile(contacts), successMessage);
    }

    /**
     * Returns a numbered list of all contacts.
     * @return Formatted string of all contacts or empty message
     */
    public String enumContact() {
        if (contacts.isEmpty()) {
            return "You have no friends :(";
        }
        return IntStream.range(0, contacts.size())
                .mapToObj(i -> (i + 1) + "." + contacts.get(i).getName()
                            + " (" + contacts.get(i).getRelationship() + ")\n")
                .collect(Collectors.joining()) + "\n";
    }

    /**
     * Finds a contact by exact name
     * @param input Name or partial name to search for
     * @return First matching contact, or null if not found
     */
    public Contact findContact(String input) throws AlterEgoException {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String lowerInput = input.trim().toLowerCase();
        Contact result = contacts.stream()
                .filter(contact -> contact.getName().toLowerCase().equals(lowerInput))
                .findFirst()
                .orElse(null);
        if (result == null) {
            throw new AlterEgoException("Contact '" + input + "' not found.");
        }
        return result;
    }

    /**
     * Returns the number of contacts in the list.
     * @return Contact count
     */
    public int getSize() {
        return contactSet.size();
    }

    /**
     * Checks for duplicate contacts and throws exception if found.
     * @param contact Contact to check
     * @throws AlterEgoException if contact already exists
     */
    private void handleDuplicate(Contact contact) throws AlterEgoException {
        if (contactSet.contains(contact)) {
            throw new AlterEgoException("Contact already exists!");
        }
        contactSet.add(contact);
    }
}
