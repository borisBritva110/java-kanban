package ru.common.handler;

import java.io.IOException;
import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;

import ru.common.manager.TaskManager;
import ru.common.model.Epic;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println("Началась обработка " + method + " /epics запроса от клиента.");

        switch(method) {
            case "POST":
                handlePost(exchange);
                break;
            case "GET":
                handleGet(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            default:
                sendBadRequest(exchange, "Метод не поддерживается");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPathParameter(exchange);
        if (idOpt.isPresent()) {
            int id = idOpt.get();
            Epic epic = taskManager.getEpicById(id);
            sendSuccess(exchange, GSON.toJson(epic));
        } else {
            sendSuccess(exchange, GSON.toJson(taskManager.getAllEpics()));
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Epic epic = GSON.fromJson(body, Epic.class);

        if (epic.getId() != 0) {
            taskManager.updateEpic(epic, epic.getId());
            sendSuccess(exchange, "Эпик обновлен");
        } else {
            Epic createdEpic = taskManager.createEpic(epic);
            sendCreated(exchange, GSON.toJson(createdEpic));
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPathParameter(exchange);
        if (idOpt.isPresent()) {
            int id = idOpt.get();
            taskManager.deleteEpicById(id);
            sendSuccess(exchange, "Эпик удален");
        } else {
            taskManager.deleteAllEpics();
            sendSuccess(exchange, "Все эпики удалены");
        }
    }
}
