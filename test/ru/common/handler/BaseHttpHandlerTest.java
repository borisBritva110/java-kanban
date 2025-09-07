package ru.common.handler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.common.manager.HttpTaskServer;
import ru.common.manager.InMemoryTaskManager;
import ru.common.manager.TaskManager;
import ru.common.util.GsonDurationAdapter;
import ru.common.util.GsonLocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BaseHttpHandlerTest {
    protected HttpTaskServer server;
    protected TaskManager taskManager;
    protected HttpClient client;
    protected static final String BASE_URL = "http://localhost:8080";
    protected final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
        .registerTypeAdapter(Duration.class, new GsonDurationAdapter())
        .create();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    protected HttpResponse<String> sendGetRequest(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + endpoint))
            .GET()
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> sendPostRequest(String endpoint, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + endpoint))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> sendDeleteRequest(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + endpoint))
            .DELETE()
            .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}