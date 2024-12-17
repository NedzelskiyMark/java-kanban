package manager;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTasksManager {
    private static TaskManager inMemoryTaskManager = Managers.getDefault();
    private static final Path taskPath = Paths.get("tasks.csv");

    public FileBackedTaskManager() throws ManagerSaveException {
        /*
        *Показалось логичным в конструкторе проверять наличие файла и делать запись первой строчки в него,
        *если файл есть, восстанавливать задачи в память программы (Мапу в InMemoryTaskManager)
        * */
        if (!Files.exists(taskPath)) {
            createNewFile(taskPath);
        } else {
            loadFromFile(taskPath.toFile());
        }
    }


    public void createNewFile(Path path) throws ManagerSaveException {
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

    public void deleteFile(Path path) throws ManagerSaveException {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при создании файла");
        }

    }

    public static void loadFromFile(File file) throws ManagerSaveException {
        /*
        * В этом методе я из файла делаю список строк, с каждой строки восстанавливаю задачу,
        * добавляю эту задачу в память программы (Мапу в InMemoryTaskManager)
        * */
        List<String> linesOfTasks = listOfStringFromFile(file);

        for (String line: linesOfTasks) {
            Task taskToMemory = fromString(line);

            if (taskToMemory.getClass().getSimpleName().equals("Task")) {
                inMemoryTaskManager.addTaskToList(taskToMemory);
            } else if (taskToMemory.getClass().getSimpleName().equals("Epic")) {
                inMemoryTaskManager.addEpicToList((Epic) taskToMemory);
            } else if (taskToMemory.getClass().getSimpleName().equals("SubTask")) {
                inMemoryTaskManager.addSubTaskToList((SubTask) taskToMemory);
            }
        }
    }

    public static List<String> listOfStringFromFile(File file) throws ManagerSaveException {
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
            throw new ManagerSaveException("Произошла ошибка при чтении задач из файла");
        }

        return linesOfTasks;
    }

    public void save(Task task) throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskPath.toFile(), true))) {
            writer.write(task.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи новой задачи в файл");
        }
    }

    public List<String> getListOfTasksFromMemory() {
        List<Task> listOfTasks = inMemoryTaskManager.getAllTasksList();
        List<String> listOsStringTasks = new ArrayList<>();

        for (Task task: listOfTasks) {
            listOsStringTasks.add(task.toString());
        }

        return listOsStringTasks;
    }

    public void saveTasksToFileFromList(List<String> listOfTasks) throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskPath.toFile(), true))) {
            for (String line: listOfTasks) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при записи задачи из списка в файл");
        }
    }

    public void updateFileToActualTasks(Path path) throws ManagerSaveException {
        List<String> linesOfTasks = getListOfTasksFromMemory();
        deleteFile(path);
        createNewFile(path);
        saveTasksToFileFromList(linesOfTasks);
    }

    public static Task fromString(String value) {
        String[] strArr = value.split(",");
        int id = Integer.parseInt(strArr[0]);
        String name = strArr[2];
        String description = strArr[4];
        TaskStatus taskStatus = TaskStatus.NEW;

        int relationEpicId = 0;
        if (strArr.length == 6) {
            relationEpicId = Integer.parseInt(strArr[5]);
        }


        switch (strArr[3]) {
            case "IN_PROGRESS":
                taskStatus = TaskStatus.IN_PROGRESS;
                break;
            case "DONE":
                taskStatus = TaskStatus.DONE;
        }

        Task taskFromFile = null;

        switch (strArr[1]) {
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
        return inMemoryTaskManager.getAllTasksList();
    }

    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        /*
        * Метод очищает список задач, историю, удаляет файл и создает новый,
        * чтобы программа могла продолжать работу
        * */
        inMemoryTaskManager.deleteAllTasks();
         updateFileToActualTasks(taskPath);
    }

    @Override
    public Task getTaskById(int idToFind) {
        return inMemoryTaskManager.getTaskById(idToFind);
    }

    @Override
    public void addTaskToList(Task newTask) throws ManagerSaveException {
        super.addTaskToList(newTask);
        save(newTask);
    }
    @Override
    public void addEpicToList(Epic newEpic) throws ManagerSaveException {
        super.addEpicToList(newEpic);
        save(newEpic);
    }
    @Override
    public void addSubTaskToList(SubTask newSubtask) throws ManagerSaveException {
        super.addSubTaskToList(newSubtask);
        save(newSubtask);
    }
    @Override
    public void updateTask(Task updatedTask) throws ManagerSaveException {
        inMemoryTaskManager.updateTask(updatedTask);
        updateFileToActualTasks(taskPath);
    }

    @Override
    public void deleteById(int idToRemove) throws ManagerSaveException {
        inMemoryTaskManager.deleteById(idToRemove);
        updateFileToActualTasks(taskPath);
    }

    @Override
    public void removeSubtasksOfEpic(int id) throws ManagerSaveException {
        inMemoryTaskManager.removeSubtasksOfEpic(id);
        updateFileToActualTasks(taskPath);
    }

    @Override
    public List<SubTask> getAllSubtaskOfEpic(int id) {
        return inMemoryTaskManager.getAllSubtaskOfEpic(id);
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryTaskManager.getHistory();
    }

    @Override
    public void removeTaskFromHistoryList(int id) {
        inMemoryTaskManager.removeTaskFromHistoryList(id);
    }
}
