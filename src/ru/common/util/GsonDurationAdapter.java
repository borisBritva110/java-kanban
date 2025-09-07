package ru.common.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class GsonDurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration value) throws IOException {
        jsonWriter.value(value.toMinutes());
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        int minutes = jsonReader.nextInt();
        if (minutes == 0) {
            return Duration.ZERO;
        }
        return Duration.ofMinutes(minutes);
    }
}