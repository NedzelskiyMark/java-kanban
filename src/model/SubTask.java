package model;

public class SubTask extends Task {


    public SubTask(String name, String description, int hours, int minutes) {
        super(name, description, hours, minutes);
    }

    public SubTask(int id, String name, TaskStatus taskStatus, String description, int relationEpicId, int hours, int minutes) {
        super(id, name, taskStatus, description, relationEpicId, hours, minutes);
    }

}
