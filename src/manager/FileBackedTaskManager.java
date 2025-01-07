package manager;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTasksManager {
    private static Path taskPath;

    public FileBackedTaskManager(File file) {
        taskPath = file.toPath();
    }

    public static FileBackedTaskManager getManager(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        if (file.exists()) {
            List<String> listOfLinesFromFile = getListOfStringFromFile(file);

            listOfLinesFromFile.stream()
                    .forEach(line -> {
                        Task task = fromString(line);
                        if (task.getClass().getSimpleName().equals("Task")) {
                            taskManager.addTaskToList(task);
                        } else if (task.getClass().getSimpleName().equals("Epic")) {
                            taskManager.addEpicToList((Epic) task);
                        } else if (task.getClass().getSimpleName().equals("SubTask")) {
                            taskManager.addSubTaskToList((SubTask) task);
                        }
                    });
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
            writer.write("id,type,name,taskStatus,description,relatedEpic,durationHours,durationMinutes,startTime\n");
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

        linesOfTasks.stream().forEach(line -> listOfTasksToReturn.add(fromString(line)));

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

        listOfTasks.stream().forEach(task -> listOsStringTasks.add(task.toString()));

        return listOsStringTasks;
    }

    public void saveTasksToFileFromList(List<String> listOfTasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskPath.toFile(), true))) {
//По заданию надо поменять все циклы for на стримы, но здесь из-за того что приходится добавлять блок try-catch
// страдает читаемость кода. Мне кажется правильным было бы оставить цикл for ради читаемости или лучше стрим?
            listOfTasks.stream().forEach(line -> {
                try {
                    writer.write(line);
                } catch (IOException e) {
                    throw new ManagerSaveException("Произошла ошибка при записи задачи из списка в файл");
                }
            });
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
        int relationEpicId = Integer.parseInt(strArr[5]);
        int hours = Integer.parseInt(strArr[6]);
        int minutes = Integer.parseInt(strArr[7]);
        String startTime = strArr[8];


        TaskStatus taskStatus = TaskStatus.NEW;

        switch (taskStatusString) {
            case "IN_PROGRESS":
                taskStatus = TaskStatus.IN_PROGRESS;
                break;
            case "DONE":
                taskStatus = TaskStatus.DONE;
        }

        Task taskFromFile = null;
        if (startTime.equals("null")) {
            switch (taskType) {
                case "TASK":
                    taskFromFile = new Task(id, name, description, taskStatus, relationEpicId, hours, minutes);
                    break;
                case "EPIC":
                    taskFromFile = new Epic(id, name, description, taskStatus, hours, minutes);
                    break;
                case "SUBTASK":
                    taskFromFile = new SubTask(id, name, description, taskStatus, relationEpicId, hours, minutes);
            }
        } else {
            LocalDateTime startTimeParsed = LocalDateTime.parse(startTime,
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            switch (taskType) {
                case "TASK":
                    taskFromFile = new Task(id, name, description, taskStatus, relationEpicId, hours, minutes, startTimeParsed);
                    break;
                case "EPIC":
                    taskFromFile = new Epic(id, name, description, taskStatus, hours, minutes);
                    break;
                case "SUBTASK":
                    taskFromFile = new SubTask(id, name, description, taskStatus, relationEpicId, hours, minutes, startTimeParsed);
            }
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
        updateFileToActualTasks(taskPath);
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
        Path pathToFile = Paths.get("tasks.csv");
        FileBackedTaskManager firstTaskManager = FileBackedTaskManager.getManager(pathToFile.toFile());

        Task task = new Task("Task name", "Task description", 1, 15);
        firstTaskManager.addTaskToList(task);

        Epic epic = new Epic("Epic name", "Epic description");
        firstTaskManager.addEpicToList(epic);

        SubTask subtaskForEpic = new SubTask("Subtask name", "Subtask description", 0, 45);
        epic.addSubTaskIdToEpic(subtaskForEpic);
        firstTaskManager.addSubTaskToList(subtaskForEpic);

        Epic epicSecond = new Epic("EpicSecond name", "EpicSecond description");
        firstTaskManager.addEpicToList(epicSecond);

        SubTask secondEpicSubtask1 = new SubTask("Subtask second name", "Subtask second description", 0, 20);
        epicSecond.addSubTaskIdToEpic(secondEpicSubtask1);
        firstTaskManager.addSubTaskToList(secondEpicSubtask1);

        SubTask secondEpicSubtask2 = new SubTask("Second subtask second name", "Second subtask description", 0, 15);
        epicSecond.addSubTaskIdToEpic(secondEpicSubtask2);
        firstTaskManager.addSubTaskToList(secondEpicSubtask2);

        Task task7 = new Task("Task 7 name", "Task 7 description", 2, 55);
        firstTaskManager.addTaskToList(task7);
        try {
            firstTaskManager.setStartTimeToTask(task7, LocalDateTime.of(2025, 1, 5, 12, 0));
        } catch (IllegalStartTimeException e) {
            System.out.println(e.getMessage());
        }
        //Должен пойматься exception
        try {
            firstTaskManager.setStartTimeToTask(secondEpicSubtask2, LocalDateTime.of(2025, 1, 5, 12, 30));
        } catch (IllegalStartTimeException e) {
            System.out.println(e.getMessage());
        }

        firstTaskManager.updateTask(secondEpicSubtask1);
        firstTaskManager.updateTask(secondEpicSubtask2);
        firstTaskManager.updateTask(secondEpicSubtask2);

        //Список задач из памяти первого менеджера
        List<String> tasksListFromFirstManager = firstTaskManager.getListOfTasksFromMemory();
        System.out.println(tasksListFromFirstManager);

        //Создание второго менеджера и создание списка задач из него
        FileBackedTaskManager secondTaskManager = FileBackedTaskManager.getManager(pathToFile.toFile());
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
