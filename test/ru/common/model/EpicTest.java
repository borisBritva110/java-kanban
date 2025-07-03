package ru.common.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.common.manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private InMemoryTaskManager manager;
    private Epic epic;

    @BeforeEach
    void createInMemoryManager() {
        manager = new InMemoryTaskManager();
        epic = new Epic("Эпик", "Эпик для теста");
        manager.createEpic(epic);
    }

    @Test
    public void createEpicWithSubtasks() {
        Subtask subtask = new Subtask(epic.getId(), "Подзадача", "", TaskStatus.NEW);
        manager.createSubtask(subtask);

        Subtask testSubtask = new Subtask(epic.getId(), "Подзадача для теста", "", TaskStatus.NEW);
        manager.createSubtask(testSubtask);

        assertEquals(epic.getTaskStatus(), subtask.getTaskStatus(), "Статусы эпика и подзадачи отличаются");
        assertEquals(epic.getTaskStatus(), testSubtask.getTaskStatus(), "Статусы эпика и подзадачи отличаются");
    }

    @Test
    public void testUpdateEpicStatusAllDone() {
        Subtask subtask1 = new Subtask(epic.getId(), "Подзадача 1", "", TaskStatus.DONE);
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask(epic.getId(), "Подзадача 2", "", TaskStatus.DONE);
        manager.createSubtask(subtask2);
        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());
        manager.updateEpicStatus(epic);
        assertEquals(TaskStatus.DONE, epic.getTaskStatus(), "Статуса эпика и его подзадач отличается");
    }
}

