package ru.common.manager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpServer;

import ru.common.handler.EpicHandler;
import ru.common.handler.HistoryHandler;
import ru.common.handler.PriorityHandler;
import ru.common.handler.SubtaskHandler;
import ru.common.handler.TaskHandler;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        getEndpoints();
    }

    public void getEndpoints() {
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    public static void main(String[] args) throws IOException {
        Path dir = Paths.get("/Users/m-imaeva/IdeaProjects/java-kanban/src");
        Path tempFile = Files.createTempFile(dir, "ManagerTask", ".txt");
        TaskManager taskManager = Managers.getDefault(tempFile);
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
        server.stop();
    }
}