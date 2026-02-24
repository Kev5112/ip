package alterego.command;

import alterego.AlterEgoException;
import alterego.task.TaskList;
import alterego.ui.Ui;

/**
 * Parses and executes user commands.
 */
public class Parser {
    private boolean isExit;

    /**
     * Creates a new Parser, initializing exit loop as false.
     */
    public Parser() {
        this.isExit = false;
    }

    /**
     * Parses and executes a user command. Calls methods in taskList.
     * Skips a blank command.
     * @param input user command string
     * @param taskList task list to operate on
     * @throws AlterEgoException if command is invalid
     */
    public String execute(String input, TaskList taskList) throws AlterEgoException {
        if (input.isBlank()) {
            return "";
        }
        if (input.equals("bye")) {
            return "bye";
        }
        if (input.equals("clear")) {
            return taskList.clear();
        }
        if (input.equals("list")) {
            return taskList.enumList();
        }
        if (input.equals("help")) {
            return Ui.help();
        }
        if (input.startsWith("find")) {
            if (input.length() < 6) {
                throw new AlterEgoException("Delete what?");
            }
            return taskList.find(input.substring(5));
        }
        if (input.startsWith("delete")) {
            if (input.length() < 8) {
                throw new AlterEgoException("Delete what?");
            }
            String num = input.substring(7);
            int taskNumber = Integer.parseInt(num);
            return taskList.delete(taskNumber);
        }
        if (input.startsWith("mark")) {
            if (input.length() < 6) {
                throw new AlterEgoException("Mark what?");
            }
            String num = input.substring(5);
            int taskNumber = Integer.parseInt(num);
            return taskList.mark(taskNumber);
        }
        if (input.startsWith("unmark")) {
            if (input.length() < 8) {
                throw new AlterEgoException("Unmark what?");
            }
            String num = input.substring(7);
            int taskNumber = Integer.parseInt(num);
            return taskList.unmark(taskNumber);
        }
        if (input.startsWith("todo")) {
            if (input.length() < 6) {
                throw new AlterEgoException("Error: you didn't input the description??");
            }
            String taskName = input.substring(5);
            return taskList.addToDo(taskName);
        }
        if (input.startsWith("deadline")) {
            int index = input.indexOf("/by");
            if (input.length() < 10) {
                throw new AlterEgoException("Error: you didn't input the description??");
            } else if (index == -1) {
                throw new AlterEgoException("Error: you didn't input the deadline. "
                        + "Use '/by' to indicate the deadline");
            }
            String taskName = input.substring(9, index).trim();
            String date = input.substring(index + 4);
            return taskList.addDeadline(taskName, date);
        }
        if (input.startsWith("event")) {
            int fromIndex = input.indexOf("/from");
            int toIndex = input.indexOf("/to");
            if (input.length() < 7) {
                throw new AlterEgoException("Error: you didn't input the description??");
            } else if (fromIndex == -1 || toIndex == -1) {
                throw new AlterEgoException("Error: you fail to input the timing. "
                        + "Use '/from' to indicate the start and '/to' to indicate the end");
            }
            String taskName = input.substring(6, fromIndex).trim();
            String fromDate = input.substring(fromIndex + 6, toIndex).trim();
            String toDate = input.substring(toIndex + 4);
            return taskList.addEvent(taskName, fromDate, toDate);
        }
        throw new AlterEgoException("I don't understand that. Use 'help' to get the list of commands.");
    }

    /**
     * Checks if exit command has been received.
     * @return true if user entered "bye", false otherwise
     */
    public boolean isExit() {
        return isExit;
    }
}
