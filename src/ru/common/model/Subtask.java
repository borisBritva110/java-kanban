package ru.common.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int id;
    private int epicId;

    public Subtask(int id, String name, String description, TaskStatus status,
                   LocalDateTime startTime, Duration duration, int epicId) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status,
                   LocalDateTime startTime, Duration duration, int epicId) {
        this(GENERATED_ID, name, description, status, startTime, duration, epicId);
    }

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        this(GENERATED_ID, name, description, status, null, Duration.ZERO, epicId);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public TaskStatus getTaskStatus() {
        return super.getTaskStatus();
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        super.setTaskStatus(taskStatus);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }
}
