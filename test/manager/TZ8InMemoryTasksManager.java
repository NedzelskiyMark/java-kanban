package manager;

public class TZ8InMemoryTasksManager extends TZ8TaskManagerTest<TaskManager> {
    public TZ8InMemoryTasksManager() {
        this.tasksManager = Managers.getDefault();
    }
}
