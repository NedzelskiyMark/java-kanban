package manager;

import model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTasksManager implements TaskManager {
    private Map<Integer, Task> tasksList = new HashMap<>();
    private Map<Integer, Epic> epicsList = new HashMap<>();
    private Map<Integer, SubTask> subtasksList = new HashMap<>();

    private Collection<String> idInUse = new ArrayList<>();
    /*
    Я решил что не буду добавлять в приоритизированные задачи Эпики, т.к. они сам по себе являются
    * "абстрактными", а в списке оставить именно те задачи, которые надо непосредственно выполнять. Если это не
    правильный подход, я его изменю:)
    */
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(new TaskComparatorByStartTime());

    private HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public List<Task> getAllTasksList() {
        List<Task> allTasksList = new ArrayList<>();

        tasksList.values().stream().forEach(allTasksList::add);


        //немного усложнил, хотел сделать чтобы в списке всех задач был порядок,
        // сначала добавляется эпик, затем его подзадачи, затем следующий эпик и т.д.

        epicsList.values().stream()
                .forEach(task -> { allTasksList.add(task);
                getAllSubtaskOfEpic(task.getId()).stream().forEach(allTasksList::add);
                });

        return allTasksList;
    }

    @Override
    public void deleteAllTasks() {
        tasksList.clear();
        epicsList.clear();
        subtasksList.clear();
        historyManager.clearHistoryList();
    }

    @Override
    public Task getTaskById(int idToFind) {
        Task foundTask;
        if (tasksList.containsKey(idToFind)) {
            foundTask = tasksList.get(idToFind);
            historyManager.add(foundTask);
            return foundTask;
        }

        if (epicsList.containsKey(idToFind)) {
            foundTask = epicsList.get(idToFind);
            historyManager.add(foundTask);
            return foundTask;
        }

        foundTask = subtasksList.get(idToFind);
        historyManager.add(foundTask);
        return foundTask;
    }

    @Override
    public void addTaskToList(Task newTask) {
        tasksList.put(newTask.getId(), newTask);
        idInUse.add(String.valueOf(newTask.getId()));
        addPrioritizedTask(newTask);
    }

    @Override
    public void addEpicToList(Epic newEpic) {
        epicsList.put(newEpic.getId(), newEpic);
        idInUse.add(String.valueOf(newEpic.getId()));
    }

    @Override
    public void addSubTaskToList(SubTask newSubtask) {
        subtasksList.put(newSubtask.getId(), newSubtask);
        idInUse.add(String.valueOf(newSubtask.getId()));
        addPrioritizedTask(newSubtask);
    }

    @Override
    public void updateTask(Task updatedTask) {
        switch (updatedTask.getTaskStatus()) {
            case NEW:
                updatedTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                updatedTask.setTaskStatus(TaskStatus.DONE);
        }

        if (updatedTask.getClass().getName().equals("model.SubTask")) {
            SubTask updatedTaskCopy = (SubTask) updatedTask;
            Epic relatedEpic = epicsList.get(updatedTaskCopy.getRelationEpicId());
            checkAndSetEpicStatus(relatedEpic.getId());//добавил метод, чтобы разгрузить действующий метод

            if (updatedTask.getTaskStatus() == TaskStatus.DONE) {
                relatedEpic.minusDuration(updatedTaskCopy.getDuration());
            }
        }
    }

    @Override
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

    @Override
    public void deleteById(int idToRemove) {
        if (tasksList.containsKey(idToRemove)) {
            tasksList.remove(idToRemove);
            idInUse.remove(String.valueOf(idToRemove));
        }

        if (subtasksList.containsKey(idToRemove)) {
            subtasksList.remove(idToRemove);
            idInUse.remove(String.valueOf(idToRemove));
        }
        //если удаляется эпик, удаляются все его подзадачи
        if (epicsList.containsKey(idToRemove)) {
            epicsList.remove(idToRemove);
            idInUse.remove(String.valueOf(idToRemove));
            removeSubtasksOfEpic(idToRemove);//добавил метод, чтобы разгрузить действующий метод
        }

        removeTaskFromHistoryList(idToRemove);
    }

    @Override
    public void removeSubtasksOfEpic(int id) {
        //Создаю список, куда положу id подзадач для удаления, т.к. В foreach нельзя редактировать список, в цикле for
        // возникала ошибка, этот способ показался оптимальным из всех что я придумал)
        ArrayList<Integer> idSubtasksToRemove = new ArrayList<>();

        for (SubTask subtaskToCheck : subtasksList.values()) {
            if (subtaskToCheck.getRelationEpicId() == id) {
                idSubtasksToRemove.add(subtaskToCheck.getId());
            }
        }

        for (Integer idToRemove : idSubtasksToRemove) {
            subtasksList.remove(idToRemove);
            idInUse.remove(String.valueOf(idToRemove));
            removeTaskFromHistoryList(idToRemove);
        }
    }

    @Override
    public List<SubTask> getAllSubtaskOfEpic(int id) {
        List<SubTask> epicRelatedSubtasks = new ArrayList<>();
        for (SubTask subtask : subtasksList.values()) {
            if (subtask.getRelationEpicId() == id) {
                epicRelatedSubtasks.add(subtask);
            }
        }

        return epicRelatedSubtasks;
    }

    @Override
    public List<Task> getHistory() {

        return historyManager.getHistory();
    }

    @Override
    public void removeTaskFromHistoryList(int id) {
        historyManager.remove(id);
    }

    private void addPrioritizedTask(Task task) {
        Optional<LocalDateTime> startTimeOfTask = Optional.ofNullable(task.getStartTime());

        if (startTimeOfTask.isPresent()) {
            prioritizedTasks.add(task);
        }
    }

    private boolean checkTasksTimeIntersections(LocalDateTime startTime) {
        TreeSet<Task> prioritizedTasks = getPrioritizedTasks();

        List<Task> filteredByPresenceIntersections = prioritizedTasks.stream()
                .filter(task -> (task.getStartTime().isEqual(startTime) ||
                        task.getStartTime().isBefore(startTime) && task.getEndTime().isAfter(startTime)))
                .toList();

        return filteredByPresenceIntersections.isEmpty();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void setStartTimeToTask(Task task,LocalDateTime startTime) throws IllegalStartTimeException {
        boolean isStartTimeAccepted = checkTasksTimeIntersections(startTime);

        if (!isStartTimeAccepted) {
            throw new IllegalStartTimeException("Стартовое время не подходит, пересечение с другой задачей");
        }

         task.setStartTime(startTime);
         addPrioritizedTask(task);
    }
}

class TaskComparatorByStartTime implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        LocalDateTime startTime1 = t1.getStartTime();
        LocalDateTime startTime2 = t2.getStartTime();

        if (startTime1.isBefore(startTime2)) {
            return -1;
        } else if (startTime1.isAfter(startTime2)) {
            return 1;
        } else {
            return 0;
        }
    }
}
