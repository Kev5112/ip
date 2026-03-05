package alterego;

import alterego.command.Parser;
import alterego.list.ContactList;
import alterego.storage.ContactStorage;
import alterego.storage.Storage;
import alterego.storage.TaskStorage;
import alterego.list.TaskList;
import alterego.utils.AlterEgoException;

/**
 * Represents the main class for the AlterEgo chatbot application.
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
     * @param filePathTask the path to the file where tasks are stored.
     * @param filePathContact the path to the file where contacts are stored.
     */
    public AlterEgo(String filePathTask, String filePathContact) {
        taskStorage = new TaskStorage(filePathTask);
        contactStorage = new ContactStorage(filePathContact);
        taskList = new TaskList(taskStorage);
        contactList = new ContactList(contactStorage, taskList);
        parser = new Parser(taskList, contactList);
    }

    /**
     * Processes user input and returns the chatbot's response.
     * Catches any AlterEgoException and returns its message.
     * @param input Raw user input string
     * @return Response message from the chatbot
     */
    public String getResponse(String input) {
        try {
            String output = parser.execute(input);
            return output;
        } catch (AlterEgoException e) {
            return e.getMessage();
        }
    }

    /**
     * Returns the combined load status from both task and contact storage.
     * If both storages loaded successfully, returns null.
     * @return Combined warning messages, or null if no warnings
     */
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
