package alterego.utils;

import alterego.task.TaskList;

import java.io.IOException;

public class ExceptionCatcher {
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
