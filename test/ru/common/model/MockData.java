package ru.common.model;

public class MockData {

    public static Task createTask1() {
        return new Task("Task", "Описание", TaskStatus.NEW);
    }

    public static Task createTask2() {
        return new Task("Task", "Описание", TaskStatus.NEW);
    }

    public static Epic createEpic() {
        return new Epic("Epic", "Описание");
    }

    public static Subtask createSubTask(Epic epic) {
        return new Subtask(epic.getId(), "SubTask", "Описание", TaskStatus.DONE);
    }

    public static Subtask createSubTask2(Epic epic) {
        return new Subtask(epic.getId(), "SubTask 2", "Описание 2", TaskStatus.NEW);
    }

    public static Subtask createSubTask3(Epic epic) {
        return new Subtask(epic.getId(), "SubTask 3", "Описание 3", TaskStatus.NEW);
    }
}