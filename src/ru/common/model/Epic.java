package ru.common.model;

import java.util.ArrayList;

public class Epic extends Task {
    private int id;
    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subtaskIds = new ArrayList<>();
    }

    public Epic(int id, String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.id = id;
        subtaskIds = new ArrayList<>();
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
}