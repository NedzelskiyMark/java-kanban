package manager;

import model.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTasksManager implements TaskManager {
    private Map<Integer, Task> tasksMap = new HashMap<>();
    private Map<Integer, Epic> epicsMap = new HashMap<>();
    private Map<Integer, SubTask> subtasksMap = new HashMap<>();

    private Collection<String> idInUse = new ArrayList<>();
    /*
    Я решил что не буду добавлять в приоритизированные задачи Эпики, т.к. они сам по себе являются
    * "абстрактными", а в списке оставить именно те задачи, которые надо непосредственно выполнять. Если это не
    правильный подход, я его изменю:)
    */
    private Set<Task> prioritizedTasks = new TreeSet<>(new TaskComparatorByStartTime());

    private HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public List<Task> getAllTasksList() {
        List<Task> allTasksList = new ArrayList<>();

        tasksMap.values().forEach(allTasksList::add);


        //немного усложнил, хотел сделать чтобы в списке всех задач был порядок,
        // сначала добавляется эпик, затем его подзадачи, затем следующий эпик и т.д.

        epicsMap.values().forEach(task -> {
                    allTasksList.add(task);
                    getAllSubtaskOfEpic(task.getId()).forEach(allTasksList::add);
                });

        return allTasksList;
    }

    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
        epicsMap.clear();
        subtasksMap.clear();
        historyManager.clearHistoryList();
        prioritizedTasks.clear();
    }

    @Override
    public Task getTaskById(int idToFind) {
        Task foundTask;
        if (tasksMap.containsKey(idToFind)) {
            foundTask = tasksMap.get(idToFind);
            historyManager.add(foundTask);
            return foundTask;
        }

        if (epicsMap.containsKey(idToFind)) {
            foundTask = epicsMap.get(idToFind);
            historyManager.add(foundTask);
            return foundTask;
        }

        foundTask = subtasksMap.get(idToFind);
        historyManager.add(foundTask);
        return foundTask;
    }

    @Override
    public void addTaskToList(Task newTask) {
        tasksMap.put(newTask.getId(), newTask);
        idInUse.add(String.valueOf(newTask.getId()));
        addTaskToPrioritizedList(newTask);
    }

    @Override
    public void addEpicToList(Epic newEpic) {
        epicsMap.put(newEpic.getId(), newEpic);
        idInUse.add(String.valueOf(newEpic.getId()));
    }

    @Override
    public void addSubTaskToList(SubTask newSubtask) {
        subtasksMap.put(newSubtask.getId(), newSubtask);
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
            Epic relatedEpic = epicsMap.get(updatedTaskCopy.getRelationEpicId());
            checkAndSetEpicStatus(relatedEpic.getId());//добавил метод, чтобы разгрузить действующий метод

            if (updatedTask.getTaskStatus() == TaskStatus.DONE) {
                relatedEpic.minusDuration(updatedTaskCopy.getDuration());
            }
        }
    }

    @Override
    public void checkAndSetEpicStatus(int epicId) {
        boolean isThereNotDoneSubtasks = subtasksMap.values().stream()
                .filter(subtask -> subtask.getRelationEpicId() == epicId)
                .anyMatch(subtask -> (subtask.getTaskStatus().equals(TaskStatus.NEW) ||
                        subtask.getTaskStatus().equals(TaskStatus.IN_PROGRESS)));

        if (isThereNotDoneSubtasks) {
            epicsMap.get(epicId).setTaskStatus(TaskStatus.IN_PROGRESS);
        } else {
            epicsMap.get(epicId).setTaskStatus(TaskStatus.DONE);
        }
    }

    @Override
    public void deleteById(int idToRemove) {
        prioritizedTasks.remove(getTaskById(idToRemove));

        if (tasksMap.containsKey(idToRemove)) {
            tasksMap.remove(idToRemove);
            idInUse.remove(String.valueOf(idToRemove));
        }

        if (subtasksMap.containsKey(idToRemove)) {
            subtasksMap.remove(idToRemove);
            idInUse.remove(String.valueOf(idToRemove));
        }
        //если удаляется эпик, удаляются все его подзадачи
        if (epicsMap.containsKey(idToRemove)) {
            epicsMap.remove(idToRemove);
            idInUse.remove(String.valueOf(idToRemove));
            removeSubtasksOfEpic(idToRemove);//добавил метод, чтобы разгрузить действующий метод
        }


        removeTaskFromHistoryList(idToRemove);
    }

    @Override
    public void removeSubtasksOfEpic(int id) {
        List<SubTask> subtasksToRemove = getAllSubtaskOfEpic(id);

        Map<Integer, SubTask> filteredMap = subtasksMap.entrySet().stream()
                .filter(entry -> !subtasksToRemove.contains(entry.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        subtasksMap = filteredMap;
    }

    @Override
    public List<SubTask> getAllSubtaskOfEpic(int id) {
        return subtasksMap.values().stream()
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
        LocalDateTime startTimeOfTask = task.getStartTime();

        if (startTimeOfTask != null) {
            prioritizedTasks.add(task);
        }
    }

    //метод проверяет пересечения как по стартовому времени так и по времени окончания задания, стрим фильтрует
    //приоритизированный список, оставляя только задачи, где есть пересечения, и возвращает булево значение пустой ли
    //выходит список после фильтрации
    private boolean checkTasksTimeIntersections(LocalDateTime startToVerify, LocalDateTime endToVerify) {
        boolean isThereIntersectionPresence = prioritizedTasks.stream()
                .anyMatch(task -> {
                    LocalDateTime taskStartTime = task.getStartTime();
                    LocalDateTime taskEndTime = task.getEndTime();

                    return startToVerify.isEqual(taskStartTime) || startToVerify.isEqual(taskEndTime) ||
                            endToVerify.isEqual(taskStartTime) || endToVerify.isEqual(taskEndTime) ||
                            startToVerify.isAfter(taskStartTime) && startToVerify.isBefore(taskEndTime) ||
                            endToVerify.isAfter(taskStartTime) && endToVerify.isBefore(taskEndTime);
                });

        return !isThereIntersectionPresence;
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}


