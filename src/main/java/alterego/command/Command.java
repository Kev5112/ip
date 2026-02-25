package alterego.command;

/**
 * Enumeration of all available commands in AlterEgo.
 */
public enum Command {
    LIST,
    CLEAR,
    HELP,
    FIND,
    DELETE,
    MARK,
    UNMARK,
    TODO,
    DEADLINE,
    EVENT,
    BYE;

    public static Command fromString(String input) {
        return Command.valueOf(input.toUpperCase());
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
