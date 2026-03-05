package alterego.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import alterego.data.Storable;
import alterego.list.DataList;
import alterego.utils.AlterEgoException;

/**
 * Represents abstract base class for storage operations of storable objects.
 * Provides common file handling functionality for reading and writing data.
 * @param <T> The type of storable objects managed by this storage, must implement Storable
 */
public abstract class Storage<T extends Storable> {
    private final String filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     * @param filePath The path to the file where data will be stored
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Ensures the parent directory exists before file operations.
     * Creates directories if they don't exist.
     * @throws AlterEgoException if directories cannot be created
     */
    protected void ensureDirectoryExists() throws AlterEgoException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new AlterEgoException("Failed to create file directory");
            }
        }
    }

    /**
     * Creates a FileWriter for the storage file.
     * @return FileWriter instance
     * @throws IOException if file cannot be opened or created
     */
    protected FileWriter createFileWriter() throws IOException {
        return new FileWriter(filePath);
    }

    /**
     * Creates a FileWriter for the storage file with specified append mode.
     * @param append If true, data will be appended to existing file; if false, file will be overwritten
     * @return FileWriter instance
     * @throws IOException if file cannot be opened or created
     */
    protected FileWriter createFileWriter(boolean append) throws IOException {
        return new FileWriter(filePath, append);
    }

    /**
     * Creates a File object representing the storage file.
     * @return File instance for the storage path
     */
    protected File createFile() {
        return new File(filePath);
    }

    /**
     * Rewrites storage file with current task list and its state.
     * @param storables list of tasks to save
     */
    public void rewriteFile(ArrayList<T> storables) throws IOException {
        assert storables != null : "List of tasks cannot be null";
        ensureDirectoryExists();
        FileWriter fw = createFileWriter();
        for (T storable : storables) {
            assert storable != null : "Task in list should not be null";
            fw.write(storable.toFileFormat() + System.lineSeparator());
        }
        fw.close();

        File f = createFile();
        assert f.exists() : "File should exist after writing";
        assert f.length() > 0 || storables.isEmpty()
                : "File should have content if tasks not empty";
    }

    /**
     * Loads data from the storage file into the provided DataList.
     * Lines that fail to parse are skipped and their errors are added to the load status.
     * @param dataList The DataList to populate with loaded items
     * @throws FileNotFoundException if the storage file does not exist
     */
    public void load(DataList<T> dataList) throws FileNotFoundException {
        File f = createFile();
        Scanner s = new Scanner(f);
        int lineCount = 0;
        while (s.hasNextLine()) {
            lineCount += 1;
            String line = s.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            T storable = null;
            try {
                storable = parseFromFile(line);
            } catch (AlterEgoException e) {
                dataList.addLoadStatus("Task storage problem: Problem with line "
                        + lineCount + ". " + e.getMessage() + "\n");
            }
            if (storable != null) {
                dataList.addItem(storable);
            }
        }
    }

    /**
     * Clears all contacts from storage file.
     * @throws IOException if file write operation fails
     */
    public void clear() throws IOException {
        ensureDirectoryExists();
        FileWriter fw = createFileWriter();
        fw.write("");
        fw.close();
    }

    /**
     * Parses a single line from the storage file into a storable object.
     * @param line The line read from the file
     * @return The parsed storable object
     * @throws AlterEgoException if the line format is invalid or cannot be parsed
     */
    public abstract T parseFromFile(String line) throws AlterEgoException;
}
