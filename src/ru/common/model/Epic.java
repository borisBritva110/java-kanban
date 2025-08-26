package ru.common.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private int id;
    private ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        subtaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description, TaskStatus.NEW);
        subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description) {
        this(GENERATED_ID, name, description);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        if (subtaskIds == null) {
            subtaskIds = new ArrayList<>();
        }
        if (subtaskId != this.getId()) {
            subtaskIds.add(subtaskId);
        }
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
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}