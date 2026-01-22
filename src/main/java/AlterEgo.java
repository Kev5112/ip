import java.util.Scanner;

public class AlterEgo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String greeting = "____________________________________________________________\n"
                + " Hello! I'm Alter Ego\n"
                + " What can I do for you?\n"
                + "____________________________________________________________\n";
        String farewell = " Bye. Hope to see you again soon!\n"
                + "____________________________________________________________";
        String line = "____________________________________________________________";
        System.out.println(greeting);

        while(true) {
            String input = scanner.nextLine();
            if(input.equals("bye")) break;
            System.out.println(line + "\n " + input + "\n" + line + "\n");
        }
        System.out.println(farewell);
    }
}
