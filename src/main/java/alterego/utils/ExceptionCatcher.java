package alterego.utils;

import java.io.IOException;

/**
 * Utility class for repetitive IO exception handling
 */
public class ExceptionCatcher {
    /**
     * Executes a file operation, catches IO exceptions, returns success message.
     * Converts IO exceptions to AlterEgoExceptions.
     * @param fileOperation The file operation to execute
     * @param successMessage Message to return if operation succeeds
     * @return The success message if operation completes without exception
     * @throws AlterEgoException if an IO exception occurs during execution
     */
    public static String catchIoException(FileOperation fileOperation,
                                          String successMessage) throws AlterEgoException {
        try {
            fileOperation.execute();
            return successMessage;
        } catch (IOException e) {
            throw new AlterEgoException("Error: IO exception");
        }
    }
}
