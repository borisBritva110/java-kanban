package ru.common.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.common.manager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private InMemoryTaskManager manager;
    private Epic epic;
    private Subtask subtask2;
    private Subtask subtask3;

    @BeforeEach
    void createInMemoryManager() {
        manager = new InMemoryTaskManager();
        epic = MockData.createEpic();
        subtask2 = MockData.createSubTask2(epic);
        subtask3 = MockData.createSubTask3(epic);
    }

    @Test
    public void createEpicWithSubtasks() {
        assertEquals(epic.getTaskStatus(), subtask2.getTaskStatus(), "Статусы эпика и подзадачи отличаются");
        assertEquals(epic.getTaskStatus(), subtask3.getTaskStatus(), "Статусы эпика и подзадачи отличаются");
    }

    @Test
    public void testUpdateEpicStatusAllDone() {
        epic.addSubtaskId(subtask2.getId());
        epic.addSubtaskId(subtask3.getId());
        manager.updateEpicStatus(epic);
        assertEquals(TaskStatus.NEW, epic.getTaskStatus(), "Статуса эпика и его подзадач отличается");
    }
}

