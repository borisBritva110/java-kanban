package ru.common.manager;

import java.nio.file.Path;

public class Managers {
    public static InMemoryTaskManager getDefault(Path fileName) {
        return new FileBackedTaskManager(fileName);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}