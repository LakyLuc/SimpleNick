package de.lcpcraft.lucas.simplenick.utils;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lcpcraft.lucas.simplenick.SimpleNick;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class SkinManager {
    private static final String SKIN_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private final Map<TexturesModel, Long> cache;

    public SkinManager(Map<TexturesModel, Long> cache) {
        this.cache = cache;
        Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aLoaded §6" + cache.size() + " §askin(s)");
    }

    public Optional<TexturesModel> getSkin(UUID ownerUUID) {
        if (ownerUUID == null)
            return Optional.empty();
        Optional<TexturesModel> cached = cache.keySet().stream().filter(texturesModel -> texturesModel.getId().toString().equals(ownerUUID.toString())).findFirst();
        if (cached.isPresent()) {
            if (System.currentTimeMillis() - cache.get(cached.get()) > 1000 * 60 * 60 * 24) {
                cache.remove(cached.get());
            } else return cached;
        }
        try {
            HttpURLConnection conn = getConnection(String.format(SKIN_URL, ownerUUID.toString().replace("-", "")));
            if (conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                cache(new TexturesModel());
                return Optional.empty();
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                TexturesModel texturesModel = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create().fromJson(reader, TexturesModel.class);
                cache(texturesModel);
                return Optional.of(texturesModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public TexturesModel randomSkin(UUID exclude) {
        if (cache.isEmpty())
            return null;
        List<TexturesModel> skins = new ArrayList<>(cache.keySet());
        skins.removeIf(texturesModel -> texturesModel.getId().toString().equals(exclude.toString()));
        Collections.shuffle(skins);
        return skins.get(0);
    }

    public void cache(TexturesModel texturesModel) {
        long time = System.currentTimeMillis();
        cache.put(texturesModel, time);
        File skinFile = new File(SimpleNick.skinFolder, texturesModel.getId().toString() + ".json");
        if (!skinFile.exists()) {
            try {
                skinFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> json = new HashMap<>();
        json.put("timestamp", String.valueOf(time));
        json.put("texturesModel", new Gson().toJson(texturesModel));
        String jsonStr = new Gson().toJson(json);
        try {
            Files.writeString(skinFile.toPath(), jsonStr, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();

        httpConnection.setConnectTimeout(3000);
        httpConnection.setReadTimeout(6000);

        httpConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json");
        httpConnection.setRequestProperty(HttpHeaders.USER_AGENT, "SimpleNick-Plugin");
        return httpConnection;
    }
}
