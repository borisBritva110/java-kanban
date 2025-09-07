package ru.common.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class MockData {
    public static int nextId = 1;

    public static Task createTask1() {
        return new Task(nextId++, "Task", "Описание", TaskStatus.NEW, LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofMinutes(10));
    }

    public static Task createTask2() {
        return new Task(nextId++, "Task", "Описание", TaskStatus.NEW, LocalDateTime.of(2024, 1, 2, 10, 0), Duration.ofMinutes(10));
    }

    public static Epic createEpic() {
        return new Epic(nextId++, "Epic", "Описание");
    }

    public static Subtask createSubTask(Epic epic) {
        return new Subtask(nextId++,"SubTask", "Описание", TaskStatus.DONE, LocalDateTime.of(2024, 1, 4, 10, 0), Duration.ofMinutes(10), epic.getId());
    }

    public static Subtask createSubTask2(Epic epic) {
        return new Subtask(nextId++, "SubTask 2", "Описание 2", TaskStatus.NEW, LocalDateTime.of(2024, 1, 5, 10, 0), Duration.ofMinutes(10), epic.getId());
    }

    public static Subtask createSubTask3(Epic epic) {
        return new Subtask(nextId++, "SubTask 3", "Описание 3", TaskStatus.NEW, LocalDateTime.of(2024, 1, 6, 10, 0), Duration.ofMinutes(10), epic.getId());
    }
}