package de.lcpcraft.lucas.simplenick.utils;

public class SkinProperty {

    public static final String SKIN_KEY = "textures";

    private final String name = SKIN_KEY;
    private String value;
    private String signature;

    public SkinProperty() {
    }

    public SkinProperty(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
