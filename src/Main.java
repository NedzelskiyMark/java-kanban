import manager.FileBackedTaskManager;
import model.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {

        /*
        * К сожалению дополнительное задание в main на этот раз не написал, не хватает времени, и так заятнул
        * с решением..
        * */

        Path pathToFile = Paths.get("tasks.csv");
        FileBackedTaskManager tasksManager = FileBackedTaskManager.loadFromFile(pathToFile.toFile());

    }
}
