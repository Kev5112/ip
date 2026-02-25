package alterego;

import alterego.command.Parser;
import alterego.storage.Storage;
import alterego.task.TaskList;
import alterego.ui.Ui;

/**
 * The main class for the AlterEgo chatbot application.
 * AlterEgo is a task management chatbot that helps users track todos, deadlines, and events.
 */
public class AlterEgo {

    private Storage storage;
    private TaskList taskList;
    private Parser parser;

    /**
     * Constructs an AlterEgo chatbot instance with the specified file path for data storage.
     * @param filePath the path to the file where tasks are stored.
     */
    public AlterEgo(String filePath) {
        storage = new Storage(filePath);
        taskList = new TaskList(storage);
        parser = new Parser(taskList);
    }

    public String getResponse(String input) {
        try {
            String output = parser.execute(input, taskList);
            return output;
        } catch (AlterEgoException e) {
            return Ui.decorate(e.getMessage());
        }
    }
}
