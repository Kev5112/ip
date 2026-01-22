import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(String task) {
        tasks.add(new Task(task));
        System.out.println(CommonWords.LINE + "added: " + task + "\n" + CommonWords.LINE);
    }

    public void enumList() {
        System.out.println(CommonWords.LINE);
        if (tasks.isEmpty()) {
            System.out.print("No task. You're free to play. Yippie!\n");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task currTask = tasks.get(i);
                System.out.print((i + 1) + "." + currTask.getCheckbox() + " " + currTask + "\n");
            }
        }
        System.out.println(CommonWords.LINE);
    }

    public void mark(int taskNumber) {
        System.out.println(CommonWords.LINE + "Nice! I've marked this task as done:");
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setDone();
        System.out.println(currTask.getCheckbox() + " " + currTask);
        System.out.println(CommonWords.LINE);
    }

    public void unmark(int taskNumber) {
        System.out.println(CommonWords.LINE + "OK, I've marked this task as not done yet:");
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setUndone();
        System.out.println(currTask.getCheckbox() + " " + currTask);
        System.out.println(CommonWords.LINE);
    }

}
