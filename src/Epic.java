import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
        //making connection with SubTasks by recording Epic id into SubTasks
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.put(subTask.getId(), subTask);
        subTask.setRelationEpicId(this.getId());
    }

    public void checkEpicStatus() {
        boolean isAllSubtasksDone = true;

        for (SubTask subtask: subTasks.values()) {
            if (subtask.getTaskStatus().equals(TaskStatus.NEW) ||
            subtask.getTaskStatus().equals(TaskStatus.IN_PROGRESS)) {
                isAllSubtasksDone = false;
                break;
            }
        }
        //if all SubTasks is DONE, Epic is DONE, else Epic IN_PROGRESS because this method use only when
        // SubTask status is updated
        if (isAllSubtasksDone) {
            this.setTaskStatus(TaskStatus.DONE);
        } else {
            this.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }
}

