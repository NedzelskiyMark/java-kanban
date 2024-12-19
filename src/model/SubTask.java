package model;

public class SubTask extends Task {


    public SubTask(String name, String description) {
        super(name, description);
    }

    public SubTask(int id, String name, TaskStatus taskStatus, String description, int relationEpicId) {
        super(id, name, taskStatus, description, relationEpicId);
    }

}
