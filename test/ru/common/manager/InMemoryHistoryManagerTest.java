package ru.common.manager;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private InMemoryTaskManager manager;
    private static Task task1;
    private Task task2;
    private Epic epic;
    private Subtask subTask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Описание 1", TaskStatus.NEW);
        task2 = new Task("Task 2", "Описание 2", TaskStatus.NEW);
        epic = new Epic("Epic ", "Описание");
        subTask = new Subtask(epic.getId(), "SubTask", "Описание", TaskStatus.NEW);
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
        assertEquals(updatedHistory.get(2).getId(), task1.getId(), "Объекты не совпадают");
    }

    @Test
    public void testGetTaskHistoryListWithDifferentTasks() {
        historyManager.add(task1);
        historyManager.add(subTask);
        historyManager.add(epic);

        Task newTask = new Task(1, "Задача", "");
        manager.createTask(newTask);
        historyManager.add(newTask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "В истории отсутствуют задачи.");
        assertEquals(4, history.size(), "Количество задач отличается.");

        assertEquals(history.get(3).getId(), newTask.getId(), "Объекты не совпадают");
        assertEquals(history.get(0).getId(), task1.getId(), "Объекты не совпадают");
    }

    @Test
    public void canAddSameTasksToTheHistory() {
        Task task1 = new Task("Задача", "Описание 1", TaskStatus.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Задача", "Описание 2", TaskStatus.NEW);
        manager.createTask(task2);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        assertEquals(3,  manager.getHistory().size(), "Количество задач отличается.");
    }
}
