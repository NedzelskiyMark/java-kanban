package Manager;

import Model.Epic;
import Model.SubTask;
import Model.Task;
import Model.TaskStatus;

import java.util.HashMap;
import java.util.ArrayList;

/*
Из предложенных тобой вариантов (класс-обертка для списков задач или нескольких хэшмепов для них), выбрал хэшмепы,
т.к. вроде понимаю как реализовать класс-обертку, но не стал усложнять код, т.к. действительно не представляю как
это скажется на дальнейшем развитии проекта. Спасибо за помощь!:)
 */
public class TasksManager {
    //времена действительно страдают у меня в английском, пока что)
    private HashMap<Integer, Task> tasksList = new HashMap<>();
    private HashMap<Integer, Epic> epicsList = new HashMap<>();
    private HashMap<Integer, SubTask> subtasksList = new HashMap<>();


    public ArrayList<Task> getAllTasksList() {
        ArrayList<Task> allTasksList = new ArrayList<>();
        allTasksList.addAll(tasksList.values());

        //немного усложнил, хотел сделать чтобы в списке всех задач был порядок,
        // сначала добавляется эпик, затем его подзадачи, затем следующий эпик и т.д.
        for (Epic epic : epicsList.values()) {
            allTasksList.add(epic);
            int epicId = epic.getId();
            ArrayList<SubTask> subtaskListForCopy = getAllSubtaskOfEpic(epicId);
            allTasksList.addAll(subtaskListForCopy);
        }

        return allTasksList;
    }

    public void deleteAllTasks() {
        tasksList.clear();
        epicsList.clear();
        subtasksList.clear();
    }

    public Task getTaskById(int idToFind) {
        if (tasksList.containsKey(idToFind)) {
            return tasksList.get(idToFind);
        }

        if (epicsList.containsKey(idToFind)) {
            return epicsList.get(idToFind);
        }

        if (subtasksList.containsKey(idToFind)) {
            return subtasksList.get(idToFind);
        }

        return null;
    }

    public void addTaskToList(Task newTask) {
        tasksList.put(newTask.getId(), newTask);
    }

    public void addEpicToList(Epic newEpic) {
        epicsList.put(newEpic.getId(), newEpic);
    }

    public void addSubTaskToList(SubTask newSubtask) {
        subtasksList.put(newSubtask.getId(), newSubtask);
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

        if (updatedTask.getClass().getName().equals("Model.SubTask")) {
            SubTask updatedTaskCopy = (SubTask) updatedTask;
            Epic relatedEpic = epicsList.get(updatedTaskCopy.getRelationEpicId());
            checkAndSetEpicStatus(relatedEpic.getId());//добавил метод, чтобы разгрузить действующий метод
            return;
        }

        tasksList.put(id, updatedTask);

    }

    public void checkAndSetEpicStatus(int epicId) {
        boolean isAllSubtasksDone = true;

        for (SubTask subtask : subtasksList.values()) {
            if (subtask.getRelationEpicId() == epicId) {
                if (subtask.getTaskStatus().equals(TaskStatus.NEW) ||
                        subtask.getTaskStatus().equals(TaskStatus.IN_PROGRESS)) {
                    isAllSubtasksDone = false;
                    break;
                }
            }
        }
        //if all SubTasks is DONE, Model.Epic is DONE, else Model.Epic IN_PROGRESS because this method use only when
        // Model.SubTask status is updated
        if (isAllSubtasksDone) {
            epicsList.get(epicId).setTaskStatus(TaskStatus.DONE);
        } else {
            epicsList.get(epicId).setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void deleteById(int idToRemove) {
        if (tasksList.containsKey(idToRemove)) {
            tasksList.remove(idToRemove);
        }

        if (subtasksList.containsKey(idToRemove)) {
            subtasksList.remove(idToRemove);
        }
        //если удаляется эпик, удаляются все его подзадачи
        if (epicsList.containsKey(idToRemove)) {
            epicsList.remove(idToRemove);
            removeSubtasksOfEpic(idToRemove);//добавил метод, чтобы разгрузить действующий метод
        }
    }

    public void removeSubtasksOfEpic(int id) {
        //создаю список, куда положу id подзадач для удаления, т.к. в foreach нельзя редактировать список, в цикле for
        // возникала ошибка, этот способ показался оптимальным из всех что я придумал)
        ArrayList<Integer> idSubtasksToRemove = new ArrayList<>();

        for (SubTask subtaskToCheck : subtasksList.values()) {
            if (subtaskToCheck.getRelationEpicId() == id) {
                idSubtasksToRemove.add(subtaskToCheck.getId());
            }
        }

        for (Integer idToRemove : idSubtasksToRemove) {
            subtasksList.remove(idToRemove);
        }
    }

    public ArrayList<SubTask> getAllSubtaskOfEpic(int id) {
        ArrayList<SubTask> epicRelatedSubtasks = new ArrayList<>();
        for (SubTask subtask : subtasksList.values()) {
            if (subtask.getRelationEpicId() == id) {
                epicRelatedSubtasks.add(subtask);
            }
        }

        return epicRelatedSubtasks;
    }
}
