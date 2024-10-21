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

    public void checkEpicStatus() {
        boolean isNewTasksInside = false;
        boolean isInProgressTasksInside = false;
        boolean isDoneTasksInside = false;

        for (SubTask subTask: subTasks) {
            switch (subTask.getTaskStatus()) {
                case NEW:
                    isNewTasksInside = true;
                    break;
                case IN_PROGRESS:
                    isInProgressTasksInside = true;
                case DONE:
                    isDoneTasksInside = true;
            }
        }

        if (isDoneTasksInside && !(isInProgressTasksInside || isNewTasksInside)) {
            //only Done tasks inside
            setTaskStatus(TaskStatus.DONE);

        } else if (isNewTasksInside && !(isInProgressTasksInside || isDoneTasksInside)) {
            //only NEW tasks inside
            //nothing will change, status already is NEW
        } else {
            setTaskStatus(TaskStatus.IN_PROGRESS);
        }
        //выбрал именно такой порядок т.к. показалось что проще описать две этих ситуации, а в ином случае ставить
        //статус IN_PROGRESS, чем описывать все варианты при которых Epic будет IN_PROGRESS

    }
}

