package ru.common.manager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime).thenComparing(Task::getId));

    private static int id = 1;

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("Задача с id=" + id + " не найдена");
        } else {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Подзадача с id=" + id + " не найдена");
        } else {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик с id=" + id + " не найден");
        } else {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        tasks.keySet().forEach(historyManager::remove);
        System.out.println("Задачи удалены");
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        subtasks.keySet().forEach(historyManager::remove);
        for (Epic epic : epics.values()) {
            epic.setSubtaskIds(new ArrayList<>());
            epic.setTaskStatus(TaskStatus.NEW);
            System.out.println("Статус эпика с id=" + epic.getId() + " изменен на NEW");
        }
        System.out.println("Подзадачи удалены");

    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        epics.keySet().forEach(historyManager::remove);
        subtasks.clear();
        System.out.println("Эпики удалены");
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
        System.out.println("Задача удалена");
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        historyManager.remove(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            updateEpicStatus(epic);
        }
        System.out.println("Подзадача удалена");
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик с id=" + id + " не найден");
            return;
        }

        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(id);
        }
        epics.remove(id);
        historyManager.remove(id);
        System.out.println("Эпик и подзадачи эпика удалены");
    }

    @Override
    public Task createTask(Task task) {
        if (isTaskIntersected(task)) {
            throw new RuntimeException("Время выполнения пересекается с другой задачей");
        }
        int entityId = id++;
        tasks.put(entityId, task);
        task.setId(entityId);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        System.out.println("Создана задача с id: " + entityId);
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (isTaskIntersected(subtask)) {
            throw new RuntimeException("Время выполнения пересекается с другой задачей");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            System.out.println("Невозможно создать подзадачу вне эпика");
            return null;
        }
        int entityId = id++;
        subtasks.put(entityId, subtask);
        subtask.setId(entityId);

        if (!epic.getSubtaskIds().contains(entityId)) {
            epic.getSubtaskIds().add(entityId);
        }
        updateEpicStatus(epic);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        System.out.println("Создана подзадача с id: " + entityId);
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (isTaskIntersected(epic)) {
            throw new RuntimeException("Время выполнения пересекается с другой задачей");
        }
        int entityId = id++;
        epics.put(entityId, epic);
        epic.setId(entityId);
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        System.out.println("Создан эпик с id: " + entityId);
        return epic;
    }

    @Override
    public void updateTask(Task newtTask, int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("Задача не была найдена");
            return;
        }
        if (isTaskIntersected(newtTask)) {
            throw new RuntimeException("Время выполнения пересекается с другой задачей");
        }
        prioritizedTasks.remove(task);
        tasks.put(id, newtTask);
        newtTask.setId(id);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        System.out.println("Задача обновлена: " + newtTask);
    }

    @Override
    public void updateSubtask(Subtask newSubtask, int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.println("Подзадача не была найдена");
            return;
        }
        if (isTaskIntersected(newSubtask)) {
            throw new RuntimeException("Время выполнения пересекается с другой задачей");
        }
        prioritizedTasks.remove(subtask);
        subtasks.put(id, newSubtask);
        newSubtask.setId(id);
        Epic epic = epics.get(newSubtask.getEpicId());
        updateEpicStatus(epic);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        System.out.println("Подзадача обновлена: " + newSubtask);
    }

    @Override
    public void updateEpic(Epic newEpic, int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик не был найден");
            return;
        }
        if (isTaskIntersected(newEpic)) {
            throw new RuntimeException("Время выполнения пересекается с другой задачей");
        }
        prioritizedTasks.remove(epic);
        List<Subtask> subtasksByEpic = getSubtasksFromEpic(epic);
        for (Subtask subtask : subtasksByEpic) {
            subtask.setEpicId(epic.getId());
        }
        prioritizedTasks.remove(epic);
        updateEpicStatus(epic);
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        System.out.println("Эпик с id=" + id + " был обновлен.");
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Эпик с id=" + epic.getId() + " не был найден");
            return null;
        }
        List<Subtask> subtasksByEpic = new ArrayList<>();
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
        List<Subtask> allSubtasksFromEpicById = getSubtasksFromEpic(epic);
        if (allSubtasksFromEpicById.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            epic.setStartTime(null);
            System.out.println("Статус эпика с id=" + epic.getId() + " изменен на NEW, так как отсутствуют подзадачи");
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        List<Subtask> sortedSubtask = allSubtasksFromEpicById.stream().sorted(Comparator.comparing(Task::getStartTime)).toList();

        for (Subtask subtask : sortedSubtask) {
            if (subtask.getTaskStatus() != TaskStatus.NEW) {
                allNew = allNew && (subtask.getTaskStatus() == TaskStatus.NEW);
                allDone = allDone && (subtask.getTaskStatus() == TaskStatus.DONE);
            }
        }

        if (allNew) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
        updateEpicTimeFields(epic, sortedSubtask);
        System.out.println("Статус эпика с id=" + epic.getId() + " изменен на " + epic.getTaskStatus());
    }

    public void updateEpicTimeFields(Epic epic, List<Subtask> sortedSubtask) {
        if (sortedSubtask.isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
            return;
        }
        epic.setStartTime(sortedSubtask.getFirst().getStartTime());
        epic.setEndTime(sortedSubtask.getLast().getEndTime());

        if (epic.getStartTime() != null && epic.getEndTime() != null) {
            Duration epicDuration = Duration.between(epic.getStartTime(), epic.getEndTime());
            epic.setDuration(epicDuration);
        } else {
            epic.setDuration(Duration.ZERO);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return tasks.values().stream().filter(t -> t.getStartTime() != null)
            .sorted(Comparator.comparing(Task::getStartTime)).collect(Collectors.toList());
    }

    public boolean isTaskIntersected(Task task) {
        LocalDateTime timeTaskStarted = task.getStartTime();
        if (timeTaskStarted == null) {
            return false;
        }

        LocalDateTime timeTaskFinished = task.getEndTime();
        for (Task existingTask : prioritizedTasks) {
            if (existingTask.getStartTime() == null || existingTask.getEndTime() == null) {
                continue;
            }
            LocalDateTime existingStart = existingTask.getStartTime();
            LocalDateTime existingEnd = existingTask.getEndTime();
            boolean isIntersected = timeTaskStarted.isBefore(existingEnd) &&
                timeTaskFinished.isAfter(existingStart);

            if (isIntersected) {
                return true;
            }
        }
        return false;
    }
}
