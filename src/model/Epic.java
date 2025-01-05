package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, TaskStatus taskStatus, String description, int hours, int minutes) {
        super(id, name, taskStatus, description, hours, minutes);
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public int addSubTaskIdToEpic(Task task) {
        if (task.getId().equals(this.getId()) || task.getClass().getSimpleName().equals("Epic")) {
            return -1;
        }
        SubTask taskToAdd = (SubTask) task;
        this.subTasks.add(taskToAdd);
        task.setRelationEpicId(this);
        plusDuration(taskToAdd);
        return 1;
    }


}

