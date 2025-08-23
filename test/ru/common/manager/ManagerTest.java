package ru.common.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ManagerTest {

    @Test
    public void testGetDefaultTaskService() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        assertNotNull(inMemoryTaskManager, "getDefault должен возвращать экземпляр InMemoryTaskService");
        assertEquals(InMemoryTaskManager.class, inMemoryTaskManager.getClass(), "getDefault должен возвращать" +
            " экземпляр InMemoryTaskService");
    }

    @Test
    public void testGetDefaultHistoryService() {
        HistoryManager historyService = new InMemoryHistoryManager();
        assertNotNull(historyService, "getDefaultHistory должен возвращать экземпляр HistoryService");
        assertEquals(InMemoryHistoryManager.class, historyService.getClass(), "getDefaultHistory должен " +
            "возвращать объект InMemoryHistoryService");
    }
}