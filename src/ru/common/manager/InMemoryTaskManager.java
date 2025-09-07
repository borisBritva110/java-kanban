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

import ru.common.exception.InteractionsException;
import ru.common.exception.NotFoundException;
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
    public Task getTaskById(int id) throws NotFoundException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с id=" + id + " не найдена");
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) throws NotFoundException {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            throw new NotFoundException("Подзадача с id=" + id + " не найдена");
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) throws NotFoundException {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Эпик с id=" + id + " не найден");
        }
        historyManager.add(epic);
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
    public void deleteTaskById(int id) throws NotFoundException {
        Task task = tasks.remove(id);
        if (task == null) {
            throw new NotFoundException("Задача с id=" + id + " не найдена");
        }
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) throws NotFoundException {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            throw new NotFoundException("Подзадача с id=" + id + " не найдена");
        }
        historyManager.remove(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpicById(int id) throws NotFoundException {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Эпик с id=" + id + " не найден");
        }
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Task createTask(Task task) throws InteractionsException {
        if (isTaskIntersected(task)) {
            throw new InteractionsException("Время выполнения пересекается с другой задачей");
        }
        int entityId = id++;
        tasks.put(entityId, task);
        task.setId(entityId);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) throws NotFoundException, InteractionsException {
        if (isTaskIntersected(subtask)) {
            throw new InteractionsException("Время выполнения пересекается с другой задачей");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new NotFoundException("Эпик с id=" + subtask.getEpicId() + " не найден");
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
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) throws InteractionsException {
        if (isTaskIntersected(epic)) {
            throw new InteractionsException("Время выполнения пересекается с другой задачей");
        }
        int entityId = id++;
        epics.put(entityId, epic);
        epic.setId(entityId);
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        return epic;
    }

    @Override
    public void updateTask(Task newTask, int id) throws NotFoundException, InteractionsException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с id=" + id + " не найдена");
        }
        if (isTaskIntersected(newTask)) {
            throw new InteractionsException("Время выполнения пересекается с другой задачей");
        }
        prioritizedTasks.remove(task);
        tasks.put(id, newTask);
        newTask.setId(id);
        if (newTask.getStartTime() != null) {
            prioritizedTasks.add(newTask);
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask, int id) throws NotFoundException, InteractionsException {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            throw new NotFoundException("Подзадача с id=" + id + " не найдена");
        }
        if (isTaskIntersected(newSubtask)) {
            throw new InteractionsException("Время выполнения пересекается с другой задачей");
        }
        prioritizedTasks.remove(subtask);
        subtasks.put(id, newSubtask);
        newSubtask.setId(id);
        Epic epic = epics.get(newSubtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
        if (newSubtask.getStartTime() != null) {
            prioritizedTasks.add(newSubtask);
        }
    }

    @Override
    public void updateEpic(Epic newEpic, int id) throws NotFoundException, InteractionsException {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Эпик с id=" + id + " не найден");
        }
        if (isTaskIntersected(newEpic)) {
            throw new InteractionsException("Время выполнения пересекается с другой задачей");
        }
        prioritizedTasks.remove(epic);
        List<Subtask> subtasksByEpic = getSubtasksFromEpic(epic);
        for (Subtask subtask : subtasksByEpic) {
            subtask.setEpicId(epic.getId());
        }
        prioritizedTasks.remove(epic);
        updateEpicStatus(epic);
        if (newEpic.getStartTime() != null) {
            prioritizedTasks.add(newEpic);
        }
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(Epic epic) throws NotFoundException {
        if (epic == null) {
            throw new NotFoundException("Эпик не найден");
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
