import java.util.HashMap;
import java.util.ArrayList;

public class TasksManager {
    private HashMap<Integer, Task> tasksList = new HashMap<>();

    public void addTask(Task task) {
        tasksList.put(task.getId(), task);
    }

    public HashMap<Integer, Task> getTasksList() {
        return tasksList;
    }

    public ArrayList<Task> getAllTasksList() {
        ArrayList<Task> allTasksList = new ArrayList<>();


        for (Task task : tasksList.values()) {
            if (task.getClass().getName().equals("Epic")) {
                ArrayList<SubTask> subTasks = ((Epic) task).getSubTasks();
                allTasksList.addAll(subTasks);
            } else {
                allTasksList.add(task);
            }
        }

        return allTasksList;
    }

    public void deleteAllTasks() {
        tasksList.clear();
    }

//    public Task getById (int id) {
//
//    }

    public void createTask (String name, String description) {

    }
}
