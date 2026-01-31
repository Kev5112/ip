public class Parser {
    private boolean isExit;

    public Parser() {
        this.isExit = false;
    }

    public void execute(String input, TaskList taskList) throws AlterEgoException {
        if (input.isBlank()) {
            return;
        }
        if (input.equals("bye")) {
            isExit = true;
            return;
        }
        if (input.equals("clear")) {
            taskList.clear();
            return;
        }
        if(input.equals("list")) {
            taskList.enumList();
            return;
        }
        if (input.equals("help")) {
            help();
            return;
        }
        if(input.startsWith("delete")) {
            if (input.length() < 8) {
                throw new AlterEgoException("Delete what?");
            }
            String num = input.substring(7);
            int taskNumber = Integer.parseInt(num);
            taskList.delete(taskNumber);
            return;
        }
        if(input.startsWith("mark")) {
            if (input.length() < 6) {
                throw new AlterEgoException("Mark what?");
            }
            String num = input.substring(5);
            int taskNumber = Integer.parseInt(num);
            taskList.mark(taskNumber);
            return;
        }
        if(input.startsWith("unmark")) {
            if (input.length() < 8) {
                throw new AlterEgoException("Unmark what?");
            }
            String num = input.substring(7);
            int taskNumber = Integer.parseInt(num);
            taskList.unmark(taskNumber);
            return;
        }
        if (input.startsWith("todo")) {
            if (input.length() < 6) {
                throw new AlterEgoException("Error: you didn't input the description??");
            }
            String taskName = input.substring(5);
            taskList.addToDo(taskName);
            return;
        }
        if (input.startsWith("deadline")) {
            int index = input.indexOf("/by");
            if (input.length() < 10) {
                throw new AlterEgoException("Error: you didn't input the description??");
            } else if (index == -1) {
                throw new AlterEgoException("Error: you didn't input the deadline. " +
                        "Use '/by' to indicate the deadline");
            }
            String taskName = input.substring(9, index).trim();
            String date = input.substring(index + 4);
            taskList.addDeadline(taskName, date);
            return;
        }
        if (input.startsWith("event")) {
            int fromIndex = input.indexOf("/from");
            int toIndex = input.indexOf("/to");
            if (input.length() < 7) {
                throw new AlterEgoException("Error: you didn't input the description??");
            } else if (fromIndex == -1 || toIndex == -1) {
                throw new AlterEgoException("Error: you fail to input the timing. " +
                        "Use '/from' to indicate the start and '/to' to indicate the end");
            }
            String taskName = input.substring(6, fromIndex).trim();
            String fromDate = input.substring(fromIndex + 6, toIndex).trim();
            String toDate = input.substring(toIndex + 4);
            taskList.addEvent(taskName, fromDate, toDate);
            return;
        }
        throw new AlterEgoException("I don't understand that. Use 'help' to get the list of commands.");
    }

    public static void help() {
        System.out.print(Line.LINE);
        for (Command command : Command.values()) {
            System.out.println(" " + command.toString().toLowerCase());
        }
        System.out.println(Line.LINE);
    }

    public boolean isExit() {
        return isExit;
    }
}
