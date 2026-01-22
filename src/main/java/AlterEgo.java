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

            taskList.addTask(input);
        }

        System.out.println(CommonWords.FAREWELL);
    }
}
