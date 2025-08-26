package ru.common.manager;

import java.util.List;

import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

public interface TaskManager {
    // Получение
    List<Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    // Удаление
    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    // Создание
    Task createTask(Task task);

    Subtask createSubtask(Subtask subtask);

    Epic createEpic(Epic epic);

    // Обновление сущности
    void updateTask(Task newtTask, int id);

    void updateSubtask(Subtask newSubtask, int id);

    void updateEpic(Epic newEpic, int id);

    List<Task> getHistory();

    List<Subtask> getSubtasksFromEpic(Epic epic);

    List<Task> getPrioritizedTasks();
}
