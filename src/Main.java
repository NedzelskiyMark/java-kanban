import java.util.ArrayList;
public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        ArrayList<SubTask> sub = new ArrayList<>();
        sub.add(new SubTask("имя 1", "описание 1"));
        sub.add(new SubTask("имя 2", "описание 2"));
        sub.add(new SubTask("имя 3", "описание 3"));
        Epic epic = new Epic("имя эпика", "описание эпика", sub);

        tasksManager.addTask(epic);

        Task task = new Task("task name", "task description");
        Task task2 = new Task("task name2", "task description2");

        tasksManager.addTask(task);
        tasksManager.addTask(task2);

        System.out.println(tasksManager.getAllTasksList());
    }
}
