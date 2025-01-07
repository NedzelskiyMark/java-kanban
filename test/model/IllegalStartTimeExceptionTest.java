package model;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class IllegalStartTimeExceptionTest {
    private TaskManager tasksManager = Managers.getDefault();

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
