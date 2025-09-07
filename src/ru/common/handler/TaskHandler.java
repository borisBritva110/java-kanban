package ru.common.handler;

import ru.common.exception.InteractionsException;
import ru.common.exception.NotFoundException;
import ru.common.manager.TaskManager;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Optional;

import ru.common.model.Task;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:
                    sendBadRequest(exchange, "Метод не поддерживается");
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (InteractionsException e) {
            sendInteractions(exchange, e.getMessage());
        } catch (Exception e) {
            sendInternalError(exchange, "Внутренняя ошибка сервера: " + e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPathParameter(exchange);
        if (idOpt.isPresent()) {
            int id = idOpt.get();
            Task task = taskManager.getTaskById(id);
            sendSuccess(exchange, GSON.toJson(task));
        } else {
            sendSuccess(exchange, GSON.toJson(taskManager.getAllTasks()));
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Task task = GSON.fromJson(body, Task.class);

        if (task.getId() != 0) {
            taskManager.updateTask(task, task.getId());
            sendSuccess(exchange, "Задача обновлена");
        } else {
            Task createdTask = taskManager.createTask(task);
            sendCreated(exchange, GSON.toJson(createdTask));
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPathParameter(exchange);
        if (idOpt.isPresent()) {
            int id = idOpt.get();
            taskManager.deleteTaskById(id);
            sendSuccess(exchange, "Задача удалена");
        } else {
            taskManager.deleteAllTasks();
            sendSuccess(exchange, "Все задачи удалены");
        }
    }
}