package de.lcpcraft.lucas.simplenick.utils;

import de.lcpcraft.lucas.simplenick.SimpleNick;

import java.io.IOException;

public class Message {

    public static String prefix;
    public static String noPermission;
    public static String nickInfoMessage;
    public static String chatFormat;
    public static String newNickname;
    public static String skinChanged;
    public static String skinChangeFailed;
    public static String noRandomSkin;
    public static String nicknameReset;
    public static String allNicknamesReset;
    public static String configReloaded;
    public static String skinNotFoundUUID;
    public static String skinNotFoundUseUUID;
    public static String skinNotFoundName;
    public static String skinOfPlayerChanged;
    public static String skinOfPlayerChangedFailed;
    public static String playerOffline;
    public static String nicknameOfPlayerReset;
    public static String playerNeverJoined;
    public static String newNicknameOfPlayer;
    public static String noNickedPlayers;
    public static String nickedPlayers;

    public static String explainHelp;
    public static String explainOtherRandom;
    public static String explainOtherSkin;
    public static String explainOtherReset;
    public static String explainResetAll;
    public static String explainRandom;
    public static String explainSkin;
    public static String explainReset;
    public static String explainList;
    public static String explainReload;
    public static String availableCommands;


    public static void load() {
        prefix = SimpleNick.config.getString("prefix", "§1[§9SimpleNick§1] §r");
        noPermission = SimpleNick.config.getString("no_permission", "§cDazu hast du keine Rechte!");
        nickInfoMessage = SimpleNick.config.getString("nick_info_message", "§7Du bist aktuell als §r%nickname% §7genickt");
        chatFormat = SimpleNick.config.getString("chat_format", "%player% §8> §r%message%");
        newNickname = SimpleNick.config.getString("new_nickname", "§7Du bist nun als §r%nickname% §7genickt");
        skinChanged = SimpleNick.config.getString("skin_changed", "§7Dein Skin wurde zu §r%skin% §7geändert");
        skinChangeFailed = SimpleNick.config.getString("skin_change_failed", "§cDein Skin konnte nicht geändert werden!");
        noRandomSkin = SimpleNick.config.getString("no_random_skin", "§cEs wurde kein zufälliger Skin gefunden!");
        nicknameReset = SimpleNick.config.getString("nickname_reset", "§7Dein Nickname/Skin wurde zurückgesetzt");
        allNicknamesReset = SimpleNick.config.getString("nickname_reset_all", "§7Alle Nicknames/Skins wurden zurückgesetzt");
        configReloaded = SimpleNick.config.getString("config_reloaded", "§7Die Konfiguration wurde neu geladen");
        skinNotFoundUUID = SimpleNick.config.getString("skin_not_found_uuid", "§cDer Skin des Spielers mit der UUID §r%uuid% §cwurde nicht gefunden!");
        skinNotFoundUseUUID = SimpleNick.config.getString("skin_not_found_use_uuid", "§cDer Spieler §r%player% §cwar noch nie auf dem Server, bitte nutze die UUID!");
        skinNotFoundName = SimpleNick.config.getString("skin_not_found_name", "§cDer Skin des Spielers §r%player% §cwurde nicht gefunden!");
        skinOfPlayerChanged = SimpleNick.config.getString("skin_of_player_changed", "§7Der Skin von §r%player% §7wurde zu §r%skin% §7geändert");
        skinOfPlayerChangedFailed = SimpleNick.config.getString("skin_of_player_changed_failed", "§cDer Skin von §r%player% §ckonnte nicht geändert werden!");
        playerOffline = SimpleNick.config.getString("player_offline", "§cDer Spieler §r%player% §cist nicht online!");
        nicknameOfPlayerReset = SimpleNick.config.getString("nickname_of_player_reset", "§7Der Nickname/Skin von §r%player% §7wurde zurückgesetzt");
        playerNeverJoined = SimpleNick.config.getString("player_never_joined", "§cDer Spieler §r%player% §cwar noch nie auf dem Server!");
        newNicknameOfPlayer = SimpleNick.config.getString("new_nickname_of_player", "§7Der Spieler §r%player% §7ist nun als §r%nickname% §7genickt");
        noNickedPlayers = SimpleNick.config.getString("no_nicked_players", "§7Es sind aktuell keine Spieler genickt");
        nickedPlayers = SimpleNick.config.getString("nicked_players", "§7Folgende Spieler sind aktuell genickt: §r%players%");

        explainHelp = SimpleNick.config.getString("explain_help", "§e%command% §7- §aZeigt diese Hilfe an");
        explainOtherRandom = SimpleNick.config.getString("explain_other_random", "§e%command% §7- §aNickt einen Spieler mit einem zufälligen Namen");
        explainOtherSkin = SimpleNick.config.getString("explain_other_skin", "§e%command% §7- §aÄndert den Skin eines Spielers");
        explainOtherReset = SimpleNick.config.getString("explain_other_reset", "§e%command% §7- §aSetzt den Nickname/Skin eines Spielers zurück");
        explainResetAll = SimpleNick.config.getString("explain_reset_all", "§e%command% §7- §aSetzt alle Nicknames/Skins zurück");
        explainRandom = SimpleNick.config.getString("explain_random", "§e%command% §7- §aNickt dich mit einem zufälligen Namen");
        explainSkin = SimpleNick.config.getString("explain_skin", "§e%command% §7- §aÄndert deinen Skin");
        explainReset = SimpleNick.config.getString("explain_reset", "§e%command% §7- §aSetzt deinen Nickname/Skin zurück");
        explainList = SimpleNick.config.getString("explain_list", "§e%command% §7- §aListet alle genickten Spieler auf");
        explainReload = SimpleNick.config.getString("explain_reload", "§e%command% §7- §aLädt die Konfiguration neu");
        availableCommands = SimpleNick.config.getString("available_commands", "§aVerfügbare Befehle:");

        if (!SimpleNick.config.isSet("prefix"))
            SimpleNick.config.set("prefix", prefix);
        if (!SimpleNick.config.isSet("no_permission"))
            SimpleNick.config.set("no_permission", noPermission);
        if (!SimpleNick.config.isSet("nick_info_message"))
            SimpleNick.config.set("nick_info_message", nickInfoMessage);
        if (!SimpleNick.config.isSet("chat_format"))
            SimpleNick.config.set("chat_format", chatFormat);
        if (!SimpleNick.config.isSet("new_nickname"))
            SimpleNick.config.set("new_nickname", newNickname);
        if (!SimpleNick.config.isSet("skin_changed"))
            SimpleNick.config.set("skin_changed", skinChanged);
        if (!SimpleNick.config.isSet("skin_change_failed"))
            SimpleNick.config.set("skin_change_failed", skinChangeFailed);
        if (!SimpleNick.config.isSet("no_random_skin"))
            SimpleNick.config.set("no_random_skin", noRandomSkin);
        if (!SimpleNick.config.isSet("nickname_reset"))
            SimpleNick.config.set("nickname_reset", nicknameReset);
        if (!SimpleNick.config.isSet("nickname_reset_all"))
            SimpleNick.config.set("nickname_reset_all", allNicknamesReset);
        if (!SimpleNick.config.isSet("config_reloaded"))
            SimpleNick.config.set("config_reloaded", configReloaded);
        if (!SimpleNick.config.isSet("skin_not_found_uuid"))
            SimpleNick.config.set("skin_not_found_uuid", skinNotFoundUUID);
        if (!SimpleNick.config.isSet("skin_not_found_use_uuid"))
            SimpleNick.config.set("skin_not_found_use_uuid", skinNotFoundUseUUID);
        if (!SimpleNick.config.isSet("skin_not_found_name"))
            SimpleNick.config.set("skin_not_found_name", skinNotFoundName);
        if (!SimpleNick.config.isSet("skin_of_player_changed"))
            SimpleNick.config.set("skin_of_player_changed", skinOfPlayerChanged);
        if (!SimpleNick.config.isSet("skin_of_player_changed_failed"))
            SimpleNick.config.set("skin_of_player_changed_failed", skinOfPlayerChangedFailed);
        if (!SimpleNick.config.isSet("player_offline"))
            SimpleNick.config.set("player_offline", playerOffline);
        if (!SimpleNick.config.isSet("nickname_of_player_reset"))
            SimpleNick.config.set("nickname_of_player_reset", nicknameOfPlayerReset);
        if (!SimpleNick.config.isSet("player_never_joined"))
            SimpleNick.config.set("player_never_joined", playerNeverJoined);
        if (!SimpleNick.config.isSet("new_nickname_of_player"))
            SimpleNick.config.set("new_nickname_of_player", newNicknameOfPlayer);
        if (!SimpleNick.config.isSet("no_nicked_players"))
            SimpleNick.config.set("no_nicked_players", noNickedPlayers);
        if (!SimpleNick.config.isSet("nicked_players"))
            SimpleNick.config.set("nicked_players", nickedPlayers);
        if (!SimpleNick.config.isSet("explain_help"))
            SimpleNick.config.set("explain_help", explainHelp);
        if (!SimpleNick.config.isSet("explain_other_random"))
            SimpleNick.config.set("explain_other_random", explainOtherRandom);
        if (!SimpleNick.config.isSet("explain_other_skin"))
            SimpleNick.config.set("explain_other_skin", explainOtherSkin);
        if (!SimpleNick.config.isSet("explain_other_reset"))
            SimpleNick.config.set("explain_other_reset", explainOtherReset);
        if (!SimpleNick.config.isSet("explain_reset_all"))
            SimpleNick.config.set("explain_reset_all", explainOtherReset);
        if (!SimpleNick.config.isSet("explain_random"))
            SimpleNick.config.set("explain_random", explainRandom);
        if (!SimpleNick.config.isSet("explain_skin"))
            SimpleNick.config.set("explain_skin", explainSkin);
        if (!SimpleNick.config.isSet("explain_reset"))
            SimpleNick.config.set("explain_reset", explainReset);
        if (!SimpleNick.config.isSet("explain_list"))
            SimpleNick.config.set("explain_list", explainList);
        if (!SimpleNick.config.isSet("explain_reload"))
            SimpleNick.config.set("explain_reload", explainReload);
        if (!SimpleNick.config.isSet("available_commands"))
            SimpleNick.config.set("available_commands", availableCommands);
        try {
            SimpleNick.config.save(SimpleNick.configFile);
        } catch (IOException ignored) {
        }
    }
}
