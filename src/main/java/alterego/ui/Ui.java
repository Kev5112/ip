package alterego.ui;

import java.util.Scanner;

import alterego.command.Command;

/**
 * User interface handler for AlterEgo chatbot.
 */
public class Ui {
    public static final String LINE = "";

    /**
     * Shows welcome message.
     *
     * @return
     */
    public static String hello() {
        return "Hello, I'm Alter Ego!\n\nWhat can I do for you?\n\n"
                + "Note: Type 'help' to get started";
    }

    /**
     * Shows goodbye message.
     */
    public static String bye() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Shows all available commands.
     */
    public static String help() {
        String accum = "Task adding: \n";
        int j = 0;
        for (Command command : Command.values()) {
            accum = accum + command.toString().toLowerCase() + "\n";
            j++;
            if (j == 3) {
                accum = accum + "\nContact adding: \n";
            }
            if (j == 4) {
                accum = accum + "\nView: \n";
            }
            if (j == 6) {
                accum = accum + "\nOther commands: \n";
            }
        }
        return accum;
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
