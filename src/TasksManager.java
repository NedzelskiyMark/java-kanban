import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

public class TasksManager {
    //this hashmap will include Task and Epic tasks, inside Epic will be HashMap with Subtasks
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
        if (tasksList.containsKey(idToFind)) {
            return tasksList.get(idToFind);
        }

        for (Task taskToCheck: tasksList.values()) {
            if (taskToCheck.getClass().getName().equals("Epic")) {
                Epic convertedTaskToCheck = (Epic) taskToCheck;
                if (convertedTaskToCheck.getSubTasks().containsKey(idToFind)) {
                    return convertedTaskToCheck.getSubTasks().get(idToFind);
                }
            }
        }
        //must find better option than return null
        return null;
    }

    public void addTask(Task newTask) {
        tasksList.put(newTask.getId(), newTask);
    }
    //как я понял метод обновления задачи нужен для обновления статуса задачи, а не его имени, описания и т.д.
    public void updateTask(int id, Task updatedTask) {
        switch (updatedTask.getTaskStatus()) {
            case NEW:
                updatedTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                updatedTask.setTaskStatus(TaskStatus.DONE);
        }

        if (updatedTask.getClass().getName().equals("SubTask")) {
            SubTask updatedTaskCopy = (SubTask) updatedTask;
            Epic relatedEpic =(Epic) tasksList.get(updatedTaskCopy.getRelationEpicId());
            relatedEpic.getSubTasks().put(id, updatedTaskCopy);
            relatedEpic.checkEpicStatus();
            return;
        }

        tasksList.put(id, updatedTask);

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

    public Collection<SubTask> getAllSubtaskOfEpic(int id) {
        Epic epicTask = (Epic) getTaskById(id);
        return epicTask.getSubTasks().values();
    }

/*
Изначально мне казалось что коллекция HashMap<Integer, Task> неплохая идея, более универсально, а там где надо,
я приведу типы объектов к необходимым. Теперь я не уверен какой подход был бы более оптимальным, тот что я выбрал
или было лучше писать больше кода, коллекции для каждого типа задач, но его было бы на порядок проще читать..
 */




}
