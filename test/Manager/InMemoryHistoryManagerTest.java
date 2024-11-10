package Manager;

import Model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    /*
     * Спасибо за замечание, понял что без нужды нагружал тесты созданием TaskManager, думал что лучше будет
     * воссоздать логику работы программы, поэтому и создал этот менеджер..
     * */
    private static HistoryManager historyManager = Managers.getDefaultHistory();
    private static List<Task> testTasksList = new ArrayList<>();

    LinkedList<Task> tasksFromHistory = new LinkedList<>();

    @BeforeAll
    public static void prepare() {
        testTasksList.add(new Task("Task name 1", "Task description 1"));
        testTasksList.add(new Task("Task name 2", "Task description 2"));
        testTasksList.add(new Task("Task name 3", "Task description 3"));
        testTasksList.add(new Task("Task name 4", "Task description 4"));
        testTasksList.add(new Task("Task name 5", "Task description 5"));
    }

    @AfterEach
    public void afterEach() {
        historyManager.clearHistoryList();
        tasksFromHistory.clear();
    }

    @Test
    public void addingTaskToHistoryManagerNotChangeTask() {
        Task task1 = new Task("Task name 1", "Task description");
        historyManager.add(task1);


        tasksFromHistory = (LinkedList<Task>) historyManager.getHistory();

        Task taskFromHistory = tasksFromHistory.get(0);
        /*
         * Насчет проверки того что работа менеджера не меняет задачи, решил оставить как есть, т.е. проверку всех полей,
         * хотя в логике программы заложено что достаточно одинакового id, чтобы считать задачи одинаковыми, но
         * при этом в ТЗ есть пункты, цитирую:
         *
         *  - создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
         *  - убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
         *
         * Подскажите, если это излишне, тогда в дальнейшем буду проверять только по id:)
         * */
        assertEquals(task1.getId(), taskFromHistory.getId());
        assertEquals(task1.getName(), taskFromHistory.getName());
        assertEquals(task1.getDescription(), taskFromHistory.getDescription());
        assertEquals(task1.getTaskStatus(), taskFromHistory.getTaskStatus());
    }

    @Test
    public void checkAddOrder() {
        for (Task task : testTasksList) {
            historyManager.add(task);
        }

        tasksFromHistory = (LinkedList<Task>) historyManager.getHistory();

        for (int i = 0; i < tasksFromHistory.size(); i++) {
            assertEquals(tasksFromHistory.get(i), testTasksList.get(i));
        }
    }

    @Test
    public void addSameTaskToHistoryList() {
        historyManager.add(testTasksList.get(0));
        historyManager.add(testTasksList.get(0));

        LinkedList<Task> listToCheck = new LinkedList<>();
        listToCheck.add(testTasksList.get(0));
        listToCheck.add(testTasksList.get(0));

        assertEquals(listToCheck, historyManager.getHistory());
    }

    @Test
    public void historyIs10ElementOnly() {
        //добавляем суммарно 15 элементов в историю
        for (int i = 0; i < 3; i++) {
            for (Task task : testTasksList) {
                historyManager.add(task);
            }
        }

        assertEquals(10, historyManager.getHistory().size());
    }
}