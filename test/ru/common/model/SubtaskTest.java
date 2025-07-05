package ru.common.model;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.common.manager.InMemoryTaskManager;

public class SubtaskTest {

    private InMemoryTaskManager manager;
    private Subtask subtask;
    private Epic epic;

    @BeforeEach
    void createInMemoryManager() {
        manager = new InMemoryTaskManager();
        epic = MockData.createEpic();
        manager.createEpic(epic);
        subtask = MockData.createSubTask(epic);
        manager.createSubtask(subtask);
    }

    @Test
    void addNewSubtask() {
        assertNotNull(subtask.getId(),  "Подзадача не найдена.");
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
        Subtask subtask2 = new Subtask(subtask.getId(), "Новая подзадача", "Новая подзадача для теста", TaskStatus.NEW);
        Subtask invalidSubtask = manager.createSubtask(subtask2);
        assertNull(invalidSubtask, "Подзадача может быть своим эпиком.");
    }
}
