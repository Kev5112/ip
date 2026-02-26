package alterego.utils;

import java.io.IOException;

/**
 * Represents a file operation that can throw an IOException.
 */
@FunctionalInterface
public interface FileOperation {
    /**
     * Executes the file operation.
     * @throws IOException if an I/O error occurs
     */
    void execute() throws IOException;
}
