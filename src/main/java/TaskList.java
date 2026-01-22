import java.util.ArrayList;

public class TaskList {
    private ArrayList<String> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(String task) {
        tasks.add(task);
        System.out.println(CommonWords.LINE + "added: " + task + "\n" + CommonWords.LINE);
    }

    public void enumList() {
        System.out.println(CommonWords.LINE);
        if (tasks.isEmpty()) {
            System.out.print("No task. You're free to play. Yippie!\n");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.print((i + 1) + ". " + tasks.get(i) + "\n");
            }
        }
        System.out.println(CommonWords.LINE);
    }

}
