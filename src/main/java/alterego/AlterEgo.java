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
    private Ui ui;

    /**
     * Constructs an AlterEgo chatbot instance with the specified file path for data storage.
     * @param filePath the path to the file where tasks are stored.
     */
    public AlterEgo(String filePath) {
        storage = new Storage(filePath);
        taskList = new TaskList(storage.loadTasks(), storage);
        ui = new Ui();
    }

    /**
     * Starts the AlterEgo chatbot application.
     * This method displays a welcome message, then continuously reads user input,
     * parses and executes commands, until the user inputs "bye".
     */
    public void run() {
        Ui.hello();
        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            try {
                Parser parser = new Parser();
                parser.execute(input, taskList);
                isExit = parser.isExit();
            } catch (AlterEgoException e) {
                Ui.show(e.getMessage());
            }
        }
        Ui.bye();
    }

    /**
     * Main method to start the process. creates a new AlterEgo instance and runs the process.
     * @param args
     */
    public static void main(String[] args) {
        AlterEgo chatbot = new AlterEgo("./data/alterego.AlterEgo.txt");
        chatbot.run();
    }
}
