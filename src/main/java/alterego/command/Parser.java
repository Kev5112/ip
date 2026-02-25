package alterego.command;

import alterego.AlterEgoException;
import alterego.task.TaskList;
import alterego.ui.Ui;

/**
 * Parses and executes user commands.
 */
public class Parser {
    private TaskList taskList;

    public Parser(TaskList taskList) {
        this.taskList = taskList;
    }

    public String execute(String input) throws AlterEgoException {
        if (input.isBlank()) {
            return "";
        }

        Command command = commandExtractor(input);
        checkValidity(input, command);
        return executeCommand(input, command);
    }

    private Command commandExtractor(String input) throws AlterEgoException {
        if (input.equals("bye")) {
            return Command.BYE;
        }
        if (input.equals("clear")) {
            return Command.CLEAR;
        }
        if (input.equals("list")) {
            return Command.LIST;
        }
        if (input.equals("help")) {
            return Command.HELP;
        }
        if (input.startsWith("find")) {
            return Command.FIND;
        }
        if (input.startsWith("delete")) {
            return Command.DELETE;
        }
        if (input.startsWith("mark")) {
            return Command.MARK;
        }
        if (input.startsWith("unmark")) {
            return Command.UNMARK;
        }
        if (input.startsWith("todo")) {
            return Command.TODO;
        }
        if (input.startsWith("deadline")) {
            return Command.DEADLINE;
        }
        if (input.startsWith("event")) {
            return Command.EVENT;
        }
        throw new AlterEgoException("I don't understand that. Use 'help' to get the list of commands.");
    }

    private void checkValidity(String input, Command command) throws AlterEgoException {
        if (command == Command.BYE || command == Command.CLEAR ||
                command == Command.LIST || command == Command.HELP) {
            return;
        }

        if (input.length() <= command.getStrLen()) {
            throw new AlterEgoException(getMissingArgumentMessage(command));
        }

        if (command == Command.DELETE || command == Command.MARK || command == Command.UNMARK) {
            validateIndex(input, command);
        }
    }

    private void validateIndex(String input, Command command) throws AlterEgoException {
        String numString = extractIndexString(input, command);
        if (!numString.matches("-?\\d+")) {
            throw new AlterEgoException("Input should be a number!");
        }
        if (Integer.parseInt(numString) < 1) {
            throw new AlterEgoException("Invalid task number!");
        }
    }

    private String extractIndexString(String input, Command command) throws AlterEgoException {
        String numString = input.substring(command.getStrLen()).trim();
        assert !numString.contains(" ") : "Number string should have no spaces";
        return numString;
    }

    private String executeCommand(String input, Command command) throws AlterEgoException {
        switch (command) {
        case BYE:
            return "bye";
        case CLEAR:
            return taskList.clear();
        case LIST:
            return taskList.enumList();
        case HELP:
            return Ui.help();
        case FIND:
            return taskList.find(input.substring(Command.FIND.getStrLen()).trim());
        case DELETE:
            return taskList.delete(Integer.parseInt(extractIndexString(input, command)));
        case MARK:
            return taskList.mark(Integer.parseInt(extractIndexString(input, command)));
        case UNMARK:
            return taskList.unmark(Integer.parseInt(extractIndexString(input, command)));
        case TODO:
            return taskList.addToDo(input.substring(Command.TODO.getStrLen()).trim());
        case DEADLINE:
            return handleDeadline(input);
        case EVENT:
            return handleEvent(input);
        default:
            throw new AssertionError("Should not reach here");
        }
    }

    private String handleDeadline(String input) throws AlterEgoException {
        int byIndex = input.indexOf("/by");
        if (byIndex == -1) {
            throw new AlterEgoException("Error: you didn't input the deadline. "
                    + "Use '/by' to indicate the deadline");
        }
        String taskName = input.substring(Command.DEADLINE.getStrLen(), byIndex).trim();
        String dateString = input.substring(byIndex + "/by".length()).trim();
        return taskList.addDeadline(taskName, dateString);
    }

    private String handleEvent(String input) {
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");
        if (fromIndex == -1 || toIndex == -1) {
            throw new AlterEgoException("Error: you fail to input the timing. "
                    + "Use '/from' to indicate the start and '/to' to indicate the end");
        }
        String taskName = input.substring(Command.EVENT.getStrLen(), fromIndex).trim();
        String fromDate = input.substring(fromIndex + "/from".length(), toIndex).trim();
        String toDate = input.substring(toIndex + "/to".length()).trim();
        return taskList.addEvent(taskName, fromDate, toDate);
    }

    private String getMissingArgumentMessage(Command command) {
        switch (command) {
        case FIND:
            return "Find what?";
        case DELETE:
            return "Delete what?";
        case MARK:
            return "Mark what?";
        case UNMARK:
            return "Unmark what?";
        case TODO:
            return "Error: you didn't input the description??";
        case DEADLINE:
            return "Error: you didn't input the description??";
        case EVENT:
            return "Error: you didn't input the description??";
        default:
            throw new AssertionError("Should not reach here");
        }
    }
}
