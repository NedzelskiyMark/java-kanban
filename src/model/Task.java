package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private int relationEpicId = 0;

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

    public Task(int id, String name, String description, TaskStatus taskStatus,
                int hours, int minutes) {
        this(name, description, hours, minutes);
        this.id = id;
        this.taskStatus = taskStatus;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus,
                int relationEpicId, int hours, int minutes) {
        this(name, description, hours, minutes);
        this.id = id;
        this.taskStatus = taskStatus;
        this.relationEpicId = relationEpicId;
    }

    public Task(int id, String name, String description, TaskStatus taskStatus,
                int relationEpicId, int hours, int minutes, String startTime) {
        this(id, name, description, taskStatus, relationEpicId, hours, minutes);
        if (!startTime.equals("null")) {
            this.startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            setEndTime();
        }
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

        String relationEpicString = Integer.toString(this.getRelationEpicId());

        String durationHours = String.valueOf(this.duration.toHours());
        String durationMinutes = String.valueOf(this.duration.toMinutesPart());

        String startTimeString;
        if (startTime == null) {
            startTimeString = "null";
        } else {
            startTimeString = startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }

        return this.id + "," + taskType + "," + this.name + "," +
                this.taskStatus + "," + this.description + "," + relationEpicString + "," + durationHours + "," +
                durationMinutes + "," + startTimeString + "\n";
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

    public void setStartTime(LocalDateTime startTimeToSet) {
        startTime = startTimeToSet;
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

