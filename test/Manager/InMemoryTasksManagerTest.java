package Manager;

import Model.Epic;
import Model.SubTask;
import Model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest {
    private static TaskManager tasksManager = Managers.getDefault();
    private Random random = new Random();

    @BeforeAll
    public static void prepare() {
        Task newTask = new Task("Task name", "Task description");
        Epic newEpic = new Epic("Epic name", "Epic description");
        SubTask newSubtask = new SubTask("Subtask name", "Subtask description");
        newEpic.addSubTaskIdToEpic(newSubtask);

        tasksManager.addTaskToList(newTask);
        tasksManager.addEpicToList(newEpic);
        tasksManager.addSubTaskToList(newSubtask);
    }

    @Test
    public void addDifferentTypesOfTasksToTaskManager() {
        Task taskForCheck1 = tasksManager.getTaskById(1);
        Task taskForCheck2 = tasksManager.getTaskById(2);
        Task taskForCheck3 = tasksManager.getTaskById(3);

        assertEquals("Model.Task", taskForCheck1.getClass().getName());
        assertEquals("Model.Epic", taskForCheck2.getClass().getName());
        assertEquals("Model.SubTask", taskForCheck3.getClass().getName());
    }

    @Test
    public void taskManagerCanFindTaskById() {
        Task findedTask = tasksManager.getTaskById(1);
        Task findedEpic = tasksManager.getTaskById(2);
        Task findedSubtask = tasksManager.getTaskById(3);

        assertEquals("Task name", findedTask.getName());
        assertEquals("Epic name", findedEpic.getName());
        assertEquals("Subtask name", findedSubtask.getName());
    }

    @Test
    public void historyListHaveLimitedSizeTo10() {
        for (int i = 0; i < 20; i++) {
            tasksManager.getTaskById(random.nextInt(3));
        }

        assertEquals(10, tasksManager.getHistory().size());
    }

    @Test
    public void taskNotChangedAfterAddingInManager() {
        Task newTask = new Task("Task name", "Task description", 555);

        tasksManager.addTaskToList(newTask);

        Task findedTask = tasksManager.getTaskById(555);

        assertEquals(newTask.getId(), findedTask.getId());
        assertEquals(newTask.getName(), findedTask.getName());
        assertEquals(newTask.getDescription(), findedTask.getDescription());
        assertEquals(newTask.getTaskStatus(), findedTask.getTaskStatus());
    }
}