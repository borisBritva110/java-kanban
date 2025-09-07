package ru.common.model;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.common.manager.*;

public class TaskTest {

    private InMemoryTaskManager manager;
    private Task task;
    private Task task2;

    @BeforeEach
    void createInMemoryManager() {
        manager = new InMemoryTaskManager();
        task = MockData.createTask1();
        task2 = MockData.createTask2();
    }

    @Test
    void addNewTask() {
        manager.createTask(task);
        Task savedTask = manager.getTaskById(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateTaskStatus() {
        manager.createTask(task);
        Task oldTask = manager.getTaskById(task.getId());

        manager.updateTask(
            new Task("Обновленная задача", "Описание", TaskStatus.IN_PROGRESS),
            task.getId()
        );
        Task newTask = manager.getTaskById(task.getId());

        assertNotEquals(oldTask, newTask);
        assertEquals(oldTask.getId(), newTask.getId());
    }

    @Test
    void shouldTaskEqualIfTheyHaveSameValues (){
        int randomId = MockData.nextId++;
        Task task3 = new Task(randomId,"Задача 3", "", TaskStatus.NEW);
        assertNotEquals(task2, task3, "Задачи должны отличаться");
        assertNotEquals(task.hashCode(), task3.hashCode(), "Задачи должны отличаться");
    }

    @Test
    void tasksWithDifferentIdsShouldBeDifferent() {
        task.setId(1);
        task2.setId(2);
        assertNotEquals(task, task2, "Задачи должны отличаться");
    }

    @Test
    void isTaskIntersected() {
        manager.createTask(task);
        manager.createTask(task2);

        Task candidate = new Task("Task", "Описание", TaskStatus.NEW, null, Duration.ofMinutes(15));

        boolean intersected = manager.isTaskIntersected(candidate);
        assertFalse(intersected, "Задача без начала времени не должна пересекаться с существующими задачами");
    }
}
