package manager;

import model.IllegalStartTimeException;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Спасибо за подсказку с параметризированными тестами, обязательно изучу, но пока решил попробовать разобраться хотя бы
 * с реализацией через абстрактный класс, иначе голова взорвется:) Почему-то мне очень трудно далось понять что от меня
 * требуется и как это воплотить. Результат того, до чего я дошел, сохранил в отдельные классы, где название начинается
 *  на "TZ8", написал пару тестов, чтобы уточнить, правильно ли я реализовал тестирование через абстрактный класс?
 * И правильно ли я понимаю, что в абстрактном классе я пишу тесты на общую функциональность для обоих менеджеров, а в
 * отдельном классе пишу дополнительные тесты (InMemory менеджер не может, например, создать файл)? Понимаю что иначе,
 * вроде бы, никак, но хотелось бы уточнить.. Извиняюсь за длинный вопрос
 * */

abstract class TZ8TaskManagerTest<T extends TaskManager> {
    T tasksManager;

    @Test
    public void addTaskToManagerAndGetByIdTest() {
        Task task = new Task("Task name", "Task Description", 1, 15);
        tasksManager.addTaskToList(task);
        assertEquals(task, tasksManager.getTaskById(task.getId()));
    }

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
}