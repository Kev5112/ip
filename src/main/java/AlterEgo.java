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

            taskList.addTask(input);
        }

        System.out.println(CommonWords.FAREWELL);
    }
}
