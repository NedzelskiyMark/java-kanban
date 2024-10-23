package Model;

public class SubTask extends Task {
    private int relationEpicId;

    public SubTask(String name, String description) {
        super(name, description);
    }

    public int getRelationEpicId() {
        return relationEpicId;
    }

    public void setRelationEpicId(int relationEpicId) {
        //making connection with Model.Epic by recording Model.Epic.id into Model.SubTask
        this.relationEpicId = relationEpicId;
    }

    @Override
    public String toString() {
        return "{id=" + getId() + ", name='" + getName() + "', description='" + getDescription() + "', status="
                + getTaskStatus() + "', relatedEpic id=" + getRelationEpicId() + "}\n";
    }
}
