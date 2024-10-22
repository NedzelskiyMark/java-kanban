public class SubTask extends Task {
    private int relationEpicId;

    SubTask(String name, String description) {
        super(name, description);
    }

    public int getRelationEpicId() {
        return relationEpicId;
    }

    public void setRelationEpicId(int relationEpicId) {
        //making connection with Epic by recording Epic.id into SubTask
        this.relationEpicId = relationEpicId;
    }
}
