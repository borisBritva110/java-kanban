package ru.common.model;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.common.manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubtaskTest {

    private InMemoryTaskManager manager;
    private Subtask subtask;
    private Epic epic;

    @BeforeEach
    void createInMemoryManager() {
        manager = new InMemoryTaskManager();
        epic = new Epic("Эпик", "Эпик для теста подзадач");
        manager.createEpic(epic);

        subtask = new Subtask(epic.getId(), "Подзадача", "Подзадача для теста", TaskStatus.NEW);
        manager.createSubtask(subtask);
    }

    @Test
    void addNewSubtask() {
        Subtask savedSubTask = manager.getSubtaskById(subtask.getId());
        assertNotNull(savedSubTask,  "Подзадача не найдена.");
        assertEquals(subtask, savedSubTask, "Задачи не совпадают.");

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
        assertTrue(epic.getSubtaskIds().contains(subtask.getId()), "Подзадача отсутствует в эпике.");
    }

    @Test
    void updateSubtaskStatus() {
        manager.updateSubtask(
            new Subtask(epic.getId(), "Обновленная подзадача", "Описание", TaskStatus.IN_PROGRESS),
            subtask.getId()
        );
        Subtask newSubtask = manager.getSubtaskById(subtask.getId());

        assertNotEquals(subtask, newSubtask);
        assertEquals(subtask.getId(), newSubtask.getId());
    }

    @Test
    public void cannotAddSubtaskAsEpicToItself() {
        Subtask invalidSubtask = new Subtask(subtask.getId(), "Новая подзадача", "Новая подзадача для теста", TaskStatus.NEW);
        manager.createSubtask(invalidSubtask);
        assertEquals(0, invalidSubtask.getEpicId());
    }
}
