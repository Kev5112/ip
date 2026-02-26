package alterego.utils;

/**
 * Represents custom exception for AlterEgo application errors.
 */
public class AlterEgoException extends RuntimeException {

    /**
     * Creates an exception with the given error message.
     * @param message error message to display to user
     */
    public AlterEgoException(String message) {
        super(message);
    }
}
