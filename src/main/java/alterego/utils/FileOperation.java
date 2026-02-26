package alterego.utils;

import java.io.IOException;

@FunctionalInterface
public interface FileOperation {
    void execute() throws IOException;
}
