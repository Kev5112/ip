package alterego;

import alterego.command.Parser;
import alterego.contact.ContactList;
import alterego.storage.ContactStorage;
import alterego.storage.TaskStorage;
import alterego.task.TaskList;
import alterego.utils.AlterEgoException;

/**
 * The main class for the AlterEgo chatbot application.
 * AlterEgo is a task management chatbot that helps users track todos, deadlines, and events.
 */
public class AlterEgo {

    private TaskStorage taskStorage;
    private ContactStorage contactStorage;
    private TaskList taskList;
    private ContactList contactList;
    private Parser parser;

    /**
     * Constructs an AlterEgo chatbot instance with the specified file path for data storage.
     * @param filePath the path to the file where tasks are stored.
     */
    public AlterEgo(String filePathTask, String filePathContact) {
        taskStorage = new TaskStorage(filePathTask);
        contactStorage = new ContactStorage(filePathContact);
        taskList = new TaskList(taskStorage);
        contactList = new ContactList(contactStorage, taskList);
        parser = new Parser(taskList, contactList);
    }

    public String getResponse(String input) {
        try {
            String output = parser.execute(input);
            return output;
        } catch (AlterEgoException e) {
            return e.getMessage();
        }
    }

    public String getLoadStatus() {
        String taskWarning = taskList.getLoadStatus();
        String contactWarning = contactList.getLoadStatus();
        if (taskWarning == null && contactWarning == null) {
            return null;
        }
        return (taskWarning == null ? "" : taskWarning + "\n")
                + (contactWarning == null ? "" : contactWarning);
    }
}
