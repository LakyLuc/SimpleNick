package de.lcpcraft.lucas.simplenick.utils;

import com.github.javafaker.Faker;

import java.security.SecureRandom;

public class NameCreator {
    public static String funnyName() {
        return optimize(new Faker().funnyName().name());
    }

    public static String username() {
        return optimize(new Faker().name().username());
    }

    public static String randomName() {
        if (new SecureRandom().nextBoolean())
            return funnyName();
        else return username();
    }

    private static String optimize(String fakeName) {
        fakeName = filterAllowedChars(fakeName);
        fakeName = trimLength(fakeName);
        return fakeName;
    }

    private static String filterAllowedChars(String fakeName) {
        return fakeName.replaceAll("[^a-zA-Z0-9_]", "");
    }

    private static String trimLength(String fakeName) {
        if (fakeName.length() > 16)
            fakeName = fakeName.substring(0, 16);
        return fakeName;
    }
}
