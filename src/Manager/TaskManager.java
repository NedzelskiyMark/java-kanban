package Manager;

import Model.Epic;
import Model.SubTask;
import Model.Task;

import java.util.Collection;
import java.util.Map;

public interface TaskManager {
    Collection<Task> getAllTasksList();

    void deleteAllTasks();

    Task getTaskById(int idToFind);

    void addTaskToList(Task newTask);

    void addEpicToList(Epic newEpic);

    void addSubTaskToList(SubTask newSubtask);

    void updateTask(int id, Task updatedTask);

    void checkAndSetEpicStatus(int epicId);

    void deleteById(int idToRemove);

    void removeSubtasksOfEpic(int id);

    Collection<SubTask> getAllSubtaskOfEpic(int id);

    Map<Integer, Task> getHistory();
}
