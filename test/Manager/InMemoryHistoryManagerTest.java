package Manager;

import Model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private TaskManager tasksManager = Managers.getDefault();

    @Test
    public void addingTaskToHistoryManagerNotChangeTask() {
        Task task1 = new Task("Task name", "Task description");

        tasksManager.addTaskToList(task1);
        tasksManager.getTaskById(1);
        ArrayList<Task> tasksFromHistory = tasksManager.getHistory();

        Task taskFromHistory = tasksFromHistory.get(0);

        assertEquals(task1.getId(), taskFromHistory.getId());
        assertEquals(task1.getName(), taskFromHistory.getName());
        assertEquals(task1.getDescription(), taskFromHistory.getDescription());
        assertEquals(task1.getTaskStatus(), taskFromHistory.getTaskStatus());
    }
}