package alterego.command;

import alterego.contact.Contact;
import alterego.contact.ContactList;
import alterego.utils.AlterEgoException;
import alterego.task.TaskList;
import alterego.ui.Ui;

/**
 * Parses and executes user commands.
 */
public class Parser {
    private TaskList taskList;
    private ContactList contactList;

    public Parser(TaskList taskList, ContactList contactList) {
        assert taskList != null : "TaskList is null. Should've been handled in TaskList class";
        this.taskList = taskList;
        this.contactList = contactList;
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
        if (input.equals("contactlist")) {
            return Command.CONTACTLIST;
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
        if (input.startsWith("contact")) {
            return Command.CONTACT;
        }
        if (input.startsWith("assign")) {
            return Command.ASSIGN;
        }
        throw new AlterEgoException("I don't understand that. Use 'help' to get the list of commands.");
    }

    private void checkValidity(String input, Command command) throws AlterEgoException {
        assert command != null : "command should not be null by now";

        //no need arguments
        if (command == Command.LIST || command == Command.BYE ||
                command == Command.CLEAR || command == Command.HELP ||
                command == Command.CONTACTLIST) {
            return;
        }

        if (input.length() <= command.getStrLen()) {
            throw new AlterEgoException(getMissingArgumentMessage(command));
        }

        if (command == Command.MARK || command == Command.UNMARK) {
            validateIndex(input, command, 0);
        }
        if (command == Command.DELETE) {
            validateIndex(input, command, 1);
        }
        if (command == Command.ASSIGN) {
            validateIndex(input, command, 0);
        }
    }

    private void validateIndex(String input, Command command, int offset) throws AlterEgoException {
        String numString = extractIndexString(input, command, offset);
        try {
            int number = Integer.parseInt(numString);
            if (number < 1) {
                throw new AlterEgoException("Error: Invalid task number!");
            }
        } catch (NumberFormatException e) {
            switch (command) {
            case DELETE:
                throw new AlterEgoException("Invalid format. Proper format: "
                        + "delete t1 (delete task)/delete c1 (delete contact)");
            case MARK:
            case UNMARK:
                throw new AlterEgoException("Error: " + command.toString()
                        + " should be followed by a number!");
            case ASSIGN:
                throw new AlterEgoException("Invalid format. Proper format: "
                        + "assign {tasknumber} /to {contactname}");
            default:
                throw new AssertionError("Should not reach here");
            }
        }
    }

    private String extractIndexString(String input, Command command, int offset) throws AlterEgoException {
        String numString = input.substring(command.getStrLen() + 1 + offset).trim();
        String[] parts = numString.split(" ");
        return parts[0];
    }

    private String executeCommand(String input, Command command) throws AlterEgoException {
        switch (command) {
        case BYE:
            return Ui.bye();
        case CLEAR:
            return taskList.clear();
        case LIST:
            return taskList.enumList();
        case HELP:
            return Ui.help();
        case FIND:
            return taskList.find(input.substring(Command.FIND.getStrLen()).trim());
        case DELETE:
            return handleDelete(input);
        case MARK:
            return taskList.mark(Integer.parseInt(extractIndexString(input, command, 0)));
        case UNMARK:
            return taskList.unmark(Integer.parseInt(extractIndexString(input, command, 0)));
        case TODO:
            return taskList.addToDo(input.substring(Command.TODO.getStrLen()).trim());
        case DEADLINE:
            return handleDeadline(input);
        case EVENT:
            return handleEvent(input);
        case CONTACT:
            return handleContact(input);
        case CONTACTLIST:
            return contactList.enumContact();
        case ASSIGN:
            return handleAssign(input);
        default:
            throw new AssertionError("Should not reach here");
        }
    }

    private String handleContact(String input) throws AlterEgoException {
        int slashIndex = input.indexOf("/as");
        if (slashIndex == -1) {
            throw new AlterEgoException("Invalid format. Use: add-contact NAME /as RELATIONSHIP");
        }
        String name = input.substring(Command.CONTACT.getStrLen(), slashIndex).trim();
        String relationship = input.substring(slashIndex + "/as".length()).trim();
        if (name.isEmpty() || relationship.isEmpty()) {
            throw new AlterEgoException("Name and relationship cannot be empty.");
        }
        contactList.addContact(name, relationship);
        return "Added contact: " + name + " (" + relationship + ")";
    }

    private String handleAssign(String input) throws AlterEgoException {
        int toIndex = input.indexOf("/to");
        if (toIndex == -1) {
            throw new AlterEgoException("Invalid format. Use: assign TASK_NUMBER /to CONTACT_NAME");
        }
        String taskNumStr = input.substring(Command.ASSIGN.getStrLen(), toIndex).trim();
        String contactName = input.substring(toIndex + "/to".length()).trim();

        try {
            int taskNum = Integer.parseInt(taskNumStr);
            Contact contact = contactList.findContact(contactName);

            if (contact == null) {
                throw new AlterEgoException("Contact '" + contactName + "' not found.");
            }

            return taskList.assignTask(taskNum, contact);

        } catch (NumberFormatException e) {
            throw new AlterEgoException("Invalid task number: " + taskNumStr);
        }
    }

    private String handleDelete(String input) throws AlterEgoException {
        String[] parts = input.split(" ");
        if (parts[1].startsWith("t")) {
            return taskList.delete(Integer.parseInt(parts[1].substring(1).trim()));
        }
        if (parts[1].startsWith("c")) {
            return contactList.delete(Integer.parseInt(parts[1].substring(1).trim()));
        }
        throw new AlterEgoException("Invalid format. Proper format : delete {t/c}{number}");
    }

    private String handleDeadline(String input) throws AlterEgoException {
        int byIndex = input.indexOf("/by");
        if (byIndex == -1) {
            throw new AlterEgoException("Error: you didn't input the deadline. "
                    + "Use '/by' to indicate the deadline");
        }

        assert byIndex > Command.DEADLINE.getStrLen() : "/by should be after deadline command";

        String taskName = input.substring(Command.DEADLINE.getStrLen(), byIndex).trim();
        String dateString = input.substring(byIndex + "/by".length()).trim();

        assert taskName != null : "Task name should not be null";
        assert dateString != null : "Date string should not be null";

        return taskList.addDeadline(taskName, dateString);
    }

    private String handleEvent(String input) {
        int fromIndex = input.indexOf("/from");
        int toIndex = input.indexOf("/to");
        if (fromIndex == -1 || toIndex == -1) {
            throw new AlterEgoException("Error: you fail to input the timing. "
                    + "Use '/from' to indicate the start and '/to' to indicate the end");
        }

        assert fromIndex > Command.EVENT.getStrLen() : "/from should be after event command";
        assert toIndex > fromIndex : "/to should come after /from";

        String taskName = input.substring(Command.EVENT.getStrLen(), fromIndex).trim();
        String fromDate = input.substring(fromIndex + "/from".length(), toIndex).trim();
        String toDate = input.substring(toIndex + "/to".length()).trim();

        assert taskName != null : "Task name should not be null";
        assert fromDate != null : "From date should not be null";
        assert toDate != null : "To date should not be null";

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
        case DEADLINE:
        case EVENT:
            return "Error: you didn't input the description??";
        case CONTACT:
            return "Name and relationship?";
        case ASSIGN:
            return "Assign what?";
        default:
            throw new AssertionError("Should not reach here, missingArgumentMessage");
        }
    }
}
