package manager;

import org.junit.jupiter.api.AfterAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TZ8FileBackedTaskManagerTest extends TZ8TaskManagerTest<FileBackedTaskManager> {
    static Path pathToTestFile = Paths.get("TestFileTZ8.csv");

    public TZ8FileBackedTaskManagerTest() {
        this.tasksManager = FileBackedTaskManager.getManager(pathToTestFile.toFile());
    }

    @AfterAll
    static public void afterAll() {
        try {
            Files.deleteIfExists(pathToTestFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
