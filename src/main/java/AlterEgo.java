import java.util.Scanner;

public class AlterEgo {
    private static TaskList taskList = new TaskList();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(CommonWords.GREETING);

        while(true) {
            String input = scanner.nextLine();

            if(input.equals("bye")) {
                break;
            }
            try {
                processInput(input);
            } catch (EmptyTaskException e) {
                System.out.println(e.getMessage());
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.print(CommonWords.FAREWELL);
    }

    private static void processInput(String input) throws EmptyTaskException, InvalidInputException {
        if (input.isBlank()) {
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
                throw new EmptyTaskException(" Delete what?");
            }
            String num = input.substring(7);
            int taskNumber = Integer.parseInt(num);
            taskList.delete(taskNumber);
            return;
        }
        if(input.startsWith("mark")) {
            if (input.length() < 6) {
                throw new EmptyTaskException(" Mark what?");
            }
            String num = input.substring(5);
            int taskNumber = Integer.parseInt(num);
            taskList.mark(taskNumber);
            return;
        }
        if(input.startsWith("unmark")) {
            if (input.length() < 8) {
                throw new EmptyTaskException(" Unmark what?");
            }
            String num = input.substring(7);
            int taskNumber = Integer.parseInt(num);
            taskList.unmark(taskNumber);
            return;
        }
        if (input.startsWith("todo")) {
            if (input.length() < 6) {
                throw new EmptyTaskException(" Error: you didn't input the description??");
            }
            String taskName = input.substring(5);
            taskList.addToDo(taskName);
            return;
        }
        if (input.startsWith("deadline")) {
            int index = input.indexOf("/by");
            if (input.length() < 10) {
                throw new EmptyTaskException(" Error: you didn't input the description??");
            } else if (index == -1) {
                throw new EmptyTaskException(" Error: you didn't input the deadline. " +
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
                throw new EmptyTaskException(" Error: you didn't input the description??");
            } else if (fromIndex == -1 || toIndex == -1) {
                throw new EmptyTaskException(" Error: you fail to input the timing. " +
                        "Use '/from' to indicate the start and '/to' to indicate the end");
            }
            String taskName = input.substring(6, fromIndex).trim();
            String fromDate = input.substring(fromIndex + 6, toIndex).trim();
            String toDate = input.substring(toIndex + 4);
            taskList.addEvent(taskName, fromDate, toDate);
            return;
        }
        throw new InvalidInputException(" I don't understand that. Use 'help' to get the list of commands.");
    }

    public static void help() {
        System.out.println(CommonWords.LINE + " list\n help\n delete\n mark\n unmark\n todo\n deadline\n event\n bye\n" + CommonWords.LINE);
    }
}
