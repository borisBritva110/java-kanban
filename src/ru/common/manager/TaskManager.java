package ru.common.manager;

import java.util.List;

import ru.common.exception.InteractionsException;
import ru.common.exception.NotFoundException;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;

public interface TaskManager {
    // Получение
    List<Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    Task getTaskById(int id) throws NotFoundException;

    Subtask getSubtaskById(int id) throws NotFoundException;

    Epic getEpicById(int id) throws NotFoundException;

    // Удаление
    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    void deleteTaskById(int id) throws NotFoundException;

    void deleteSubtaskById(int id) throws NotFoundException;

    void deleteEpicById(int id) throws NotFoundException;

    // Создание
    Task createTask(Task task) throws InteractionsException;

    Subtask createSubtask(Subtask subtask) throws NotFoundException, InteractionsException;

    Epic createEpic(Epic epic) throws InteractionsException;

    // Обновление сущности
    void updateTask(Task newTask, int id) throws NotFoundException, InteractionsException;

    void updateSubtask(Subtask newSubtask, int id) throws NotFoundException, InteractionsException;

    void updateEpic(Epic newEpic, int id) throws NotFoundException, InteractionsException;

    List<Task> getHistory();

    List<Subtask> getSubtasksFromEpic(Epic epic) throws NotFoundException;

    List<Task> getPrioritizedTasks();
}
