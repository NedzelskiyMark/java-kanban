import java.util.HashMap;
import java.util.ArrayList;

public class TasksManager {
    private HashMap<Integer, Task> tasksList = new HashMap<>();

    public ArrayList<Task> getAllTasksList() {
        ArrayList<Task> allTasksList = new ArrayList<>();

        for (Task task : tasksList.values()) {
            allTasksList.add(task);
        }

        return allTasksList;
    }

    public void deleteAllTasks() {
        tasksList.clear();
    }

    public Task getTaskById(int idToFind) {
        for (Integer id : tasksList.keySet()) {
            if (id.equals(idToFind)) {
                return tasksList.get(id);
            }
            //if current task is Epic, we will search in it's SubTasks
            Task taskCopy = tasksList.get(id);
            if (taskCopy.getClass().getName().equals("Epic")) {
                ArrayList<SubTask> tempList = ((Epic) taskCopy).getSubTasks();
                for (SubTask subTaskFromEpic : tempList) {
                    if (subTaskFromEpic.getId().equals(idToFind)) {
                        return subTaskFromEpic;
                    }
                }
            }
        }
        //must find better option
        return null;
    }

    public void createTask(Task newTask) {
        tasksList.put(newTask.getId(), newTask);
    }
//наверное не понял сути обновления задачи
    public void updateTask(int id, Task task) {
        tasksList.put(id, task);
        switch (task.getTaskStatus()) {
            case NEW:
                task.setTaskStatus(TaskStatus.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                task.setTaskStatus(TaskStatus.DONE);
        }
    }

    public void deleteById(int id) {
        Task taskToDelete = getTaskById(id);
        if (taskToDelete.getClass().getName().equals("SubTask")) {
            Epic relativeEpicForTask = (Epic) tasksList.get(((SubTask) taskToDelete).getRelationEpicId());
            relativeEpicForTask.getSubTasks().remove(taskToDelete.getId());
            return;
        }

        tasksList.remove(id);
    }

    public ArrayList<SubTask> getAllSubtaskOfEpic(int id) {
        Epic epicTask = (Epic) getTaskById(id);
        return epicTask.getSubTasks();
    }

/*
Изначально мне казалось что коллекция HashMap<Integer, Task> неплохая идея, более универсально, а там где надо,
я приведу типы объектов к необходимым. Теперь я не уверен какой подход был бы более оптимальным, так как я сделал
или писать больше кода, для каждого типа задач, но его было бы на порядок проще читать..
 */




}
