package ru.common.manager;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.common.model.Epic;
import ru.common.model.MockData;
import ru.common.model.Subtask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private InMemoryTaskManager manager;
    private static Task task1;
    private Task task2;
    private Epic epic;
    private Subtask subTask;

    @BeforeEach
    void setUp() {
        this.manager = Managers.getDefault();
        this.historyManager = Managers.getDefaultHistory();
        task1 = MockData.createTask1();
        task2 = MockData.createTask2();
        epic = MockData.createEpic();
        subTask = MockData.createSubTask(epic);
        epic.addSubtaskId(subTask.getId());
    }

    @Test
    public void testAddTaskToHistory() {
        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Количество задач отличается.");
        assertEquals(task1, history.getFirst(), "Задачи не совпадают.");
    }

    @Test
    public void testGetTaskHistoryList() {
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "В истории отсутствуют задачи.");
        assertEquals(2, history.size(), "Количество задач отличается.");
        assertEquals(task1, history.get(0), "Задачи не совпадают.");
        assertEquals(task2, history.get(1), "Задачи не совпадают.");

        historyManager.add(subTask);
        List<Task> updatedHistory = historyManager.getHistory();
        assertNotNull(history, "В истории отсутствуют задачи.");
        assertEquals(3, updatedHistory.size(), "Количество задач отличается.");
        assertEquals(updatedHistory.get(2).getId(), subTask.getId(), "Объекты не совпадают");
        assertEquals(updatedHistory.get(1).getId(), task2.getId(), "Объекты не совпадают");
    }

    @Test
    public void testGetTaskHistoryListWithDifferentTasks() {
        historyManager.add(task1);
        historyManager.add(subTask);
        historyManager.add(epic);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "В истории отсутствуют задачи.");
        assertEquals(4, history.size(), "Количество задач отличается.");
        assertEquals(history.get(0).getId(), task1.getId(), "Объекты не совпадают");
    }

    @Test
    public void canAddSameTasksToTheHistory() {
        manager.createTask(task1);
        manager.createTask(task2);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        assertEquals(2,  manager.getHistory().size(), "Количество задач отличается.");
    }

    @Test
    public void testRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(2, historyManager.getHistory().size());

        historyManager.remove(task1.getId());
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
    }

    @Test
    public void testRemoveNonExistentTask() {
        historyManager.add(task1);
        historyManager.remove(999);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    public void testTaskAddedToHistoryAndRemoved() {
        Task task = MockData.createTask1();
        Task createdTask = manager.createTask(task);
        assertNotNull(createdTask, "Задача не создана");

        Task retrievedTask = manager.getTaskById(createdTask.getId());
        assertNotNull(retrievedTask, "Задача не найдена");
        List<Task> historyAfterGet = manager.getHistory();
        assertEquals(1, historyAfterGet.size(), "Количество элементов в истории не совпадает");
        assertEquals(createdTask.getId(), historyAfterGet.get(0).getId(), "Задача в истории не совпадает с полученной");

        manager.deleteTaskById(createdTask.getId());
        List<Task> historyAfterDelete = manager.getHistory();
        assertEquals(0, historyAfterDelete.size(), "После удаления задачи история должна быть очищена");
    }
}
