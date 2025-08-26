import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import ru.common.manager.InMemoryTaskManager;
import ru.common.model.Epic;
import ru.common.model.Subtask;
import ru.common.model.Task;
import ru.common.model.TaskStatus;

public class Main {
    private static final String ERROR_TEXT_MESSAGE = "Команда не найдена";

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        LocalDateTime time = LocalDateTime.now().minusDays(2);
        Duration duration = Duration.ofMinutes(15);
        Task task = new Task("Задача", "TEST1", TaskStatus.NEW, time, duration);
        manager.createTask(task);
        Task task2 = new Task("Задача2", "TEST2", TaskStatus.NEW, time, duration);
        manager.createTask(task2);

        Epic epic = new Epic("Эпик5", "TEST5");
        manager.createEpic(epic);
        Epic epic2 = new Epic("Эпик6", "TEST6");
        manager.createEpic(epic2);

        Subtask subtask = new Subtask("Подзадача3", "TEST3", TaskStatus.NEW, time, duration, epic.getId());
        manager.createSubtask(subtask);

        Subtask subtask2 = new Subtask("Подзадача4", "TEST4", TaskStatus.IN_PROGRESS, time, duration, epic.getId());
        manager.createSubtask(subtask2);

        Subtask subtask3 = new Subtask( "Подзадача", "TEST4", TaskStatus.NEW, time, duration, epic.getId());
        manager.createSubtask(subtask3);
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
                        List<Task> tasks = manager.getAllTasks();
                        System.out.println(tasks);
                        break;
                    case 2:
                        List<Subtask> subtasks = manager.getAllSubtasks();
                        System.out.println(subtasks);
                        break;
                    case 3:
                        List<Epic> epics = manager.getAllEpics();
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
                System.out.println(taskById);
            } else if (command == 4) {
                System.out.println("Какой тип задачи необходимо создать? 1 - задача, 2 - подзадача, 3 - эпик");
                Task task3 = new Task("Тестовая задача", "Просто задача", TaskStatus.DONE, time, duration);
                manager.createTask(task3);
                Epic epic3 = new Epic("Тестовый эпик", "");
                manager.createEpic(epic3);
            } else if (command == 5) {
                System.out.println("Какого типа задачу необходимо отредактировать? 1 - задача, 2 - подзадача, 3 - эпик");
            } else if (command == 6) {
                 manager.deleteTaskById(task.getId());
                 manager.deleteEpicById(epic.getId());
                 manager.deleteSubtaskById(subtask.getId());
            } else if (command == 7) {
                System.out.println("Введите id");
                List<Subtask> subtasks = manager.getSubtasksFromEpic(epic2);
                System.out.println(subtasks);
            } else if (command == 8) {
                List<Task> tasks = manager.getHistory();
                System.out.println(tasks);
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
        System.out.println("8 - Получить историю запросов");
    }
}
