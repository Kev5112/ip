import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addToDo(String taskName) {
        Task newTask = new ToDo(taskName);
        tasks.add(newTask);
        System.out.println(CommonWords.LINE + "Got it. I've added this task: \n "
                + newTask + "\n" + "now you have " + tasks.size()
                + " tasks in the list.\n" + CommonWords.LINE);
    }

    public void addDeadline(String taskName, String date) {
        Task newTask = new Deadline(taskName, date);
        tasks.add(newTask);
        System.out.println(CommonWords.LINE + "Got it. I've added this task: \n "
                + newTask + "\n" + "now you have " + tasks.size()
                + " tasks in the list.\n" + CommonWords.LINE);
    }

    public void addEvent(String taskName, String fromDate, String toDate) {
        Task newTask = new Event(taskName, fromDate, toDate);
        tasks.add(newTask);
        System.out.println(CommonWords.LINE + "Got it. I've added this task: \n "
                + newTask + "\n" + "now you have " + tasks.size()
                + " tasks in the list.\n" + CommonWords.LINE);
    }

    public void enumList() {
        System.out.println(CommonWords.LINE);
        if (tasks.isEmpty()) {
            System.out.print("No task. You're free to play. Yippie!\n");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task currTask = tasks.get(i);
                System.out.print((i + 1) + "." + currTask + "\n");
            }
        }
        System.out.println(CommonWords.LINE);
    }

    public void mark(int taskNumber) {
        System.out.println(CommonWords.LINE + "Nice! I've marked this task as done:");
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setDone();
        System.out.println(currTask);
        System.out.println(CommonWords.LINE);
    }

    public void unmark(int taskNumber) {
        System.out.println(CommonWords.LINE + "OK, I've marked this task as not done yet:");
        Task currTask = tasks.get(taskNumber - 1);
        currTask.setUndone();
        System.out.println(currTask);
        System.out.println(CommonWords.LINE);
    }

}
