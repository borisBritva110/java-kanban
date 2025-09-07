package ru.common.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.common.model.Epic;
import ru.common.model.MockData;
import ru.common.model.Subtask;
import ru.common.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {

    private static TaskManager taskManager;
    private Task task;
    private Task task2;
    private Subtask subtask;
    private Subtask subtask2;
    private Epic epic;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        task = MockData.createTask1();
        task2 = MockData.createTask2();
        epic = MockData.createEpic();
        subtask = MockData.createSubTask(epic);
        subtask2 = MockData.createSubTask2(epic);
        epic.addSubtaskId(subtask.getId());
    }

    @Test
    public void testGetPrioritizedTasks() {
        task.setStartTime(LocalDateTime.of(2024, 1, 4, 9, 0));
        task.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task);

        task2.setStartTime(LocalDateTime.of(2024, 1, 5, 9, 0));
        task2.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(task2);

        List<Task> prioritized = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritized.size(), "Ожидалось 2 элемента в списке");

        LocalDateTime t1 = prioritized.get(0).getStartTime();
        LocalDateTime t2 = prioritized.get(1).getStartTime();

        assertEquals(LocalDateTime.of(2024, 1, 4, 9, 0), t1);
        assertEquals(LocalDateTime.of(2024, 1, 5, 9, 0), t2);
    }

    @Test
    public void testGetPrioritizedSubTasks() {
        subtask.setStartTime(LocalDateTime.of(2024, 1, 4, 9, 0));
        subtask.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(subtask);

        subtask2.setStartTime(LocalDateTime.of(2024, 1, 5, 9, 0));
        subtask2.setDuration(Duration.ofMinutes(60));
        taskManager.createTask(subtask2);

        taskManager.createEpic(epic);

        List<Task> prioritized = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritized.size(), "Ожидалось 2 элемента в списке");

        LocalDateTime t1 = prioritized.get(0).getStartTime();
        LocalDateTime t2 = prioritized.get(1).getStartTime();

        assertEquals(LocalDateTime.of(2024, 1, 4, 9, 0), t1);
        assertEquals(LocalDateTime.of(2024, 1, 5, 9, 0), t2);
    }
}
