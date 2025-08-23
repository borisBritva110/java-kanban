package ru.common.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.common.model.Epic;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackendTaskManagerTest {

    private Path tempDir;
    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("kanbanTest");
        tempFile = Files.createTempFile(tempDir, "test", ".txt");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempDir);
    }

    @Test
    public void testSaveToFile() {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        Task task = new Task("Task", "", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "");
        manager.createEpic(epic);
        manager.createTask(task);
        manager.save();
        FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = manager.getAllTasks();
        List<Epic> epics = manager.getAllEpics();

        assertEquals(tasks.size(), 1, "Количество задач отличается");
        assertEquals(epics.size(), 1, "Количество задач отличается");
    }
}