package alterego.ui;

import alterego.command.Command;

import java.util.Scanner;

public class Ui {
    Scanner scanner;
    public static final String LINE = "____________________________________________________________\n";

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public static void hello() {
        show("Hello! I'm Alter Ego\nWhat can I do for you?\n");
    }

    public static void bye() {
        show("Bye. Hope to see you again soon!");
    }

    public static void help() {
        System.out.print(Ui.LINE);
        for (Command command : Command.values()) {
            System.out.println(" " + command.toString().toLowerCase());
        }
        System.out.println(Ui.LINE);
    }

    public static String decorate(String message) {
        String[] lines = message.split("\\R");
        String accum = "";
        for (String line : lines) {
            accum = accum + " " + line + "\n";
        }
        return Ui.LINE + accum + Ui.LINE;
    }

    public static void show(String message) {
        System.out.println(decorate(message));
    }
}
