package ru.common.model;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.common.manager.*;

public class TaskTest {

    InMemoryTaskManager manager;

    @BeforeEach
    void createInMemoryManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void addNewTask() {
        Task task = new Task("Задача", "Задача для теста", TaskStatus.NEW);
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
        Task task = new Task("Новая задача", "Новая задача для теста", TaskStatus.NEW);
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
        Task task1 = new Task(1, "Задача 1", "");
        Task task2 = new Task(1,"Задача 1", "");
        Task task3 = new Task(2,"Задача 3", "");
        assertEquals(task1, task2, "Задачи должны быть равны.");
        assertEquals(task1.hashCode(), task2.hashCode(), "Задачи должны быть равны.");
        assertNotEquals(task2, task3, "Задачи должны отличаться");
        assertNotEquals(task1.hashCode(), task3.hashCode(), "Задачи должны отличаться");
    }

    @Test
    void tasksWithDifferentIdsShouldBeDifferent() {
        Task task1 = new Task("Task", "Description", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Task", "Description", TaskStatus.NEW);
        task2.setId(2);
        assertNotEquals(task1, task2, "Задачи должны отличаться");
    }
}
