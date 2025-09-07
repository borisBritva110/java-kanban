package ru.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class GsonFactory {
    public static Gson createGson() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new GsonDurationAdapter())
            .create();
    }
}