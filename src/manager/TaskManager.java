package manager;

import model.Epic;
import model.IllegalStartTimeException;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    List<Task> getAllTasksList();

    void deleteAllTasks();

    Task getTaskById(int idToFind);

    void addTaskToList(Task newTask);

    void addEpicToList(Epic newEpic);

    void addSubTaskToList(SubTask newSubtask);

    void updateTask(Task updatedTask);

    void checkAndSetEpicStatus(int epicId);

    void deleteById(int idToRemove);

    void removeSubtasksOfEpic(int id);

    List<SubTask> getAllSubtaskOfEpic(int id);

    List<Task> getHistory();

    void removeTaskFromHistoryList(int id);

    void setStartTimeToTask(Task task, LocalDateTime startTime) throws IllegalStartTimeException;

    TreeSet<Task> getPrioritizedTasks();
}
