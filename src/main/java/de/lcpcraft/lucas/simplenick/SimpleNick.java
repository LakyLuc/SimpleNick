package de.lcpcraft.lucas.simplenick;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.lcpcraft.lucas.simplenick.commands.SimpleNickCommand;
import de.lcpcraft.lucas.simplenick.listeners.ChatListener;
import de.lcpcraft.lucas.simplenick.listeners.JoinListener;
import de.lcpcraft.lucas.simplenick.listeners.TabCompleteListener;
import de.lcpcraft.lucas.simplenick.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class SimpleNick extends JavaPlugin {

    public static SimpleNick plugin;
    public static File configFile;
    public static File nicknameFile;
    public static File skinFolder;
    public static YamlConfiguration config;
    public static YamlConfiguration nicknameConfig;
    public static NicknameManager nicknameManager;
    public static SkinManager skinManager;

    @Override
    public void onEnable() {
        plugin = this;
        new Metrics(this, 18396);

        File pluginFolder = new File(plugin.getDataFolder().getParentFile(), "SimpleNick");
        if (!pluginFolder.exists())
            pluginFolder.mkdir();
        skinFolder = new File(pluginFolder, "skins");
        if (!skinFolder.exists())
            skinFolder.mkdir();
        configFile = new File(pluginFolder, "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("use_luckperms")) {
            config.addDefault("use_luckperms", true);
            config.addDefault("lp_group_for_nicked_players", "default");
            config.addDefault("tablist_header", new ArrayList<>());
            config.addDefault("tablist_footer", new ArrayList<>());
            config.addDefault("custom_chat_format", true);
            config.options().header("Configuration file of SimpleNick by LakyLuc").copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException ignored) {
            }
        }
        Message.load();

        Map<String, String> nicknames = new HashMap<>();
        Map<String, String> realNames = new HashMap<>();
        nicknameFile = new File(pluginFolder, "nicknames.yml");
        nicknameConfig = YamlConfiguration.loadConfiguration(nicknameFile);
        nicknameConfig.getValues(false).forEach((key, value) -> {
            if (value instanceof String)
                nicknames.put(key, (String) value);
        });
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            realNames.put(onlinePlayer.getUniqueId().toString(), onlinePlayer.getName());
        nicknameManager = new NicknameManager(nicknames, realNames);
        Map<TexturesModel, Long> skins = new HashMap<>();
        for (File skinFile : Objects.requireNonNull(skinFolder.listFiles())) {
            try {
                String jsonStr = Files.readString(skinFile.toPath());
                Map<String, Object> json = new Gson().fromJson(jsonStr, new TypeToken<HashMap<String, Object>>() {
                }.getType());
                skins.put(new Gson().fromJson((String) json.get("texturesModel"), TexturesModel.class), Long.parseLong((String) json.get("timestamp")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        skinManager = new SkinManager(skins);
        nicknameManager.updateAllPlayers();

        Objects.requireNonNull(getCommand("simplenick")).setExecutor(new SimpleNickCommand());
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new TabCompleteListener(), this);
    }

    @Override
    public void onDisable() {
        nicknameManager.onPluginDisable();
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
        Message.load();
        HashMap<String, String> nicknames = new HashMap<>();
        nicknameConfig = YamlConfiguration.loadConfiguration(nicknameFile);
        nicknameConfig.getValues(false).forEach((key, value) -> {
            if (value instanceof String)
                nicknames.put(key, (String) value);
        });
        HashMap<String, String> realNames = nicknameManager.getRealNames();
        nicknameManager = new NicknameManager(nicknames, realNames);
        Map<TexturesModel, Long> skins = new HashMap<>();
        for (File skinFile : Objects.requireNonNull(skinFolder.listFiles())) {
            try {
                String jsonStr = Files.readString(skinFile.toPath());
                Map<String, Object> json = new Gson().fromJson(jsonStr, new TypeToken<HashMap<String, Object>>() {
                }.getType());
                skins.put(new Gson().fromJson((String) json.get("texturesModel"), TexturesModel.class), Long.parseLong((String) json.get("timestamp")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        skinManager = new SkinManager(skins);
        nicknameManager.updateAllPlayers();
    }

    public static void saveNickname(String uuid, String nickname) {
        nicknameConfig.set(uuid, nickname);
        try {
            nicknameConfig.save(nicknameFile);
        } catch (IOException ignored) {
        }
    }

    public static void removeNickname(String uuid) {
        nicknameConfig.set(uuid, null);
        try {
            nicknameConfig.save(nicknameFile);
        } catch (IOException ignored) {
        }
    }

    public static boolean useLuckPerms() {
        return config.getBoolean("use_luckperms", true) && Bukkit.getPluginManager().isPluginEnabled("LuckPerms");
    }

    public static boolean customChatFormat() {
        return config.getBoolean("custom_chat_format", true);
    }
}
