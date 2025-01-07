package model;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ManagerLoadExceptionTest {
    Path pathToFile = Paths.get("ManagerLoadExceptionFile");
    private FileBackedTaskManager tasksManager = FileBackedTaskManager.getManager(pathToFile.toFile());

    @Test
    public void managerLoadExceptionTest() {
        assertThrows(ManagerLoadException.class, () -> {
            tasksManager.deleteFile(pathToFile);
            tasksManager.getListOfStringFromFile(pathToFile.toFile());
        }, "Попытка прочесть не существующий файл");
    }
}
