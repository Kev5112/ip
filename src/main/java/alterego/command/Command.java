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

    public int getStrLen() {
        return this.toString().length();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
