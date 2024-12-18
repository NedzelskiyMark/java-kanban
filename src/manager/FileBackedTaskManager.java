package manager;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTasksManager {
    private static Path taskPath;

    public FileBackedTaskManager(File file) {
        taskPath = file.toPath();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        if (file.exists()) {
            List<String> listOfLinesFromFile = getListOfStringFromFile(file);

            for (String line : listOfLinesFromFile) {
                Task task = fromString(line);
                if (task.getClass().getSimpleName().equals("Task")) {
                    taskManager.addTaskToList(task);
                } else if (task.getClass().getSimpleName().equals("Epic")) {
                    taskManager.addEpicToList((Epic) task);
                } else if (task.getClass().getSimpleName().equals("SubTask")) {
                    taskManager.addSubTaskToList((SubTask) task);
                }
            }
        } else {
            createNewFile(file.toPath());
        }

        return taskManager;
    }


    public static void createNewFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при создании файла");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskPath.toFile()))) {
            writer.write("id,type,name,status,description,epic\n");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи в файл");
        }
    }

    public void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при создании файла");
        }

    }

    public List<Task> loadListOfTasksFromFile(File file) {
        List<Task> listOfTasksToReturn = new ArrayList<>();
        List<String> linesOfTasks = getListOfStringFromFile(file);

        for (String line : linesOfTasks) {
            Task taskFromFile = fromString(line);
            listOfTasksToReturn.add(taskFromFile);
        }

        return listOfTasksToReturn;
    }


    public static List<String> getListOfStringFromFile(File file) {
        List<String> linesOfTasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.startsWith("id") || line.isBlank()) {  //пропускаем первую или пустую строки
                    continue;
                } else {
                    linesOfTasks.add(line);
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Произошла ошибка при чтении задач из файла");
        }

        return linesOfTasks;
    }

    public void save(Task task) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskPath.toFile(), true))) {
            writer.write(task.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи новой задачи в файл");
        }
    }

    public List<String> getListOfTasksFromMemory() {
        List<Task> listOfTasks = super.getAllTasksList();
        List<String> listOsStringTasks = new ArrayList<>();

        for (Task task : listOfTasks) {
            listOsStringTasks.add(task.toString());
        }

        return listOsStringTasks;
    }

    public void saveTasksToFileFromList(List<String> listOfTasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskPath.toFile(), true))) {
            for (String line : listOfTasks) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи задачи из списка в файл");
        }
    }

    public void updateFileToActualTasks(Path path) {
        List<String> linesOfTasks = getListOfTasksFromMemory();
        deleteFile(path);
        createNewFile(path);
        saveTasksToFileFromList(linesOfTasks);
    }

    public static Task fromString(String value) {
        String[] strArr = value.split(",");
        int id = Integer.parseInt(strArr[0]);
        String taskType = strArr[1];
        String name = strArr[2];
        String taskStatusString = strArr[3];
        String description = strArr[4];
        TaskStatus taskStatus = TaskStatus.NEW;

        int relationEpicId = 0;
        if (strArr.length == 6) {
            relationEpicId = Integer.parseInt(strArr[5]);
        }


        switch (taskStatusString) {
            case "IN_PROGRESS":
                taskStatus = TaskStatus.IN_PROGRESS;
                break;
            case "DONE":
                taskStatus = TaskStatus.DONE;
        }

        Task taskFromFile = null;

        switch (taskType) {
            case "TASK":
                taskFromFile = new Task(id, name, taskStatus, description);
                break;
            case "EPIC":
                taskFromFile = new Epic(id, name, taskStatus, description);
                break;
            case "SUBTASK":
                taskFromFile = new SubTask(id, name, taskStatus, description, relationEpicId);
        }

        return taskFromFile;

    }

    @Override
    public List<Task> getAllTasksList() {
        return super.getAllTasksList();
    }

    @Override
    public void deleteAllTasks() {
        /*
         * Метод очищает список задач, историю, удаляет файл и создает новый,
         * чтобы программа могла продолжать работу
         * */
        super.deleteAllTasks();
        updateFileToActualTasks(taskPath);
    }

    @Override
    public Task getTaskById(int idToFind) {
        return super.getTaskById(idToFind);
    }

    @Override
    public void addTaskToList(Task newTask) {
        super.addTaskToList(newTask);
        save(newTask);
    }

    @Override
    public void addEpicToList(Epic newEpic) {
        super.addEpicToList(newEpic);
        save(newEpic);
    }

    @Override
    public void addSubTaskToList(SubTask newSubtask) {
        super.addSubTaskToList(newSubtask);
        save(newSubtask);
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        updateFileToActualTasks(taskPath);
    }

    @Override
    public void deleteById(int idToRemove) {
        super.deleteById(idToRemove);
        updateFileToActualTasks(taskPath);
    }

    @Override
    public void removeSubtasksOfEpic(int id) {
        super.removeSubtasksOfEpic(id);
        updateFileToActualTasks(taskPath);
    }

    @Override
    public List<SubTask> getAllSubtaskOfEpic(int id) {
        return super.getAllSubtaskOfEpic(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void removeTaskFromHistoryList(int id) {
        super.removeTaskFromHistoryList(id);
    }

    public static void main(String[] args) {
        /*
         * Спасибо за ревью, надеюсь что фраза "интересно думаешь" не эвфемизм:)
         * Решил все же сделать пользовательский сценарий, но не уверен что правильно понял его задание.
         * Я создам менеджер задач, добавлю туда несколько разных задач, после чего сделаю еще один менеджер,
         * как я понял, он должен увидеть созданный файл и подтянуть задачи из него себе в память.
         * Надеюсь что в этом суть задания была
         * */
        Path pathToFile = Paths.get("tasks.csv");
        FileBackedTaskManager firstTaskManager = FileBackedTaskManager.loadFromFile(pathToFile.toFile());

        Task task = new Task("Task name", "Task description");
        firstTaskManager.addTaskToList(task);

        Epic epic = new Epic("Epic name", "Epic description");
        firstTaskManager.addEpicToList(epic);

        SubTask subtaskForEpic = new SubTask("Subtask name", "Subtask description");
        epic.addSubTaskIdToEpic(subtaskForEpic);
        firstTaskManager.addSubTaskToList(subtaskForEpic);

        Epic epicSecond = new Epic("EpicSecond name", "EpicSecond description");
        firstTaskManager.addEpicToList(epicSecond);

        SubTask secondEpicSubtask1 = new SubTask("Subtask second name", "Subtask second description");
        epicSecond.addSubTaskIdToEpic(secondEpicSubtask1);
        firstTaskManager.addSubTaskToList(secondEpicSubtask1);

        SubTask secondEpicSubtask2 = new SubTask("Second subtask second name", "Second subtask description");
        epicSecond.addSubTaskIdToEpic(secondEpicSubtask2);
        firstTaskManager.addSubTaskToList(secondEpicSubtask2);

        Task task7 = new Task("Task 7 name", "Task 7 description");
        firstTaskManager.addTaskToList(task7);

        //Список задач из памяти первого менеджера
        List<String> tasksListFromFirstManager = firstTaskManager.getListOfTasksFromMemory();
        System.out.println(tasksListFromFirstManager);

        //Создание второго менеджера и создание списка задач из него
        FileBackedTaskManager secondTaskManager = FileBackedTaskManager.loadFromFile(pathToFile.toFile());
        List<String> tasksListFromSecondManager = secondTaskManager.getListOfTasksFromMemory();
        System.out.println(tasksListFromSecondManager);

        //сравнение двух списков
        System.out.println(tasksListFromFirstManager.equals(tasksListFromSecondManager));

        try {
            Files.deleteIfExists(pathToFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
