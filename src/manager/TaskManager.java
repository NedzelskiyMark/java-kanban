package manager;

import model.Epic;
import model.ManagerSaveException;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasksList();

    void deleteAllTasks() throws ManagerSaveException;

    Task getTaskById(int idToFind);

    void addTaskToList(Task newTask) throws ManagerSaveException;

    void addEpicToList(Epic newEpic) throws ManagerSaveException;

    void addSubTaskToList(SubTask newSubtask) throws ManagerSaveException;

    void updateTask(Task updatedTask) throws ManagerSaveException;

    void checkAndSetEpicStatus(int epicId);

    void deleteById(int idToRemove) throws ManagerSaveException;

    void removeSubtasksOfEpic(int id) throws ManagerSaveException;

    List<SubTask> getAllSubtaskOfEpic(int id);

    List<Task> getHistory();

    public void removeTaskFromHistoryList(int id);
}
