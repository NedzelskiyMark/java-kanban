import java.util.ArrayList;
public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        ArrayList<SubTask> sub = new ArrayList<>();
        sub.add(new SubTask("имя 1", "описание 1"));
        sub.add(new SubTask("имя 2", "описание 2"));
        sub.add(new SubTask("имя 3", "описание 3"));
        tasksManager.createTask("имя эпика", "описание эпика", sub);


        tasksManager.createTask("task name", "task description");
        tasksManager.createTask("task name2", "task description2");


        System.out.println(tasksManager.getAllTasksList());

        System.out.println(tasksManager.getById(9));
    }
}
