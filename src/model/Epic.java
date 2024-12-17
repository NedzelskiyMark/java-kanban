package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, TaskStatus taskStatus, String description) {
        super(id, name, taskStatus, description);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public int addSubTaskIdToEpic(Task task) {
        if (task.getId().equals(this.getId())) {
            return -1;
        }
        this.subTasks.add(task.getId());
        task.setRelationEpicId(this);

        return 1;
    }
}

