package de.lcpcraft.lucas.simplenick.utils;

import de.lcpcraft.lucas.simplenick.SimpleNick;
import de.lcpcraft.lucas.simplenick.reflection.ProfileChanger;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class NicknameManager {
    private final Map<String, String> nicknames;
    private final Map<String, String> realNames;

    public NicknameManager(Map<String, String> nicknames, Map<String, String> realNames) {
        this.nicknames = nicknames;
        this.realNames = realNames;
    }

    public void updateAllPlayers() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendPlayerListHeaderAndFooter(Component.text(String.join("\n", SimpleNick.config.getStringList("tablist_header"))),
                    Component.text(String.join("\n", SimpleNick.config.getStringList("tablist_footer"))));
            String cleanName = getNickname(onlinePlayer);
            Component rankedName = Component.text(getRankedName(onlinePlayer, cleanName));
            onlinePlayer.playerListName(rankedName);
            ProfileChanger.changeName(onlinePlayer, cleanName);
            setPlayerTeam(onlinePlayer);
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aNickname of " + getRealName(onlinePlayer) + " is " + cleanName);
            TexturesModel random = SimpleNick.skinManager.randomSkin(onlinePlayer.getUniqueId());
            if (random != null && ProfileChanger.changeSkin(onlinePlayer, random))
                Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aChanged skin of " + getRealName(onlinePlayer) + " to " + random.getName());
        }
        if (nicknames.isEmpty())
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aAll players have been updated. No nicked players.");
        else
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aAll players have been updated. Nicked players: "
                    + String.join("§7, ", getColoredNickedPlayersList()));
    }

    public void onPluginDisable() {
        if (nicknames.keySet().stream().anyMatch(s -> {
            Player player = Bukkit.getPlayer(UUID.fromString(s));
            return player != null && player.isOnline();
        }))
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§6§lWARN: §cThere are still nicked players online. Please remove them first if you disable the plugin manually. Some players have an incorrect name tag until they rejoin or the plugin is reactivated.");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String realName = getRealName(onlinePlayer);
            onlinePlayer.playerListName(Component.text(realName));
            ProfileChanger.changeName(onlinePlayer, realName, true);
            setPlayerTeam(onlinePlayer);
        }
        Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aAll players have been reset.");
    }

    public void onPlayerLogin(Player player) {
        realNames.put(player.getUniqueId().toString(), player.getName());
        String cleanName = getNickname(player);
        if (!player.getName().equals(cleanName))
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + player.getName() + " §ais nicked as §7" + cleanName);
        Component rankedName = Component.text(getRankedName(player, cleanName));
        player.playerListName(rankedName);
        ProfileChanger.cacheSkin(player);
        ProfileChanger.changeName(player, cleanName);
        setPlayerTeam(player);
    }

    public void onPlayerJoin(Player player) {
        player.sendPlayerListHeaderAndFooter(Component.text(String.join("\n", SimpleNick.config.getStringList("tablist_header"))),
                Component.text(String.join("\n", SimpleNick.config.getStringList("tablist_footer"))));
        String cleanName = nicknames.getOrDefault(player.getUniqueId().toString(), null);
        if (cleanName != null) {
            player.sendMessage(Message.prefix
                    + Message.nickInfoMessage.replace("%nickname%", cleanName));
            TexturesModel random = SimpleNick.skinManager.randomSkin(player.getUniqueId());
            if (random != null && ProfileChanger.changeSkin(player, random))
                Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + player.getName() + " §ais now using the skin of §7" + random.getName());
        }
    }

    public void nickPlayer(UUID uuid, String nickname) {
        nicknames.put(uuid.toString(), nickname);
        SimpleNick.saveNickname(uuid.toString(), nickname);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            String cleanName = nicknames.getOrDefault(player.getUniqueId().toString(), getRealName(player));
            Component rankedName = Component.text(getRankedName(player, cleanName));
            player.playerListName(rankedName);
            ProfileChanger.changeName(player, cleanName);
            setPlayerTeam(player);
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + getRealName(player) + " §ais now nicked as §7" + cleanName);
            TexturesModel random = SimpleNick.skinManager.randomSkin(player.getUniqueId());
            if (random != null && ProfileChanger.changeSkin(player, random))
                Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + getRealName(player) + " §ais now using the skin of §7" + random.getName());
        }
    }

    public void unnickPlayer(UUID uuid) {
        nicknames.remove(uuid.toString());
        SimpleNick.removeNickname(uuid.toString());
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            Component rankedName = Component.text(getRankedName(player, getRealName(player)));
            player.playerListName(rankedName);
            ProfileChanger.changeName(player, getRealName(player));
            setPlayerTeam(player);
            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + player.getName() + " §ais no longer nicked.");
            Optional<TexturesModel> playerSkin = SimpleNick.skinManager.getSkin(player.getUniqueId());
            playerSkin.ifPresent(texturesModel -> ProfileChanger.changeSkin(player, texturesModel));
        }
    }

    public void unnickAllPlayers() {
        for (String uuid : nicknames.keySet()) {
            SimpleNick.removeNickname(uuid);
            Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if (player != null) {
                Component rankedName = Component.text(getRankedName(player, getRealName(player)));
                player.playerListName(rankedName);
                ProfileChanger.changeName(player, getRealName(player));
                setPlayerTeam(player);
                Optional<TexturesModel> playerSkin = SimpleNick.skinManager.getSkin(player.getUniqueId());
                playerSkin.ifPresent(texturesModel -> ProfileChanger.changeSkin(player, texturesModel));
            }
        }
        nicknames.clear();
        Bukkit.getConsoleSender().sendMessage(Message.prefix + "§aAll players have been unnicked.");
    }

    public String getNickname(Player player) {
        return nicknames.getOrDefault(player.getUniqueId().toString(), getRealName(player));
    }

    public String getRealName(Player player) {
        return realNames.getOrDefault(player.getUniqueId().toString(), player.getName());
    }

    public String getRankedName(Player player, String cleanName) {
        if (SimpleNick.useLuckPerms()) {
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getUserManager().getUser(player.getUniqueId());
            if (user == null) return cleanName;
            Group group = api.getGroupManager().getGroup(user.getPrimaryGroup());
            String prefix = user.getCachedData().getMetaData().getPrefix();
            String suffix = user.getCachedData().getMetaData().getSuffix();
            if (nicknames.containsKey(player.getUniqueId().toString())) {
                group = api.getGroupManager().getGroup(SimpleNick.config.getString("lp_group_for_nicked_players", "default"));
                prefix = null;
                suffix = null;
            }
            if (prefix == null && group != null)
                prefix = group.getCachedData().getMetaData().getPrefix();
            if (suffix == null && group != null)
                suffix = group.getCachedData().getMetaData().getSuffix();
            if (prefix != null || suffix != null)
                return (prefix != null ? prefix : "") + cleanName + (suffix != null ? suffix : "");
        }
        return cleanName;
    }

    public List<String> getColoredNickedPlayersList() {
        List<String> nickedPlayers = new ArrayList<>();
        nicknames.forEach((uuid, name) -> {
            UUID id = UUID.fromString(uuid);
            Player player = Bukkit.getPlayer(id);
            String realName;
            if (player != null && player.isOnline()) realName = getRealName(player);
            else {
                realName = Bukkit.getOfflinePlayer(id).getName();
                if (realName == null || realName.equals(name)) realName = uuid;
            }
            nickedPlayers.add("§6" + realName + " §8-> §e" + name);
        });
        return nickedPlayers;
    }

    public HashMap<String, String> getRealNames() {
        return new HashMap<>(realNames);
    }

    public void setPlayerTeam(Player player) {
        if (SimpleNick.useTeams()) {
            if (SimpleNick.useLuckPerms()) {
                LuckPerms api = LuckPermsProvider.get();
                User user = api.getUserManager().getUser(getRealName(player));
                if (user == null) return;
                Group group = api.getGroupManager().getGroup(user.getPrimaryGroup());
                String prefix = user.getCachedData().getMetaData().getPrefix();
                String suffix = user.getCachedData().getMetaData().getSuffix();
                if (nicknames.containsKey(player.getUniqueId().toString())) {
                    group = api.getGroupManager().getGroup(SimpleNick.config.getString("lp_group_for_nicked_players", "default"));
                    prefix = null;
                    suffix = null;
                }
                if (prefix == null && group != null)
                    prefix = group.getCachedData().getMetaData().getPrefix();
                if (suffix == null && group != null)
                    suffix = group.getCachedData().getMetaData().getSuffix();
                if (prefix != null || suffix != null) {
                    Team team = player.getScoreboard().getTeam(user.getPrimaryGroup());
                    if (team == null) team = player.getScoreboard().registerNewTeam(user.getPrimaryGroup());
                    team.addEntry(player.getName());
                    team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
                    team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                    team.prefix(prefix != null ? Component.text(prefix) : Component.empty());
                    team.suffix(suffix != null ? Component.text(suffix) : Component.empty());
                }
            }
        }
    }
}
