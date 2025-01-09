package manager;

import model.Epic;
import model.IllegalStartTimeException;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest {
    private static TaskManager tasksManager;
    private static List<Task> tasksList = new ArrayList<>();

    @BeforeAll
    public static void beforeAll() {
        Task newTask = new Task("Task name", "Task description", 0, 0);
        Epic newEpic = new Epic("Epic name", "Epic description");
        SubTask newSubtask = new SubTask("Subtask name", "Subtask description", 0, 0);

        tasksList.add(newTask);
        tasksList.add(newEpic);
        tasksList.add(newSubtask);
    }

    @BeforeEach
    public void beforeEach() {
        tasksManager = Managers.getDefault();
    }

    @Test
    public void addDifferentTypesOfTasksToTaskManagerAndFindById() {
        tasksManager.addTaskToList(tasksList.get(0));
        tasksManager.addEpicToList((Epic) tasksList.get(1));
        tasksManager.addSubTaskToList((SubTask) tasksList.get(2));

        Task taskForCheck1 = tasksManager.getTaskById(tasksList.get(0).getId());
        Task taskForCheck2 = tasksManager.getTaskById(tasksList.get(1).getId());
        Task taskForCheck3 = tasksManager.getTaskById(tasksList.get(2).getId());

        assertEquals("model.Task", taskForCheck1.getClass().getName());
        assertEquals("model.Epic", taskForCheck2.getClass().getName());
        assertEquals("model.SubTask", taskForCheck3.getClass().getName());
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

    // Новые тесты спринта 8
    @Test
    public void prioritizedTasksInRightOrder() throws IllegalStartTimeException {
        Task newTask = new Task("Test", "first", 0, 15);
        Task newTask2 = new Task("Test", "last", 0, 15);
        SubTask newSubtask = new SubTask("test", "middle", 0, 15);

        tasksManager.addTaskToList(newTask);
        tasksManager.addTaskToList(newTask2);
        tasksManager.addSubTaskToList(newSubtask);

        tasksManager.setStartTimeToTask(newTask, LocalDateTime.of(2025, 1, 5, 12, 0));
        tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 5, 13, 0));
        tasksManager.setStartTimeToTask(newSubtask, LocalDateTime.of(2025, 1, 5, 12, 30));

        List<Task> expectedList = List.of(newTask, newSubtask, newTask2);
        List<Task> listPrioritizedTasksFromManager = tasksManager.getPrioritizedTasks().stream().toList();

        assertEquals(expectedList, listPrioritizedTasksFromManager);
    }

    @Test
    public void startTimeValidation() throws IllegalStartTimeException {
        Task newTask = new Task("Test", "first", 0, 15);
        Task newTask2 = new Task("Test", "last", 0, 15);
        SubTask newSubtask = new SubTask("test", "middle", 0, 15);

        tasksManager.setStartTimeToTask(newTask, LocalDateTime.of(2025, 1, 7, 12, 0));

        //attempt to set same startTime
        assertThrows(IllegalStartTimeException.class, () ->
                tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 7, 12, 0)));

        //attempt to set startTime same with endTime for another task
        assertThrows(IllegalStartTimeException.class, () ->
                tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 7, 12, 15)));

        //attempt to set startTime 1 after before startTime of another task
        assertDoesNotThrow(() ->
                tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 7, 12, 16)));

        //attempt to set startTime 1 minute before startTime of another task (endTime will not support)
        assertThrows(IllegalStartTimeException.class, () ->
                tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 7, 11, 59)));

        //attempt to set startTime in the middle of another task
        assertThrows(IllegalStartTimeException.class, () ->
                tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 7, 12, 7)));

        //attempt to set startTime 15 minute before startTime of another task (endTime will be same as startTime of another Task)
        assertThrows(IllegalStartTimeException.class, () ->
                tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 7, 11, 45)));

        //attempt to set startTime 16 minute before startTime of another task
        assertDoesNotThrow(() ->
                tasksManager.setStartTimeToTask(newTask2, LocalDateTime.of(2025, 1, 7, 11, 44)));
    }

    @Test
    public void epicStatusTests() {
        Epic epic = new Epic("Epic name", "Epic description");
        tasksManager.addEpicToList(epic);
        SubTask firstSubtask = new SubTask("First subtask", "Subtask description", 1, 30);
        SubTask secondSubtask = new SubTask("Second subtask", "Subtask description", 0, 30);
        SubTask thirdSubtask = new SubTask("Third subtask", "Subtask description", 0, 45);

        epic.addSubTaskIdToEpic(firstSubtask);
        epic.addSubTaskIdToEpic(secondSubtask);
        epic.addSubTaskIdToEpic(thirdSubtask);
        tasksManager.addSubTaskToList(firstSubtask);
        tasksManager.addSubTaskToList(secondSubtask);
        tasksManager.addSubTaskToList(thirdSubtask);

        //all subtasks are NEW
        assertEquals(epic.getId() + ",EPIC,Epic name,NEW,Epic description,0,2,45,null\n", epic.toString());
        //subtask is IN_PROGRESS
        tasksManager.updateTask(firstSubtask);
        assertEquals(epic.getId() + ",EPIC,Epic name,IN_PROGRESS,Epic description,0,2,45,null\n", epic.toString());
        //some subtasks are NEW some is DONE
        tasksManager.updateTask(firstSubtask);
        assertEquals(epic.getId() + ",EPIC,Epic name,IN_PROGRESS,Epic description,0,1,15,null\n", epic.toString());
        //delete DONE subtask, all others subtasks is IN_PROGRESS
        tasksManager.deleteById(firstSubtask.getId());
        tasksManager.updateTask(secondSubtask);
        tasksManager.updateTask(thirdSubtask);
        assertEquals(epic.getId() + ",EPIC,Epic name,IN_PROGRESS,Epic description,0,1,15,null\n", epic.toString());
        //all subtasks are DONE
        tasksManager.updateTask(secondSubtask);
        tasksManager.updateTask(thirdSubtask);
        assertEquals(epic.getId() + ",EPIC,Epic name,DONE,Epic description,0,0,0,null\n", epic.toString());
    }

    @Test
    public void illegalStartTimeException() {
        Task task1 = new Task("Name", "Description", 0, 30);
        Task task2 = new Task("Name", "Description", 0, 30);

        tasksManager.addTaskToList(task1);
        tasksManager.addTaskToList(task2);

        assertThrows(IllegalStartTimeException.class, () -> {
            tasksManager.setStartTimeToTask(task1, LocalDateTime.of(2025, 1, 7, 12, 0));
            tasksManager.setStartTimeToTask(task2, LocalDateTime.of(2025, 1, 7, 12, 15));
        });

    }
}