package ru.common.handler;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

import ru.common.manager.TaskManager;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String historyJson = GSON.toJson(taskManager.getHistory());
            sendSuccess(exchange, historyJson);
        } else {
            sendBadRequest(exchange, "Метод не поддерживается");
        }
    }
}
