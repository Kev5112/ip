package alterego.ui;

import alterego.command.Command;

/**
 * Represents the user interface handler for AlterEgo chatbot.
 */
public class Ui {
    /**
     * Returns welcome message.
     * @return Welcome message
     */
    public static String hello() {
        return "Hello, I'm Alter Ego!\n\nWhat can I do for you?\n\n"
                + "Note: Type 'help' to get started";
    }

    /**
     * Returns the goodbye message displayed when exiting the application.
     * @return Goodbye message
     */
    public static String bye() {
        return "Bye. Don't forget to hydrate! (and touch grass)";
    }

    /**
     * Returns a categorized list of all available commands.
     * Commands are grouped by functionality: task adding, contact adding, view, and other.
     * @return Formatted help text with all commands
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
     * For future needs when string formatting is needed.
     * @param message message to format
     * @return formatted message
     */
    public static String decorate(String message) {
        return message;
    }
}
