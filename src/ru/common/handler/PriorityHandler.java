package ru.common.handler;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import ru.common.manager.TaskManager;

public class PriorityHandler extends BaseHttpHandler {
    public PriorityHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String prioritizedJson = GSON.toJson(taskManager.getPrioritizedTasks());
            sendSuccess(exchange, prioritizedJson);
        } else {
            sendBadRequest(exchange, "Метод не поддерживается");
        }
    }
}
