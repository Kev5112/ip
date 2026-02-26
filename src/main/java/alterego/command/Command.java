package alterego.command;

/**
 * Enumeration of all available commands in AlterEgo.
 */
public enum Command {
    TODO,
    DEADLINE,
    EVENT,
    CONTACT,
    LIST,
    CONTACTLIST,
    DELETE,
    FIND,
    CLEAR,
    HELP,
    MARK,
    UNMARK,
    ASSIGN,
    BYE;

    /**
     * Returns the length of the command string.
     * @return Length of the command name in characters
     */
    public int getStrLen() {
        return this.toString().length();
    }

    /**
     * Returns the lowercase string representation of the command.
     * @return Command name in lowercase
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
