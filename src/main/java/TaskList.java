import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    public TaskList(Storage storage) {
        this.tasks = storage.loadTasks();
        this.storage = storage;
    }

    public void addToDo(String taskName) {
        Task newTask = new ToDo(taskName);
        tasks.add(newTask);
        storage.addNewTask(newTask);
        System.out.println(CommonWords.LINE + " Got it. I've added this task:\n  "
                + newTask + "\n" + " now you have " + tasks.size()
                + " tasks in the list.\n" + CommonWords.LINE);
    }

    public void addDeadline(String taskName, String date) {
        Task newTask = new Deadline(taskName, date);
        tasks.add(newTask);
        storage.addNewTask(newTask);
        System.out.println(CommonWords.LINE + " Got it. I've added this task:\n  "
                + newTask + "\n" + " now you have " + tasks.size()
                + " tasks in the list.\n" + CommonWords.LINE);
    }

    public void addEvent(String taskName, String fromDate, String toDate) {
        Task newTask = new Event(taskName, fromDate, toDate);
        tasks.add(newTask);
        storage.addNewTask(newTask);
        System.out.println(CommonWords.LINE + " Got it. I've added this task:\n  "
                + newTask + "\n" + " Now you have " + tasks.size()
                + " tasks in the list.\n" + CommonWords.LINE);
    }

    public void enumList() {
        System.out.print(CommonWords.LINE);
        if (tasks.isEmpty()) {
            System.out.print("No task. You're free to play. Yippie!\n");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task currTask = tasks.get(i);
                System.out.print(" " + (i + 1) + "." + currTask + "\n");
            }
        }
        System.out.println(CommonWords.LINE);
    }

    public void mark(int taskNumber) throws InvalidInputException {
        if (taskNumber > tasks.size()) {
            throw new InvalidInputException(" There's only " + tasks.size() + " tasks here!");
        }
        System.out.println(CommonWords.LINE + " Nice! I've marked this task as done:");
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setDone();
        storage.rewriteFile(tasks);
        System.out.println("  " + currTask);
        System.out.println(CommonWords.LINE);
    }

    public void unmark(int taskNumber) throws InvalidInputException {
        if (taskNumber > tasks.size()) {
            throw new InvalidInputException(" There's only " + tasks.size() + " tasks here!");
        }
        System.out.println(CommonWords.LINE + " OK, I've marked this task as not done yet:");
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setUndone();
        storage.rewriteFile(tasks);
        System.out.println("  " + currTask);
        System.out.println(CommonWords.LINE);
    }

    public void delete(int taskNumber) throws InvalidInputException {
        if (taskNumber > tasks.size()) {
            throw new InvalidInputException(" There's only " + tasks.size() + " tasks here!");
        }
        System.out.println(CommonWords.LINE + " Noted. I've removed this task:");
        Task removedTask = tasks.remove(taskNumber - 1);
        storage.rewriteFile(tasks);
        System.out.println("  " + removedTask + "\n" + " Now you have " + tasks.size()
                + " tasks in the list.\n" + CommonWords.LINE);
    }

    public void clear() {
        tasks = new ArrayList<Task>();
        storage.clear();
    }

}
