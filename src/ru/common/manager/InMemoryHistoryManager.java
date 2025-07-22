package ru.common.manager;

import java.util.ArrayList;
import java.util.List;

import ru.common.model.Task;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyEntities = new ArrayList<>();
    static final int MAX_REQUESTED_ENTITIES = 10;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyEntities.size() >= MAX_REQUESTED_ENTITIES) {
            historyEntities.removeFirst();
        }
        historyEntities.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(historyEntities);
    }
}
