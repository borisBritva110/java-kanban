package ru.common.model;

public class Subtask extends Task {
    private int id;
    private int epicId;

    public Subtask(int epicId, String name, String description, TaskStatus status) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, int epicId, String name, String description, TaskStatus status) {
        super(name, description, status);
        this.id = id;
        this.epicId = epicId;
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
