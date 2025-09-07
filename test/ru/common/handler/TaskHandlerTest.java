package ru.common.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.common.model.MockData;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskHandlerTest extends BaseHttpHandlerTest {

    private static Task task;

    @BeforeEach
    void setUpEach() throws IOException {
        task = new Task("Task", "", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        taskManager.createTask(task);
    }

    @Test
    void testGetAllTasks_Empty() throws Exception {
        HttpResponse<String> response = sendGetRequest("/tasks");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Task"));
    }

    @Test
    void testCreateTask() throws Exception {
        Task task = MockData.createTask1();
        task.setId(0);
        String taskJson = gson.toJson(task);

        HttpResponse<String> response = sendPostRequest("/tasks", taskJson);

        assertEquals(201, response.statusCode());
        assertNotNull(response.body());

        Task createdTask = gson.fromJson(response.body(), Task.class);
        assertEquals("Task", createdTask.getName());
        assertEquals(TaskStatus.NEW, createdTask.getTaskStatus());
        assertNotNull(createdTask.getStartTime());
        assertNotNull(createdTask.getDuration());
    }

    @Test
    void testGetTaskById_NotFound() throws Exception {
        HttpResponse<String> response = sendGetRequest("/tasks/999");

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("не найдена"));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task updatedTask = new Task(task.getId(), "Updated Task", "Updated description",
            TaskStatus.IN_PROGRESS, task.getStartTime().minusDays(1), task.getDuration());
        String updatedJson = gson.toJson(updatedTask);

        HttpResponse<String> response = sendPostRequest("/tasks/" + task.getId(), updatedJson);

        assertEquals(200, response.statusCode());
        assertEquals("Задача обновлена", response.body());
    }

    @Test
    void testDeleteTask() throws Exception {
        HttpResponse<String> response = sendDeleteRequest("/tasks/" + task.getId());

        assertEquals(200, response.statusCode());
        assertEquals("Задача удалена", response.body());
        HttpResponse<String> getResponse = sendGetRequest("/tasks/" + task.getId());
        assertEquals(404, getResponse.statusCode());
    }

    @Test
    void testDeleteAllTasks() throws Exception {
        Task task1 = MockData.createTask1();
        Task task2 = MockData.createTask2();
        sendPostRequest("/tasks", gson.toJson(task1));
        sendPostRequest("/tasks", gson.toJson(task2));

        HttpResponse<String> response = sendDeleteRequest("/tasks");

        assertEquals(200, response.statusCode());
        assertEquals("Все задачи удалены", response.body());

        HttpResponse<String> getAllResponse = sendGetRequest("/tasks");
        assertEquals(200, getAllResponse.statusCode());
        assertEquals("[]", getAllResponse.body());
    }
}