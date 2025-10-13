package ru.common.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import ru.common.exception.InteractionsException;
import ru.common.exception.ManagerSaveException;
import ru.common.exception.NotFoundException;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;
import ru.common.model.TaskType;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static String DELIMITER = ",";

    private Path file;

    public FileBackedTaskManager(Path file) {
        this.file = file;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
            for (Task task: tasks.values()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Subtask subtask : subtasks.values()) {
                writer.write(toString(subtask));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(toString(epic));
                writer.newLine();
            }
        } catch (FileNotFoundException exception) {
            exception.getStackTrace();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при работе с файлом");
        }
    }

    public static FileBackedTaskManager loadFromFile(Path pathFile) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(pathFile);
        try (BufferedReader br = new BufferedReader(new FileReader(pathFile.toFile()))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    Task task = fromString(line);
                    if (task != null) {
                        if (task.getTaskType() == TaskType.TASK) {
                            fileBackedTaskManager.tasks.put(task.getId(), task);
                        } else if (task.getTaskType() == TaskType.SUBTASK) {
                            fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                        } else if (task.getTaskType() == TaskType.EPIC) {
                            fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                        }
                    }
                }
            }
        } catch (FileNotFoundException exception) {
            exception.getStackTrace();
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при работе с файлом");
        }
        return fileBackedTaskManager;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = super.getAllTasks();
        save();
        return tasks;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        List<Subtask> subtasks = super.getAllSubtasks();
        save();
        return subtasks;
    }

    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epics = super.getAllEpics();
        save();
        return epics;
    }

    @Override
    public Task getTaskById(int id) throws NotFoundException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) throws NotFoundException {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) throws NotFoundException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteTaskById(int id) throws NotFoundException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws NotFoundException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws NotFoundException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task createTask(Task task)  throws InteractionsException {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask)  throws InteractionsException {
        Subtask newSubtask = super.createSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic createEpic(Epic epic) throws InteractionsException {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public void updateTask(Task newtTask, int id) throws NotFoundException, InteractionsException {
        super.updateTask(newtTask, id);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask, int id) throws NotFoundException, InteractionsException {
        super.updateSubtask(newSubtask, id);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic, int id) throws NotFoundException, InteractionsException {
        super.updateEpic(newEpic, id);
        save();
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        List<Subtask> subtasks = super.getSubtasksFromEpic(epic);
        save();
        return subtasks;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public String toString(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId()).append(DELIMITER);
        stringBuilder.append(task.getName()).append(DELIMITER);
        stringBuilder.append(task.getTaskType()).append(DELIMITER);
        stringBuilder.append(task.getTaskStatus()).append(DELIMITER);
        stringBuilder.append(task.getDescription()).append(DELIMITER);
        long duration = task.getDuration() != null ? task.getDuration().toMinutes() : 0;
        stringBuilder.append(duration).append(DELIMITER);
        String startTime = task.getStartTime() != null ? task.getStartTime().toString() : "null";
        stringBuilder.append(startTime).append(DELIMITER);

        if (task.getTaskType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            stringBuilder.append(subtask.getEpicId());
        }
        return stringBuilder.toString();
    }

    public static Task fromString(String str) {
        String[] split = str.split(",");
        int id = 0;
        String name = "";
        TaskType taskType = null;
        TaskStatus status = null;
        String description = "";
        Integer epicId = null;
        LocalDateTime startTime = null;
        Duration duration = Duration.ZERO;

        try {
            id = Integer.parseInt(split[0]);
            name = split[1];
            taskType = TaskType.valueOf(split[2]);
            status = TaskStatus.valueOf(split[3]);
            duration = Duration.ofMinutes(Long.parseLong(split[5]));
            startTime = split.length > 6 && !"null".equals(split[6]) ? LocalDateTime.parse(split[6]) : null;

            if (split.length > 4) {
                description = split[4];
            }

            if (taskType == TaskType.SUBTASK && split.length > 5) {
                epicId = Integer.parseInt(split[7]);
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | DateTimeParseException e) {
            System.out.println("Ошибка при чтении задачи: " + str);
            return null;
        }

        switch (taskType) {
            case taskType.TASK -> {
                Task task = new Task(name, description, status, startTime, duration);
                task.setId(id);
                return task;
            }

            case taskType.EPIC -> {
                Epic epic = new Epic(id, name, description, status, startTime, duration);
                epic.setId(id);
                return epic;
            }

            case taskType.SUBTASK -> {
                Subtask subtask = new Subtask(id, name, description, status, startTime, duration, epicId);
                subtask.setId(id);
                return subtask;
            }
            default ->
                System.out.println("Тип задачи не определен");
        }
        return null;
    }
}
