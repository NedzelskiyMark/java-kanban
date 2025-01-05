package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest {
    private static TaskManager tasksManager = Managers.getDefault();
    private static List<Integer> rightIds = new ArrayList<>();

    @BeforeAll
    public static void prepare() {
        Task newTask = new Task("Task name", "Task description");
        Epic newEpic = new Epic("Epic name", "Epic description");
        SubTask newSubtask = new SubTask("Subtask name", "Subtask description", 1, 0);
        newEpic.addSubTaskIdToEpic(newSubtask);
        rightIds.add(newTask.getId());
        rightIds.add(newEpic.getId());
        rightIds.add(newSubtask.getId());


        tasksManager.addTaskToList(newTask);
        tasksManager.addEpicToList(newEpic);
        tasksManager.addSubTaskToList(newSubtask);
    }

    @Test
    public void addDifferentTypesOfTasksToTaskManager() {
        Task taskForCheck1 = tasksManager.getTaskById(rightIds.get(0));
        Task taskForCheck2 = tasksManager.getTaskById(rightIds.get(1));
        Task taskForCheck3 = tasksManager.getTaskById(rightIds.get(2));

        assertEquals("model.Task", taskForCheck1.getClass().getName());
        assertEquals("model.Epic", taskForCheck2.getClass().getName());
        assertEquals("model.SubTask", taskForCheck3.getClass().getName());
    }

    @Test
    public void taskManagerCanFindTaskById() {
        Task findedTask = tasksManager.getTaskById(rightIds.get(0));
        Task findedEpic = tasksManager.getTaskById(rightIds.get(1));
        Task findedSubtask = tasksManager.getTaskById(rightIds.get(2));

        assertEquals("Task name", findedTask.getName());
        assertEquals("Epic name", findedEpic.getName());
        assertEquals("Subtask name", findedSubtask.getName());
    }

    @Test
    public void taskNotChangedAfterAddingInManager() {
        Task newTask = new Task("Task name", "Task description");

        tasksManager.addTaskToList(newTask);
        Integer rightTaskId = newTask.getId();

        Task findedTask = tasksManager.getTaskById(rightTaskId);

        assertEquals(newTask.getId(), findedTask.getId());
        assertEquals(newTask.getName(), findedTask.getName());
        assertEquals(newTask.getDescription(), findedTask.getDescription());
        assertEquals(newTask.getTaskStatus(), findedTask.getTaskStatus());
    }

    @Test
    public void prioritizedTasksInRightOrder() {
        Task newTask = new Task("Test", "first", 0, 15);
        Task newTask2 = new Task("Test", "last", 0, 15);
        SubTask newSubtask = new SubTask("test", "middle", 0, 15);

        tasksManager.addTaskToList(newTask);
        tasksManager.addTaskToList(newTask2);
        tasksManager.addSubTaskToList(newSubtask);

        Integer rightIdTask1 = newTask.getId();
        Integer rightIdTask2 = newTask2.getId();
        Integer rightIdSubtask = newSubtask.getId();

        tasksManager.setStartTimeToTask(newTask, LocalDateTime.of(2025, 01, 05, 12, 0));
        tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 01, 05, 12, 2));
        tasksManager.setStartTimeToTask(newSubtask, LocalDateTime.of(2025, 01, 05, 12, 1));

        System.out.println(tasksManager.getPrioritizedTasks());


    }
}