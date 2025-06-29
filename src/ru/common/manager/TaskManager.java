package ru.common.manager;

import java.util.ArrayList;
import java.util.HashMap;

import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int id = 1;

    // Получение
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("Задача с id=" + id + " не найдена");
        }
        return task;
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Подзадача с id=" + id + " не найдена");
        }
        return subtask;
    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик с id=" + id + " не найден");
        }
        return epic;
    }

    // Удаление
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Задачи удалены");
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setSubtaskIds(new ArrayList<>());
            epic.setTaskStatus(TaskStatus.NEW);
            System.out.println("Статус эпика с id=" + epic.getId() + " изменен на NEW");
        }
        System.out.println("Подзадачи удалены");

    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
        System.out.println("Эпики удалены");
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
        System.out.println("Задача удалена");
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            updateEpicStatus(epic);
        }
        System.out.println("Подзадача удалена");
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик с id=" + id + " не найден");
            return;
        }

        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
        System.out.println("Эпик и подзадачи эпика удалены");
    }

    // Создание
    public void createTask(Task task) {
        int entityId = id++;
        tasks.put(entityId, task);
        task.setId(entityId);
        System.out.println("Создана задача с id: " + entityId);
    }

    public void createSubtask(Subtask subtask) {
        int entityId = id++;
        subtasks.put(entityId, subtask);
        subtask.setId(entityId);
        Epic epic = epics.get(subtask.getEpicId());
        if (!epic.getSubtaskIds().contains(entityId)) {
            epic.getSubtaskIds().add(entityId);
        }
        updateEpicStatus(epic);
        System.out.println("Создана подзадача с id: " + entityId);
    }

    public void createEpic(Epic epic) {
        int entityId = id++;
        epics.put(entityId, epic);
        epic.setId(entityId);
        System.out.println("Создан эпик с id: " + entityId);
    }

    // Обновление сущности
    public void updateTask(Task newtTask, int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("Задача не была найдена");
            return;
        }
        tasks.put(id, newtTask);
        newtTask.setId(id);
        System.out.println("Задача обновлена: " + newtTask);
    }

    public void updateSubtask(Subtask newSubtask, int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Подзадача не была найдена");
            return;
        }
        subtasks.put(id, newSubtask);
        newSubtask.setId(id);
        Epic epic = epics.get(newSubtask.getEpicId());
        updateEpicStatus(epic);
        System.out.println("Подзадача обновлена: " + newSubtask);
    }

    public void updateEpic(Epic newEpic, int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик не был найден");
            return;
        }

        ArrayList<Subtask> subtasksByEpic = getSubtasksFromEpic(epic);
        for (Subtask subtask : subtasksByEpic) {
            subtask.setEpicId(epic.getId());
        }

        updateEpicStatus(epic);
        System.out.println("Эпик с id=" + id + " был обновлен.");
    }

    public ArrayList<Subtask> getSubtasksFromEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Эпик с id=" + epic.getId() + " не был найден");
            return null;
        }
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                subtasksByEpic.add(subtask);
            }
        }
        return subtasksByEpic;
    }

    public void updateEpicStatus(Epic epic) {
        if (epic == null) {
            System.out.println("Эпик не был найден");
            return;
        }
        ArrayList<Subtask> allSubtasksFromEpicById = getSubtasksFromEpic(epic);
        if (allSubtasksFromEpicById.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            System.out.println("Статус эпика с id=" + epic.getId() + " изменен на NEW, так как отсутствуют подзадачи");
            return;
        }

        int completedSubtasks = 0;
        for (Subtask subtask : allSubtasksFromEpicById) {
            if (subtask.getTaskStatus() == TaskStatus.DONE) {
                completedSubtasks++;
            }
        }

        if (completedSubtasks == allSubtasksFromEpicById.size()) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            boolean hasSubtaskInProgress = allSubtasksFromEpicById.stream().anyMatch(
                subtask -> subtask.getTaskStatus() == TaskStatus.IN_PROGRESS
            );
            if (!hasSubtaskInProgress) {
                epic.setTaskStatus(TaskStatus.NEW);
            } else {
                epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }
}
