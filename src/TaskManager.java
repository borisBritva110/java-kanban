import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static final int MIN_COMPLETED_SUBTASKS = 1;
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

    public void getEntityById(int id) {
        Task task = null;

        for (Integer index : tasks.keySet()) {
            if (index == id) {
                task = tasks.get(index);
            }
        }

        for (Integer index : epics.keySet()) {
            if (index == id) {
                task = epics.get(index);
            }
        }

        for (Integer index : subtasks.keySet()) {
            if (index == id) {
                task = subtasks.get(index);
            }
        }
        if (task == null) {
            System.out.println("Задача с id=" + id + " не найдена");
        } else {
            System.out.println(task);
        }
    }

    // Удаление
    public void deleteAllTasks() {
        tasks.clear();
        System.out.println("Задачи удалены");
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
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
        subtasks.remove(id);
        System.out.println("Подзадача удалена");
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.println("Эпик с id=" + id + " не найден");
            return;
        }

        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.remove(subtask.getId());
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
        for (Subtask subtask : newEpic.getSubtasks()) {
            subtask.setEpicId(epic.getId());
        }
        epics.put(id, newEpic);
        newEpic.setId(id);
        System.out.println("Эпик обновлен: " + newEpic);
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
            System.out.println("У эпика c id=" + epic.getId() + " отсутствуют подзадачи");
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
        } else if (completedSubtasks > MIN_COMPLETED_SUBTASKS){
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setTaskStatus(TaskStatus.NEW);
        }
    }
}
