package de.lcpcraft.lucas.simplenick.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDTypeAdapter extends TypeAdapter<UUID> {

    public static final Pattern UUID_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

    public static UUID parseId(String withoutDashes) {
        return UUID.fromString(UUID_PATTERN.matcher(withoutDashes).replaceAll("$1-$2-$3-$4-$5"));
    }

    public static String toMojangId(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public void write(JsonWriter out, UUID value) throws IOException {
        TypeAdapters.STRING.write(out, toMojangId(value));
    }

    public UUID read(JsonReader in) throws IOException {
        return parseId(TypeAdapters.STRING.read(in));
    }
}
