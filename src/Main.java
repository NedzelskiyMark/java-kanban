import Manager.Managers;
import Manager.TaskManager;
import Model.*;

public class Main {

    public static void main(String[] args) {

        TaskManager tasksManager = Managers.getDefault();

        Task task1 = new Task("Помыть посуду", "Горячей воды нет");
        Task task2 = new Task("Сходить погулять", "Посмотреть заранее погоду");

        tasksManager.addTaskToList(task1);
        tasksManager.addTaskToList(task2);

        Epic driverLicenseTask = new Epic("Сдать на права", "Нужны права на трактор");
        SubTask driverLicenseSubtask1 = new SubTask("Поступить в автошколу", "Подобрать ее в интернете");
        SubTask driverLicenseSubtask2 = new SubTask("Подготовиться к экзамену", "Старательно учиться");
        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask1);
        tasksManager.addSubTaskToList(driverLicenseSubtask1);
        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask2);
        tasksManager.addSubTaskToList(driverLicenseSubtask2);

        Epic circusTrick = new Epic("Научиться жонглировать", "Найти тренера");
        SubTask circusTrickSubtask1 = new SubTask("Тренироваться 7 дней в неделю", "Купить шарики");
        circusTrick.addSubTaskIdToEpic(circusTrickSubtask1);
        tasksManager.addSubTaskToList(circusTrickSubtask1);

        tasksManager.addEpicToList(driverLicenseTask);
        tasksManager.addEpicToList(circusTrick);

        System.out.println();
        System.out.println("Список задач и эпиков, затем список подзадач эпика");
        System.out.println("---------------------------------------------------");
        System.out.println(tasksManager.getAllTasksList());
        System.out.println(tasksManager.getAllSubtaskOfEpic(3));

        System.out.println();
        System.out.println("Обновление задачи и подзадачи эпика");
        System.out.println("---------------------------------------------------");
        tasksManager.updateTask(1, task1);
        tasksManager.updateTask(4, driverLicenseSubtask1);
        System.out.println(tasksManager.getAllTasksList());

        System.out.println();
        System.out.println("Задача обновлена до DONE, также как и подзадачи эпика");
        System.out.println("---------------------------------------------------");
        tasksManager.updateTask(1, task1);//задача меняет статус на DONE
        tasksManager.updateTask(4, driverLicenseSubtask1);
        tasksManager.updateTask(4, driverLicenseSubtask1);
        tasksManager.updateTask(5, driverLicenseSubtask2);
        tasksManager.updateTask(5, driverLicenseSubtask2);
        System.out.println(tasksManager.getAllTasksList());

        System.out.println();
        System.out.println("Удаление задачи и эпика");
        System.out.println("---------------------------------------------------");
        tasksManager.deleteById(2);
        tasksManager.deleteById(6);
        System.out.println(tasksManager.getAllTasksList());

        System.out.println();
        System.out.println("Проверка метода getTaskById");
        System.out.println("---------------------------------------------------");
        System.out.println(tasksManager.getTaskById(1));
        System.out.println(tasksManager.getTaskById(3));
        System.out.println(tasksManager.getTaskById(5));

        System.out.println();
        System.out.println("Удаление всех задач");
        System.out.println("---------------------------------------------------");
        tasksManager.deleteAllTasks();
        System.out.println(tasksManager.getAllTasksList());
    }
}
