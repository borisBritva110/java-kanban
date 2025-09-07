package ru.common.handler;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Optional;

import ru.common.manager.TaskManager;
import ru.common.model.Subtask;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
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
        } catch (Exception e) {
            sendInternalError(exchange, "Внутренняя ошибка сервера");
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Subtask subtask = GSON.fromJson(body, Subtask.class);

        if (subtask.getId() != 0) {
            taskManager.updateSubtask(subtask, subtask.getId());
            sendSuccess(exchange, "Подзадача обновлена");
        } else {
            Subtask createdSubtask = taskManager.createSubtask(subtask);
            sendCreated(exchange, GSON.toJson(createdSubtask));
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPathParameter(exchange);
        if (idOpt.isPresent()) {
            int id = idOpt.get();
            Subtask subtask = taskManager.getSubtaskById(id);
            sendSuccess(exchange, GSON.toJson(subtask));
        } else {
            sendSuccess(exchange, GSON.toJson(taskManager.getAllSubtasks()));
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPathParameter(exchange);
        if (idOpt.isPresent()) {
            int id = idOpt.get();
            taskManager.deleteSubtaskById(id);
            sendSuccess(exchange, "Подзадача удалена");
        } else {
            taskManager.deleteAllSubtasks();
            sendSuccess(exchange, "Все подзадачи удалены");
        }
    }
}