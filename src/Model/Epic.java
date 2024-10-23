package Model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void addSubTaskIdToEpic(SubTask subtask) {
        this.subTasks.add(subtask.getId());
        subtask.setRelationEpicId(this.getId());
    }
}

