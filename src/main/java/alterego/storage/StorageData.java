package alterego.storage;

import alterego.contact.Contact;
import alterego.task.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StorageData {
    private final ArrayList<Task> tasks;
    private final Set<Contact> contacts;

    public StorageData(ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.contacts = new HashSet<>();

        for (Task task : tasks) {
            Contact contact = task.getAssignedTo();
            if (contact != null) {
                contacts.add(contact);
            }
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public ArrayList<Contact> getContactList() {
        return new ArrayList<>(contacts);
    }
}