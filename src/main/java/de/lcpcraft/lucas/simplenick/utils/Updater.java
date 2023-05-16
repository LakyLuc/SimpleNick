package de.lcpcraft.lucas.simplenick.utils;

import de.lcpcraft.lucas.simplenick.SimpleNick;
import de.lcpcraft.lucas.simplenick.utils.modrinth.ProjectVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

public class Updater {
    private static final String URL = "https://api.modrinth.com/v2/";
    public static ProjectVersion latestVersion;

    public static void checkForUpdates() {
        String gameVersion = Bukkit.getMinecraftVersion();
        ProjectVersion latestVersion = getLatestVersion(gameVersion, SimpleNick.updateChannel());
        if (latestVersion == null) {
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§cFailed to check for updates!");
            return;
        }
        String pluginVersion = Bukkit.getPluginManager().getPlugin("SimpleNick").getPluginMeta().getVersion();
        DefaultArtifactVersion latest = new DefaultArtifactVersion(latestVersion.version_number);
        DefaultArtifactVersion current = new DefaultArtifactVersion(pluginVersion);
        if (latest.compareTo(current) > 0) {
            Updater.latestVersion = latestVersion;
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aA new version of SimpleNick is available: §e" + latestVersion.version_number);
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aDownload it at §e"
                    + SimpleNick.MODRINTH_LINK.replace("%version%", latestVersion.version_number));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("simplenick.update"))
                    sendUpdateMessage(onlinePlayer);
            }
        }
    }

    public static void sendUpdateMessage(Player player) {
        if (latestVersion != null) {
            Component link = Component.text("Modrinth").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL,
                            SimpleNick.MODRINTH_LINK.replace("%version%", latestVersion.version_number)))
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("§7Changelog:\n" + latestVersion.changelog)));
            Component message = Component.text(Message.prefix + "§aA new version of SimpleNick is available §7("
                    + latestVersion.version_number + ") §aDownload it at ").append(link);
            player.sendMessage(message);
        }
    }

    private static ProjectVersion getLatestVersion(String gameVersion, String versionType) {
        RequestBuilder request = new RequestBuilder(URL + "project/" + SimpleNick.MODRINTH_ID
                + "/version?loaders=[%22paper%22]&game_versions=[%22" + gameVersion + "%22]");
        HttpsURLConnection connection = request.execute();
        try {
            if (connection.getResponseCode() == 200) {
                ProjectVersion[] versions = request.projectVersions();
                if (versions != null) {
                    for (ProjectVersion version : versions) {
                        if (versionType.equals("alpha"))
                            return version;
                        else if (version.version_type.equals(versionType))
                            return version;
                        else if (versionType.equals("beta") && version.version_type.equals("release"))
                            return version;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
