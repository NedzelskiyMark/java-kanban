package manager;

import model.*;

import java.time.LocalDateTime;
import java.util.*;


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
                .forEach(task -> {
                    allTasksList.add(task);
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
        prioritizedTasks.clear();
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
        addTaskToPrioritizedList(newTask);
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
        addTaskToPrioritizedList(newSubtask);
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
        List<SubTask> subtasksWithStatusNotDone = subtasksList.values().stream()
                .filter(subtask -> subtask.getRelationEpicId() == epicId &&
                        (subtask.getTaskStatus().equals(TaskStatus.NEW) ||
                                subtask.getTaskStatus().equals(TaskStatus.IN_PROGRESS)))
                .toList();

        boolean isAllSubtasksDone = subtasksWithStatusNotDone.isEmpty();

        if (isAllSubtasksDone) {
            epicsList.get(epicId).setTaskStatus(TaskStatus.DONE);
        } else {
            epicsList.get(epicId).setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public void deleteById(int idToRemove) {
        prioritizedTasks.remove(getTaskById(idToRemove));

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
        List<SubTask> idSubtasksToRemove = subtasksList.values().stream()
                .filter(subTask -> subTask.getRelationEpicId() == id)
                .toList();

        idSubtasksToRemove.stream().forEach(subTask -> subtasksList.remove(subTask.getId()));
    }

    @Override
    public List<SubTask> getAllSubtaskOfEpic(int id) {
        return subtasksList.values().stream()
                .filter(subTask -> subTask.getRelationEpicId() == id)
                .toList();
    }

    @Override
    public List<Task> getHistory() {

        return historyManager.getHistory();
    }

    @Override
    public void removeTaskFromHistoryList(int id) {
        historyManager.remove(id);
    }

    public void setStartTimeToTask(Task task, LocalDateTime startTime) throws IllegalStartTimeException {
        LocalDateTime endTime = startTime.plus(task.getDuration());
        boolean isStartTimeAccepted = checkTasksTimeIntersections(startTime, endTime);

        if (!isStartTimeAccepted) {
            throw new IllegalStartTimeException("Стартовое время не подходит, пересечение с другой задачей");
        }

        task.setStartTime(startTime);
        prioritizedTasks.remove(task);
        addTaskToPrioritizedList(task);
    }

    private void addTaskToPrioritizedList(Task task) {
        Optional<LocalDateTime> startTimeOfTask = Optional.ofNullable(task.getStartTime());

        if (startTimeOfTask.isPresent()) {
            prioritizedTasks.add(task);
        }
    }

    //метод проверяет пересечения как по стартовому времени так и по времени окончания задания, стрим фильтрует
    //приоритизированный список, оставляя только задачи, где есть пересечения, и возвращает булево значение пустой ли
    //выходит список после фильтрации
    private boolean checkTasksTimeIntersections(LocalDateTime startToVerify, LocalDateTime endToVerify) {
        List<Task> filteredByIntersectionPresence = prioritizedTasks.stream()
                .filter(task ->
                {
                    LocalDateTime taskStartTime = task.getStartTime();
                    LocalDateTime taskEndTime = task.getEndTime();

                    return startToVerify.isEqual(taskStartTime) || startToVerify.isEqual(taskEndTime) ||
                            endToVerify.isEqual(taskStartTime) || endToVerify.isEqual(taskEndTime) ||
                            startToVerify.isAfter(taskStartTime) && startToVerify.isBefore(taskEndTime) ||
                            endToVerify.isAfter(taskStartTime) && endToVerify.isBefore(taskEndTime);
                })
                .toList();

        return filteredByIntersectionPresence.isEmpty();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
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
