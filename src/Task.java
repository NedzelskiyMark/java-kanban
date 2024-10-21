public class Task {
    private String name;
    private String description;
    private final int id;
    private static int count = 0;
    private TaskStatus taskStatus;

    public Task(String name, String description) {
        count++;
        id = count;
        this.taskStatus = TaskStatus.NEW;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "{id=" + getId() + ", name='" + getName() + "', description='" + getDescription() + "', status="
                + getTaskStatus() + "'}";
    }
}
