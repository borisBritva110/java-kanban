package ru.common.manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import ru.common.model.Epic;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackendTaskManagerTest {

    @Test
    public void testSaveToFile() throws IOException {
        Path specificDirectory = Paths.get("/Users/m-imaeva/IdeaProjects/java-kanban/");
        if (!Files.exists(specificDirectory)) {
            Files.createDirectories(specificDirectory);
        }
        Path file = Files.createTempFile(specificDirectory, "test", ".txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task = new Task("Task", "", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "");

        manager.createEpic(epic);
        manager.createTask(task);

        List<String> lines = Files.readAllLines(file);
        assertEquals(2, lines.size(), "Должно быть сохранено 2 элемента");
        assertTrue(lines.contains(manager.toString(task)));
        assertTrue(lines.contains(manager.toString(epic)));

        Files.deleteIfExists(file);
    }
}
