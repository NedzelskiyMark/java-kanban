public class SubTask  extends Task {
    private int relationEpicId;
    SubTask (String name, String description) {
        super(name, description);
    }

    public int getRelationEpicId() {
        return relationEpicId;
    }

    public void setRelationEpicId(int relationEpicId) {
        this.relationEpicId = relationEpicId;
    }
}
