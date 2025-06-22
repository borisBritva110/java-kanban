import java.util.ArrayList;
import java.util.Scanner;

import ru.common.manager.TaskManager;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

public class Main {
    private static final String ERROR_TEXT_MESSAGE = "Команда не найдена";

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Task task = new Task("Задача", "TEST1", TaskStatus.NEW);
        manager.createTask(task);
        Task task2 = new Task("Задача2", "TEST2", TaskStatus.NEW);
        manager.createTask(task2);

        Epic epic = new Epic("Эпик5", "TEST5");
        manager.createEpic(epic);
        Epic epic2 = new Epic("Эпик6", "TEST6");
        manager.createEpic(epic2);

        Subtask subtask = new Subtask(epic.getId(), "Подзадача3", "TEST3", TaskStatus.NEW);
        manager.createSubtask(subtask);

        Subtask subtask2 = new Subtask(epic2.getId(), "Подзадача4", "TEST4", TaskStatus.NEW);
        manager.createSubtask(subtask2);

        printMenu();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            int command = scanner.nextInt();
            int choice;
            int entityId;

            if (command == 1) {
                System.out.println("Какого типа задачи необходимо получить? 1 - задачи, 2 - подзадачи, 3 - эпики");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        ArrayList<Task> tasks = manager.getAllTasks();
                        System.out.println(tasks);
                        break;
                    case 2:
                        ArrayList<Subtask> subtasks = manager.getAllSubtasks();
                        System.out.println(subtasks);
                        break;
                    case 3:
                        ArrayList<Epic> epics = manager.getAllEpics();
                        System.out.println(epics);
                        break;
                    default:
                        System.out.println(ERROR_TEXT_MESSAGE);
                }
            } else if (command == 2) {
                System.out.println("Какого типа задачи необходимо удалить? 1 - задачи, 2 - подзадачи, 3 - эпики");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        manager.deleteAllTasks();
                        break;
                    case 2:
                        manager.deleteAllSubtasks();
                        break;
                    case 3:
                        manager.deleteAllEpics();
                        break;
                    default:
                        System.out.println(ERROR_TEXT_MESSAGE);
                }
            } else if (command == 3) {
                System.out.println("Введите id");
                entityId = scanner.nextInt();
                Task taskById = manager.getTaskById(entityId);
                // ru.common.model.Subtask subtaskById = manager.getSubtaskById(entityId);
                // ru.common.model.Epic epicById = manager.getEpicById(entityId);
            } else if (command == 4) {
                System.out.println("Какой тип задачи необходимо создать? 1 - задача, 2 - подзадача, 3 - эпик");
                Task task3 = new Task("Тестовая задача", "Просто задача", TaskStatus.DONE);
                manager.createTask(task3);
                Epic epic3 = new Epic("Тестовый эпик", "");
                manager.createEpic(epic3);
                Subtask subtask3 = new Subtask(epic3.getId(), "Подзадача", "", TaskStatus.IN_PROGRESS);
                manager.createSubtask(subtask3);
            } else if (command == 5) {
                System.out.println("Какого типа задачу необходимо отредактировать? 1 - задача, 2 - подзадача, 3 - эпик");
                entityId = scanner.nextInt();
                manager.updateTask(task, entityId);
                // manager.updateEpic(epic, entityId);
                // manager.updateSubtask(subtask, entityId);
            } else if (command == 6) {
                 manager.deleteTaskById(task.getId());
                // manager.deleteEpicById(epic.getId());
                // manager.deleteSubtaskById(subtask.getId());
            } else if (command == 7) {
                System.out.println("Введите id");
                ArrayList<Subtask> subtasks = manager.getSubtasksFromEpic(epic2);
                System.out.println(subtasks);
            }
        }
    }

    public static void printMenu() {
        System.out.println("Список пользовательских команд:");
        System.out.println("1 - Получить список задач");
        System.out.println("2 - Удалить задачи");
        System.out.println("3 - Получить задачу по id");
        System.out.println("4 - Создать задачу");
        System.out.println("5 - Обновить задачу");
        System.out.println("6 - Удаление задачи по id");
        System.out.println("7 - Получить список подзадач для эпика");
    }
}
