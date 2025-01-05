package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

//
public class Task {
    private String name;
    private String description;
    private Integer id;
    private static int count = 0;
    private TaskStatus taskStatus;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int relationEpicId;

    public Task(String name, String description) {
        count++;
        id = count;
        this.taskStatus = TaskStatus.NEW;
        this.name = name;
        this.description = description;
        this.duration = Duration.ofHours(0).plusMinutes(0);
    }

    public Task(String name, String description, int hours, int minutes) {
        count++;
        id = count;
        this.taskStatus = TaskStatus.NEW;
        this.name = name;
        this.description = description;
        duration = Duration.ofHours(hours).plusMinutes(minutes);
    }

    public Task(int id, String name, TaskStatus taskStatus, String description,
                int hours, int minutes) {
        this(name, description, hours, minutes);
        this.id = id;
        this.taskStatus = taskStatus;
    }

    public Task(int id, String name, TaskStatus taskStatus, String description,
                int relationEpicId, int hours, int minutes) {
        this(id, name, taskStatus, description, hours, minutes);
        this.relationEpicId = relationEpicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        //Делаю строку для статуса задачи
        TaskType taskType = TaskType.TASK;
        switch (this.getClass().getSimpleName()) {
            case "Epic":
                taskType = TaskType.EPIC;
                break;
            case "SubTask":
                taskType = TaskType.SUBTASK;
        }

//        if (duration == null) {
//            durationHours = "";
//            durationMinutes = "";
//        } else {
//            durationHours = String.valueOf(duration.toHours());
//            durationMinutes = String.valueOf(duration.toMinutesPart());
//        }
        String durationHours = String.valueOf(this.duration.toHours());
        String durationMinutes = String.valueOf(this.duration.toMinutesPart());

        /*
         * Связь с Эпиками есть только у Субтасков, делаю строку для них с id их Эпика,
         *  для других задач делаю пустую строку
         * */
        int relationEpic = this.getRelationEpicId();
        String relationEpicString;

        if (relationEpic == 0) {
            relationEpicString = "";
        } else {
            relationEpicString = Integer.toString(relationEpic);
        }

        return this.id + "," + taskType + "," + this.name + "," +
                this.taskStatus + "," + this.description + "," + durationHours + "," +
                durationMinutes + "," +  relationEpicString + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getRelationEpicId() {
        return relationEpicId;
    }

    public int setRelationEpicId(Task epicTask) {
        if (epicTask.getId().equals(this.getId())) {
            return -1;
        }
        this.relationEpicId = epicTask.getId();
        return 1;
    }

    public void setDuration(int hours, int minutes) {
        this.duration = Duration.ofHours(hours).plusMinutes(minutes);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(int year, int month, int day, int hour, int minutes) {
        startTime = LocalDateTime.of(year, month, day, hour, minutes);
        setEndTime();
    }

    private void setEndTime() {
        endTime = startTime.plus(duration);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void plusDuration(SubTask task) {
        duration = duration.plus(task.getDuration());
    }

    public void minusDuration(Duration durationToMinus) {
        duration = duration.minus(durationToMinus);
    }
}
