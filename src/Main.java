public class Main {

    public static void main(String[] args) {

        TasksManager tasksManager = new TasksManager();

        Task task1 = new Task("Помыть посуду", "Горячей воды нет");
        Task task2 = new Task("Сходить погулять", "Посмотреть заранее погоду");

        tasksManager.addTask(task1);
        tasksManager.addTask(task2);

        Epic driverLicenseTask = new Epic("Сдать на права", "Нужны права на трактор");
        SubTask driverLicenseSubtask1 = new SubTask("Поступить в автошколу", "Подобрать ее в интернете");
        SubTask driverLicenseSubtask2 = new SubTask("Подготовиться к экзамену", "Старательно учиться");
        driverLicenseTask.addSubTask(driverLicenseSubtask1);
        driverLicenseTask.addSubTask(driverLicenseSubtask2);

        Epic circusTrick = new Epic("Научиться жонглировать", "Найти тренера");
        SubTask circusTrickSubtask1 = new SubTask("Тренироваться 7 дней в неделю", "Купить шарики");
        circusTrick.addSubTask(circusTrickSubtask1);

        tasksManager.addTask(driverLicenseTask);
        tasksManager.addTask(circusTrick);

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
        System.out.println("Удаление всех задач");
        System.out.println("---------------------------------------------------");
        tasksManager.deleteAllTasks();
        System.out.println(tasksManager.getAllTasksList());
    }
}
