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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void testSaveToFile() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Task task = new Task("Task", "", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "");

        manager.createEpic(epic);
        manager.createTask(task);

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(2, lines.size(), "Должно быть сохранено 2 элемента");
        assertTrue(lines.contains(manager.toString(task)));
        assertTrue(lines.contains(manager.toString(epic)));
    }
}