package de.lcpcraft.lucas.simplenick.utils;

import java.util.Arrays;
import java.util.UUID;

public class TexturesModel {

    private UUID id;
    private String name;
    private SkinProperty[] properties;

    public TexturesModel() {
    }

    public TexturesModel(UUID id, String name, SkinProperty[] properties) {
        this.id = id;
        this.name = name;
        this.properties = Arrays.copyOf(properties, properties.length);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SkinProperty[] getProperties() {
        return Arrays.copyOf(properties, properties.length);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "id=" + id +
                ", name='" + name + '\'' +
                ", properties=" + Arrays.toString(properties) +
                '}';
    }
}
