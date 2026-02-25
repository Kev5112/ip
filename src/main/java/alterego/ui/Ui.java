package alterego.ui;

import java.util.Scanner;

import alterego.command.Command;

/**
 * User interface handler for AlterEgo chatbot.
 */
public class Ui {
    public static final String LINE = "____________________________________________________________\n";

    /**
     * Shows welcome message.
     *
     * @return
     */
    public static String hello() {
        return decorate("Hello! I'm Alter Ego\nWhat can I do for you?\n");
    }

    /**
     * Shows goodbye message.
     */
    public static String bye() {
        return decorate("Bye. Hope to see you again soon!)");
    }

    /**
     * Shows all available commands.
     */
    public static String help() {
        String accum = "";
        for (Command command : Command.values()) {
            accum = accum + command.toString().toLowerCase() + "\n";
        }
        return Ui.decorate(accum);
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
}
