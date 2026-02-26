package alterego.storage;

import alterego.contact.Contact;
import alterego.task.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ContactStorage {
    private final String filePath;

    public ContactStorage(String path) {
        this.filePath = path;
    }

    public void clear() throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write("");
        fw.close();
    }

    public void rewriteFile(ArrayList<Contact> contacts) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        for (Contact contact : contacts) {
            fw.write(contact.getName() + "|" + contact.getRelationship() + System.lineSeparator());
        }
        fw.close();

        File f = new File(filePath);
    }

    public ArrayList<Contact> loadContacts() throws FileNotFoundException {
        ArrayList<Contact> contacts = new ArrayList<>();
        File f = new File(filePath);
        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String[] parts = s.nextLine().split("\\|");
                if (parts.length == 2) {
                    contacts.add(new Contact(parts[0].trim(), parts[1].trim()));
                }
            }
        }
        return contacts;
    }
}