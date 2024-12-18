package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

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
}
