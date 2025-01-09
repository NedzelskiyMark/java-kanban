package model;

import java.time.LocalDateTime;

public class SubTask extends Task {


    public SubTask(String name, String description, int hours, int minutes) {
        super(name, description, hours, minutes);
    }

    public SubTask(int id, String name, String description, TaskStatus taskStatus, int relationEpicId, int hours, int minutes) {
        super(id, name, description, taskStatus, relationEpicId, hours, minutes);
    }

    public SubTask(int id, String name, String description, TaskStatus taskStatus, int relationEpicId, int hours, int minutes, String startTime) {
        super(id, name, description, taskStatus, relationEpicId, hours, minutes, startTime);
    }

}
