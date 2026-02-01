package alterego.ui;

import java.util.Scanner;

import alterego.command.Command;

/**
 * User interface handler for AlterEgo chatbot.
 */
public class Ui {
    public static final String LINE = "____________________________________________________________\n";
    private Scanner scanner;

    /**
     * Creates a new UI handler.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a command from user input.
     * @return the command string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows welcome message.
     */
    public static void hello() {
        show("Hello! I'm Alter Ego\nWhat can I do for you?\n");
    }

    /**
     * Shows goodbye message.
     */
    public static void bye() {
        show("Bye. Hope to see you again soon!");
    }

    /**
     * Shows all available commands.
     */
    public static void help() {
        String accum = "";
        for (Command command : Command.values()) {
            accum = accum + command.toString().toLowerCase() + "\n";
        }
        Ui.show(accum);
    }

    /**
     * Decorates the message.
     * @param message message to format
     * @return formatted message
     */
    public static String decorate(String message) {
        String[] lines = message.split("\\R");
        String accum = "";
        for (String line : lines) {
            accum = accum + " " + line + "\n";
        }
        return Ui.LINE + accum + Ui.LINE;
    }

    /**
     * Displays a decorated message to the user.
     * @param message message to display
     */
    public static void show(String message) {
        System.out.println(decorate(message));
    }
}
