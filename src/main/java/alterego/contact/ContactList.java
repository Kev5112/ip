package alterego.contact;

import alterego.storage.ContactStorage;
import alterego.task.TaskList;
import alterego.utils.AlterEgoException;
import alterego.utils.ExceptionCatcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContactList {
    private String loadStatus = null;
    private ArrayList<Contact> contacts;
    private TaskList tasks;
    private Set<Contact> contactSet;
    private Contact bestFriend = null;
    private ContactStorage contactStorage;

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
            loadStatus = "Warning: File not found. Creating a new list.";
        }
    }

    public String getLoadStatus() {
        return loadStatus;
    }

    public void clear() {
        contacts = new ArrayList<>();
        contactSet = new HashSet<>();
        ExceptionCatcher.catchIoException(contactStorage::clear, "");
    }

    public void setBestFriend(Contact contact) {
        if (!contacts.contains(contact)) {
            throw new AlterEgoException("Contact not found in list!");
        }
        contact.setRelationship("bestfriend");
        this.bestFriend = contact;
    }

    public String addContact(String personName, String relationship) {
        Contact contact = new Contact(personName, relationship);
        handleDuplicate(contact);
        contacts.add(contact);
        String successMessage = "Got it. I've added this contact:\n " + contact
                + "\nNow you have " + contacts.size() + " contacts in the list.\n";
        return ExceptionCatcher.catchIoException(() -> contactStorage.rewriteFile(contacts), successMessage);
    }

    public String addContact(Contact contact) {
        handleDuplicate(contact);
        contacts.add(contact);
        String successMessage = "Got it. I've added this contact:\n " + contact
                + "\nNow you have " + contacts.size() + " contacts in the list.\n";
        return ExceptionCatcher.catchIoException(() -> contactStorage.rewriteFile(contacts), successMessage);
    }

    public String delete(int contactNumber) throws AlterEgoException {
        if (contactNumber > contacts.size()) {
            throw new AlterEgoException("There's only " + contacts.size() + " contacts here!");
        }
        Contact removedContact = contacts.remove(contactNumber - 1);
        contactSet.remove(removedContact);
        if (removedContact == bestFriend) {
            bestFriend = null;
        }
        tasks.unassignTask(removedContact);
        String successMessage = "Noted. I've removed this contact:\n " + removedContact + "\n"
                + "Now you have " + contacts.size() + " contacts in the list.";
        return ExceptionCatcher.catchIoException(() -> contactStorage.rewriteFile(contacts), successMessage);
    }

    public String enumContact() {
        if (contacts.isEmpty()) {
            return "You have no friends :(";
        }
        return IntStream.range(0, contacts.size())
                .mapToObj(i -> {
                    Contact c = contacts.get(i);
                    String star = (c == bestFriend) ? " *" : "";
                    return (i + 1) + "." + c.getName()
                            + " (" + c.getRelationship() + ")" + star + "\n";
                })
                .collect(Collectors.joining()) + "\n";
    }

    public Contact findContact(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String lowerInput = input.trim().toLowerCase();
        return contacts.stream()
                .filter(contact -> contact.getName().toLowerCase().contains(lowerInput))
                .findFirst()
                .orElse(null);
    }

    public int getSize() {
        return contactSet.size();
    }

    private void handleDuplicate(Contact contact) throws AlterEgoException {
        if (contactSet.contains(contact)) {
            throw new AlterEgoException("Contact already exists!");
        }
        contactSet.add(contact);
    }
}
