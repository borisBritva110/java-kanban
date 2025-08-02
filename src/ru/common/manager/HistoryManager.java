package ru.common.manager;

import java.util.List;

import ru.common.model.Task;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
