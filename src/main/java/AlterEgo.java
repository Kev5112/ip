public class AlterEgo {

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    public AlterEgo(String filePath) {
        storage = new Storage(filePath);
        taskList = new TaskList(storage.loadTasks(), storage);
        ui = new Ui();
    }

    public void run() {
        Ui.hello();
        boolean isExit = false;
        while(!isExit) {
            String input = ui.readCommand();
            try {
                Parser parser = new Parser();
                parser.execute(input, taskList);
                isExit = parser.isExit();
            } catch (AlterEgoException e) {
                Ui.show(e.getMessage());
            }
        }
        Ui.bye();
    }

    public static void main(String[] args) {
        AlterEgo chatbot = new AlterEgo("./data/AlterEgo.txt");

        chatbot.run();
    }

}
