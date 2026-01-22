import java.util.Scanner;

public class AlterEgo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskList taskList = new TaskList();

        System.out.println(CommonWords.GREETING);

        while(true) {
            String input = scanner.nextLine();

            if(input.equals("bye")) {
                break;
            }
            if(input.equals("list")) {
                taskList.enumList();
                continue;
            }
            if(input.startsWith("mark ")) {
                String num = input.substring(5);
                int taskNumber = Integer.parseInt(num);
                taskList.mark(taskNumber);
                continue;
            }
            if(input.startsWith("unmark ")) {
                String num = input.substring(7);
                int taskNumber = Integer.parseInt(num);
                taskList.unmark(taskNumber);
                continue;
            }
            if (input.startsWith("todo ")) {
                String taskName = input.substring(5);
                taskList.addToDo(taskName);
                continue;
            }
            if (input.startsWith("deadline ")) {
                int index = input.indexOf("/by");
                String taskName = input.substring(9, index).trim();
                String date = input.substring(index + 4);
                taskList.addDeadline(taskName, date);
                continue;
            }
            if (input.startsWith("event ")) {
                int fromIndex = input.indexOf("/from");
                int toIndex = input.indexOf("/to");
                String taskName = input.substring(6, fromIndex).trim();
                String fromDate = input.substring(fromIndex + 6, toIndex).trim();
                String toDate = input.substring(toIndex + 4);
                taskList.addEvent(taskName, fromDate, toDate);
            }
        }

        System.out.println(CommonWords.FAREWELL);
    }
}
