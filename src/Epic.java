import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, ArrayList<SubTask> subTasks) {
        super(name, description);
        this.subTasks = subTasks;
        //making connection with SubTasks by recording Epic id into SubTasks
        for (SubTask subTask : subTasks) {
            subTask.setRelationEpicId(this.getId());
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }


}

