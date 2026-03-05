package alterego.storage;

import alterego.data.Storable;
import alterego.list.DataList;
import alterego.utils.AlterEgoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Storage<T extends Storable> {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Ensures the parent directory exists before file operations.
     * Creates directories if they don't exist.
     * @throws IOException if directories cannot be created
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

    protected FileWriter createFileWriter() throws IOException {
        return new FileWriter(filePath);
    }

    protected FileWriter createFileWriter(boolean append) throws IOException {
        return new FileWriter(filePath, append);
    }

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

    public void load(DataList<T> dataList) throws FileNotFoundException, AlterEgoException {
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

    public abstract void clear() throws IOException;

    public abstract T parseFromFile(String line) throws AlterEgoException;
}
