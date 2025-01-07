package model;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ManagerSaveExceptionTest {
    Path pathToFile = Paths.get("ManagerLoadExceptionFile");
    private FileBackedTaskManager tasksManager = FileBackedTaskManager.getManager(pathToFile.toFile());

    @Test
    public void managerSaveException() {
        assertThrows(ManagerSaveException.class, () -> {
            tasksManager.createNewFile(pathToFile);
        }, "Попытка создания уже существующего файла");
    }
}
