package alterego.command;

import alterego.contact.Contact;
import alterego.contact.ContactList;
import alterego.task.TaskList;
import alterego.ui.Ui;
import alterego.utils.AlterEgoException;

/**
 * Parses user's input and executes user commands.
 */
public class Parser {
    private TaskList taskList;
    private ContactList contactList;

    /**
     * Creates a Parser with the given task and contact lists.
     * @param taskList Task list for task operations
     * @param contactList Contact list for contact operations
     */
    public Parser(TaskList taskList, ContactList contactList) {
        assert taskList != null : "TaskList is null. Should've been handled in TaskList class";
        this.taskList = taskList;
        this.contactList = contactList;
    }

    /**
     * Executes the user input command and returns the response.
     * Extract command -> check validity -> execute the command.
     * @param input Raw user input string
     * @return Response message from the executed command
     * @throws AlterEgoException if command is invalid or execution fails
     */
    public String execute(String input) throws AlterEgoException {
        if (input.isBlank()) {
            return "";
        }

        Command command = commandExtractor(input.trim().toLowerCase());
        checkValidity(input, command);
        return executeCommand(input, command);
    }

    /**
     * Extracts the command type from user input.
     */
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

    /**
     * Check validity for the input
     */
    private void checkValidity(String input, Command command) throws AlterEgoException {
        assert command != null : "command should not be null by now";

        //no need arguments
        if (command == Command.LIST || command == Command.BYE
                || command == Command.CLEAR || command == Command.HELP
                || command == Command.CONTACTLIST) {
            return;
        }

        if (input.trim().length() <= command.getStrLen()) {
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

    // helper function
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

    // helper function
    private String extractIndexString(String input, Command command, int offset) throws AlterEgoException {
        String numString = input.substring(command.getStrLen() + 1 + offset).trim();
        String[] parts = numString.split(" ");
        return parts[0];
    }

    /**
     * Executes command
     */
    private String executeCommand(String input, Command command) throws AlterEgoException {
        switch (command) {
        case BYE:
            return Ui.bye();
        case CLEAR:
            contactList.clear();
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

    // helper function
    private String handleContact(String input) throws AlterEgoException {
        int slashIndex = input.indexOf("/as");
        if (slashIndex == -1) {
            throw new AlterEgoException("Invalid format. Proper format: contact {name} /as {relationship}");
        }
        String name = input.substring(Command.CONTACT.getStrLen(), slashIndex).trim();
        String relationship = input.substring(slashIndex + "/as".length()).trim();
        if (name.isEmpty() || relationship.isEmpty()) {
            throw new AlterEgoException("Name and relationship cannot be empty.");
        }
        return contactList.addContact(name, relationship);
    }

    // helper function
    private String handleAssign(String input) throws AlterEgoException {
        int toIndex = input.indexOf("/to");
        if (toIndex == -1) {
            throw new AlterEgoException("Invalid format. Proper format: assign {tasknumber} /to {name}");
        }
        String taskNumString = input.substring(Command.ASSIGN.getStrLen(), toIndex).trim();
        String contactName = input.substring(toIndex + "/to".length()).trim();

        try {
            int taskNum = Integer.parseInt(taskNumString);
            Contact contact = contactList.findContact(contactName);
            return taskList.assignTask(taskNum, contact);

        } catch (NumberFormatException e) {
            throw new AlterEgoException("Invalid task number: " + taskNumString);
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
        return taskList.addEvent(taskName, fromDate, toDate);
    }

    private String getMissingArgumentMessage(Command command) {
        switch (command) {
        case FIND:
            return "Use find command to search a task with keyword in the task list. Use: find {keyword}";
        case DELETE:
            return "Use delete command to delete an object in the task list/contact list. Use: delete {t/c}{index}\n"
                    + "e.g. delete t1";
        case MARK:
            return "Use mark command to mark a task in the list as done. Use: mark {index}";
        case UNMARK:
            return "Use ummark command to mark a task in the list as not done. Use: unmark {index}";
        case TODO:
        case DEADLINE:
        case EVENT:
            return "Error: please input the task description";
        case CONTACT:
            return "Use contact command to add a contact. Use: contact {name} /as {relationship}";
        case ASSIGN:
            return "Use assign command to assign a task to a contact. Use: assign {tasknumber} /to {contactname}";
        default:
            throw new AssertionError("Should not reach here, missingArgumentMessage");
        }
    }
}
