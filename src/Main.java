import manager.FileBackedTaskManager;
import model.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {

        Path pathToFile = Paths.get("tasks.csv");
        FileBackedTaskManager tasksManager = FileBackedTaskManager.loadFromFile(pathToFile.toFile());

//-----------------------------------------------------------------------------------------------------
//        Task task1 = new Task("Задача 1", "Сделать сценарий действий в Main");
//        Task task2 = new Task("Задача 2", "Сварить кофейку");
//
//        tasksManager.addTaskToList(task1);
//        tasksManager.addTaskToList(task2);
//
//        Epic driverLicenseTask = new Epic("Эпик 1", "Сдать на права");
//        SubTask driverLicenseSubtask1 = new SubTask("Подзадача 1", "Поступить в автошколу");
//        SubTask driverLicenseSubtask2 = new SubTask("Подзадача 2", "Подготовиться к экзамену");
//        SubTask driverLicenseSubtask3 = new SubTask("Подзадача 3", "Сдать экзамен");
//
//        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask1);
//        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask2);
//        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask3);
//        tasksManager.addSubTaskToList(driverLicenseSubtask1);
//        tasksManager.addSubTaskToList(driverLicenseSubtask2);
//        tasksManager.addSubTaskToList(driverLicenseSubtask3);
//
//        Epic circusTrick = new Epic("Эпик 2", "Научиться жонглировать");
//
//        tasksManager.addEpicToList(driverLicenseTask);
//        tasksManager.addEpicToList(circusTrick);
//
//        List<Task> listBeforeDelete = tasksManager.getAllTasksList();
//        tasksManager.deleteById(3);
//        tasksManager.deleteById(1);

//-------------------------------------------------------------------------------------------------------
        List<Task> list = tasksManager.getAllTasksList();

        System.out.println(list);

//        TaskManager tasksManager = Managers.getDefault();
//
//        Task task1 = new Task("Задача 1", "Сделать сценарий действий в Main");
//        Task task2 = new Task("Задача 2", "Сварить кофейку");
//
//        tasksManager.addTaskToList(task1);
//        tasksManager.addTaskToList(task2);
//
//        Epic driverLicenseTask = new Epic("Эпик 1", "Сдать на права");
//        SubTask driverLicenseSubtask1 = new SubTask("Подзадача 1", "Поступить в автошколу");
//        SubTask driverLicenseSubtask2 = new SubTask("Подзадача 2", "Подготовиться к экзамену");
//        SubTask driverLicenseSubtask3 = new SubTask("Подзадача 3", "Сдать экзамен");
//
//        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask1);
//        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask2);
//        driverLicenseTask.addSubTaskIdToEpic(driverLicenseSubtask3);
//        tasksManager.addSubTaskToList(driverLicenseSubtask1);
//        tasksManager.addSubTaskToList(driverLicenseSubtask2);
//        tasksManager.addSubTaskToList(driverLicenseSubtask3);
//
//        Epic circusTrick = new Epic("Эпик 2", "Научиться жонглировать");
//
//        tasksManager.addEpicToList(driverLicenseTask);
//        tasksManager.addEpicToList(circusTrick);
//
//        System.out.println();
//        System.out.println("Список всех задач задач");
//        System.out.println("---------------------------------------------------");
//        System.out.println(tasksManager.getAllTasksList());
//
//        System.out.println();
//        System.out.println("Проверка работы истории просмотров");
//        System.out.println("---------------------------------------------------");
//        System.out.println("Скрытый вызов задачи 1");
//        tasksManager.getTaskById(1);
//        System.out.println("Проверка записи в истории:");
//        System.out.println(tasksManager.getHistory());
//        System.out.println("***************************");
//        System.out.println("Скрытый вызов задачи 2");
//        tasksManager.getTaskById(2);
//        System.out.println("Проверка записи в истории:");
//        System.out.println(tasksManager.getHistory());
//        System.out.println("***************************");
//        System.out.println("Скрытый вызов задачи 3");
//        tasksManager.getTaskById(3);
//        System.out.println("Проверка записи в истории:");
//        System.out.println(tasksManager.getHistory());
//        System.out.println("***************************");
//        System.out.println("Повторный вызов задачи 1");
//        tasksManager.getTaskById(1);
//        System.out.println("Проверка записи в истории:");
//        System.out.println(tasksManager.getHistory());
//        System.out.println("***************************");
//        System.out.println("Повторный вызов задачи 2");
//        tasksManager.getTaskById(2);
//        System.out.println("Проверка записи в истории:");
//        System.out.println(tasksManager.getHistory());
//        System.out.println("***************************");
//
//        tasksManager.updateTask(driverLicenseSubtask2);
//        tasksManager.updateTask(task2);
//
//
//
//
//        System.out.println();
//        System.out.println("Удаление записи с id1 из истории");
//        System.out.println("---------------------------------------------------");
//        tasksManager.removeTaskFromHistoryList(1);
//        System.out.println(tasksManager.getHistory());
//
//        System.out.println();
//        System.out.println("Удаление эпика удалит его и его подзадачи из истории");
//        System.out.println("---------------------------------------------------");
//        System.out.println("Скрытый вызов 3 подзадач Эпика");
//        tasksManager.getTaskById(4);
//        tasksManager.getTaskById(5);
//        tasksManager.getTaskById(6);
//        System.out.println("История до удаления эпика");
//        System.out.println(tasksManager.getHistory());
//        System.out.println("История после удаления эпика");
//        tasksManager.deleteById(3);
//        System.out.println(tasksManager.getHistory());

//        Path task = Paths.get("taskTest.csv");
//        List<Task> listTest = tasksManager.getAllTasksList();
//
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(task.toFile(), true))) {
//            if (!Files.exists(task)) {
//                Files.createFile(task);
//
//            }
//            bw.write("!id,type,name,status,description,epic!\n");
//            for (Task taskStr: listTest) {
//                bw.write(taskStr.toString());
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//        List<String> listTestReader = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(task.toFile()))) {
//            while (br.ready()) {
//                listTestReader.add(br.readLine());
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println(listTestReader);
//        /*
//        *
//        *
//        * Удаление задачи из файла
//        *
//        * */
//        List<String> stringsCopy = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(task.toFile()))){
//            while (br.ready()) {
//                stringsCopy.add(br.readLine());
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        ArrayList<Integer> indexesToRemove = new ArrayList<>();
//        for (int i = 0; i < stringsCopy.size(); i++) {
//            if (stringsCopy.get(i).startsWith("5") || stringsCopy.get(i).startsWith("3")) {
//                indexesToRemove.add(i);
//            }
//        }
//
//        Collections.sort(indexesToRemove);
//        Collections.reverse(indexesToRemove);
//
//        for (Integer index: indexesToRemove) {
//            stringsCopy.remove((int) index);
//        }
//
//        try (BufferedWriter bwd = new BufferedWriter(new FileWriter(task.toFile()))) {
//            bwd.write("");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(task.toFile(), true))) {
//            for (String str: stringsCopy) {
//                bw.write(str + "\n");
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println();
//        System.out.println();
//        System.out.println();
//
//        try {
//            String str = Files.readString(task);
//            System.out.println(str);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }



    }
}
