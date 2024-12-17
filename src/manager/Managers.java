package manager;

import model.ManagerSaveException;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static FileBackedTaskManager getFileBackedTaskManager() throws ManagerSaveException {
        return new FileBackedTaskManager();
    }
}
