package de.lcpcraft.lucas.simplenick.commands;

import de.lcpcraft.lucas.simplenick.SimpleNick;
import de.lcpcraft.lucas.simplenick.reflection.ProfileChanger;
import de.lcpcraft.lucas.simplenick.utils.Message;
import de.lcpcraft.lucas.simplenick.utils.NameCreator;
import de.lcpcraft.lucas.simplenick.utils.TexturesModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SimpleNickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("random") && p.hasPermission("simplenick.nick")) {
                    String fakeName = NameCreator.randomName();
                    SimpleNick.nicknameManager.nickPlayer(p.getUniqueId(), fakeName);
                    p.sendMessage(Message.prefix + Message.newNickname.replace("%nickname%", fakeName));
                } else if (strings[0].equalsIgnoreCase("skin") && p.hasPermission("simplenick.nick")) {
                    TexturesModel random = SimpleNick.skinManager.randomSkin(p.getUniqueId());
                    if (random != null) {
                        if (ProfileChanger.changeSkin(p, random)) {
                            p.sendMessage(Message.prefix + Message.skinChanged.replace("%skin%", random.getName()));
                            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + SimpleNick.nicknameManager.getRealName(p)
                                    + " §ais now using the skin of §7" + random.getName());
                        } else p.sendMessage(Message.prefix + Message.skinChangeFailed);
                    } else
                        p.sendMessage(Message.prefix + Message.noRandomSkin);
                } else if (strings[0].equalsIgnoreCase("reset") && p.hasPermission("simplenick.nick")) {
                    SimpleNick.nicknameManager.unnickPlayer(p.getUniqueId());
                    p.sendMessage(Message.prefix + Message.nicknameReset);
                } else if (strings[0].equalsIgnoreCase("resetall") && p.hasPermission("simplenick.nick.other")) {
                    SimpleNick.nicknameManager.unnickAllPlayers();
                    p.sendMessage(Message.prefix + Message.allNicknamesReset);
                } else if (strings[0].equalsIgnoreCase("list") && p.hasPermission("simplenick.list")) {
                    sendNickedPlayers(commandSender);
                } else if (strings[0].equalsIgnoreCase("reload") && p.hasPermission("simplenick.reload")) {
                    SimpleNick.reload();
                    p.sendMessage(Message.prefix + Message.configReloaded);
                } else sendHelp(commandSender);
            } else if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("random") && p.hasPermission("simplenick.nick.other")) {
                    nickPlayer(commandSender, strings[1]);
                } else if (strings[0].equalsIgnoreCase("skin") && p.hasPermission("simplenick.nick")) {
                    if (strings[1].equalsIgnoreCase("random")) {
                        TexturesModel random = SimpleNick.skinManager.randomSkin(p.getUniqueId());
                        if (random != null) {
                            if (ProfileChanger.changeSkin(p, random)) {
                                p.sendMessage(Message.prefix + Message.skinChanged.replace("%skin%", random.getName()));
                                Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + SimpleNick.nicknameManager.getRealName(p)
                                        + " §ais now using the skin of §7" + random.getName());
                            } else p.sendMessage(Message.prefix + Message.skinChangeFailed);
                        } else
                            p.sendMessage(Message.prefix + Message.noRandomSkin);
                    } else if (strings[1].length() == 36) {
                        UUID uuid = UUID.fromString(strings[1]);
                        Optional<TexturesModel> skin = SimpleNick.skinManager.getSkin(uuid);
                        if (skin.isPresent()) {
                            if (ProfileChanger.changeSkin(p, skin.get())) {
                                p.sendMessage(Message.prefix + Message.skinChanged.replace("%skin%", skin.get().getName()));
                                Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + SimpleNick.nicknameManager.getRealName(p)
                                        + " §ais now using the skin of §7" + skin.get().getName());
                            } else p.sendMessage(Message.prefix + Message.skinChangeFailed);
                        } else
                            p.sendMessage(Message.prefix + Message.skinNotFoundUUID.replace("%uuid%", strings[1]));
                    } else if (strings[1].length() <= 16) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                        if (offlinePlayer.hasPlayedBefore()) {
                            Optional<TexturesModel> skin = SimpleNick.skinManager.getSkin(offlinePlayer.getUniqueId());
                            if (skin.isPresent()) {
                                if (ProfileChanger.changeSkin(p, skin.get())) {
                                    p.sendMessage(Message.prefix + Message.skinChanged.replace("%skin%", skin.get().getName()));
                                    Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + SimpleNick.nicknameManager.getRealName(p)
                                            + " §ais now using the skin of §7" + skin.get().getName());
                                } else
                                    p.sendMessage(Message.prefix + Message.skinChangeFailed);
                            } else
                                p.sendMessage(Message.prefix + Message.skinNotFoundName.replace("%player%", strings[1]));
                        } else
                            p.sendMessage(Message.prefix + Message.skinNotFoundUseUUID.replace("%player%", strings[1]));
                    } else sendHelp(commandSender);
                } else if (strings[0].equalsIgnoreCase("reset") && p.hasPermission("simplenick.nick.other")) {
                    unnickPlayer(commandSender, strings[1]);
                } else sendHelp(commandSender);
            } else if (strings.length == 3) {
                changePlayersSkin(commandSender, strings);
            } else sendHelp(commandSender);
        } else if (commandSender instanceof ConsoleCommandSender) {
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("reload")) {
                    SimpleNick.reload();
                    commandSender.sendMessage(Message.prefix + Message.configReloaded);
                } else if (strings[0].equalsIgnoreCase("list")) {
                    sendNickedPlayers(commandSender);
                } else if (strings[0].equalsIgnoreCase("resetall")) {
                    SimpleNick.nicknameManager.unnickAllPlayers();
                    commandSender.sendMessage(Message.prefix + Message.allNicknamesReset);
                } else sendHelp(commandSender);
            } else if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("reset")) {
                    unnickPlayer(commandSender, strings[1]);
                } else if (strings[0].equalsIgnoreCase("random")) {
                    nickPlayer(commandSender, strings[1]);
                } else sendHelp(commandSender);
            } else changePlayersSkin(commandSender, strings);
        }
        return false;
    }

    private void changePlayersSkin(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        if (strings[0].equalsIgnoreCase("skin")) {
            Player target = Bukkit.getPlayer(strings[2]);
            if (target != null) {
                if (strings[1].equalsIgnoreCase("random")) {
                    TexturesModel random = SimpleNick.skinManager.randomSkin(target.getUniqueId());
                    if (random != null) {
                        if (ProfileChanger.changeSkin(target, random)) {
                            commandSender.sendMessage(Message.prefix + Message.skinOfPlayerChanged
                                    .replace("%player%", SimpleNick.nicknameManager.getRealName(target)).replace("%skin%", random.getName()));
                            target.sendMessage(Message.prefix + Message.skinChanged.replace("%skin%", random.getName()));
                            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + SimpleNick.nicknameManager.getRealName(target)
                                    + " §ais now using the skin of §7" + random.getName());
                        } else commandSender.sendMessage(Message.prefix + Message.skinOfPlayerChangedFailed
                                .replace("%player%", SimpleNick.nicknameManager.getRealName(target)));
                    } else
                        commandSender.sendMessage(Message.prefix + Message.noRandomSkin);
                } else if (strings[1].length() == 36) {
                    UUID uuid = UUID.fromString(strings[1]);
                    Optional<TexturesModel> skin = SimpleNick.skinManager.getSkin(uuid);
                    if (skin.isPresent()) {
                        if (ProfileChanger.changeSkin(target, skin.get())) {
                            commandSender.sendMessage(Message.prefix + Message.skinOfPlayerChanged
                                    .replace("%player%", SimpleNick.nicknameManager.getRealName(target)).replace("%skin%", skin.get().getName()));
                            target.sendMessage(Message.prefix + Message.skinChanged.replace("%skin%", skin.get().getName()));
                            Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + SimpleNick.nicknameManager.getRealName(target)
                                    + " §ais now using the skin of §7" + skin.get().getName());
                        } else commandSender.sendMessage(Message.prefix + Message.skinOfPlayerChangedFailed
                                .replace("%player%", SimpleNick.nicknameManager.getRealName(target)));
                    } else
                        commandSender.sendMessage(Message.prefix + Message.skinNotFoundUUID.replace("%uuid%", strings[1]));
                } else if (strings[1].length() <= 16) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                    if (offlinePlayer.hasPlayedBefore()) {
                        Optional<TexturesModel> skin = SimpleNick.skinManager.getSkin(offlinePlayer.getUniqueId());
                        if (skin.isPresent()) {
                            if (ProfileChanger.changeSkin(target, skin.get())) {
                                commandSender.sendMessage(Message.prefix + Message.skinOfPlayerChanged
                                        .replace("%player%", SimpleNick.nicknameManager.getRealName(target)).replace("%skin%", skin.get().getName()));
                                target.sendMessage(Message.prefix + Message.skinChanged.replace("%skin%", skin.get().getName()));
                                Bukkit.getConsoleSender().sendMessage(Message.prefix + "§7" + SimpleNick.nicknameManager.getRealName(target)
                                        + " §ais now using the skin of §7" + skin.get().getName());
                            } else
                                commandSender.sendMessage(Message.prefix + Message.skinOfPlayerChangedFailed
                                        .replace("%player%", SimpleNick.nicknameManager.getRealName(target)));
                        } else
                            commandSender.sendMessage(Message.prefix + Message.skinNotFoundName.replace("%player%", strings[1]));
                    } else
                        commandSender.sendMessage(Message.prefix + Message.skinNotFoundUseUUID.replace("%player%", strings[1]));
                } else sendHelp(commandSender);
            } else
                commandSender.sendMessage(Message.prefix + Message.playerOffline.replace("%player%", strings[2]));
        } else sendHelp(commandSender);
    }

    private void unnickPlayer(@NotNull CommandSender commandSender, @NotNull String name) {
        Player target = Bukkit.getPlayer(name);
        if (target != null) {
            SimpleNick.nicknameManager.unnickPlayer(target.getUniqueId());
            commandSender.sendMessage(Message.prefix + Message.nicknameOfPlayerReset.replace("%player%", SimpleNick.nicknameManager.getRealName(target)));
            target.sendMessage(Message.prefix + Message.nicknameReset);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            if (offlinePlayer.hasPlayedBefore()) {
                SimpleNick.nicknameManager.unnickPlayer(offlinePlayer.getUniqueId());
                commandSender.sendMessage(Message.prefix + Message.nicknameOfPlayerReset.replace("%player%", name));
            } else commandSender.sendMessage(Message.prefix + Message.playerNeverJoined.replace("%player%", name));
        }
    }

    private void nickPlayer(@NotNull CommandSender commandSender, @NotNull String name) {
        Player target = Bukkit.getPlayer(name);
        if (target != null) {
            String fakeName = NameCreator.randomName();
            SimpleNick.nicknameManager.nickPlayer(target.getUniqueId(), fakeName);
            commandSender.sendMessage(Message.prefix + Message.newNicknameOfPlayer
                    .replace("%player%", SimpleNick.nicknameManager.getRealName(target)).replace("%nickname%", fakeName));
            target.sendMessage(Message.prefix + Message.newNickname.replace("%nickname%", fakeName));
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            if (offlinePlayer.hasPlayedBefore()) {
                String fakeName = NameCreator.randomName();
                SimpleNick.nicknameManager.nickPlayer(offlinePlayer.getUniqueId(), fakeName);
                commandSender.sendMessage(Message.prefix + Message.newNicknameOfPlayer.replace("%player%", name).replace("%nickname%", fakeName));
            } else commandSender.sendMessage(Message.prefix + Message.playerNeverJoined.replace("%player%", name));
        }
    }

    private void sendNickedPlayers(CommandSender commandSender) {
        List<String> nickedPlayers = SimpleNick.nicknameManager.getColoredNickedPlayersList();
        if (nickedPlayers.isEmpty())
            commandSender.sendMessage(Message.prefix + Message.noNickedPlayers);
        else
            commandSender.sendMessage(Message.prefix + Message.nickedPlayers.replace("%players%", "§a" + String.join("§7, §a", nickedPlayers)));
    }

    private void sendHelp(CommandSender sender) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player && sender.hasPermission("simplenick.nick")) {
            commands.add(Message.explainHelp.replace("%command%", "/simplenick"));
            if (sender.hasPermission("simplenick.nick.other")) {
                commands.add(Message.explainOtherRandom.replace("%command%", "/simplenick random [Spieler]"));
                commands.add(Message.explainOtherSkin.replace("%command%", "/simplenick skin [random|UUID|Name] [Spieler]"));
                commands.add(Message.explainOtherReset.replace("%command%", "/simplenick reset [Spieler]"));
                commands.add(Message.explainResetAll.replace("%command%", "/simplenick resetall"));
            } else {
                commands.add(Message.explainRandom.replace("%command%", "/simplenick random"));
                commands.add(Message.explainSkin.replace("%command%", "/simplenick skin [random|UUID|Name]"));
                commands.add(Message.explainReset.replace("%command%", "/simplenick reset"));
            }
        } else if (sender instanceof ConsoleCommandSender) {
            commands.add(Message.explainOtherRandom.replace("%command%", "/simplenick random <Spieler>"));
            commands.add(Message.explainOtherSkin.replace("%command%", "/simplenick skin <random|UUID|Name> <Spieler>"));
            commands.add(Message.explainOtherReset.replace("%command%", "/simplenick reset <Spieler>"));
            commands.add(Message.explainResetAll.replace("%command%", "/simplenick resetall"));
        }
        if ((sender instanceof Player && sender.hasPermission("simplenick.list")) || sender instanceof ConsoleCommandSender)
            commands.add(Message.explainList.replace("%command%", "/simplenick list"));
        if ((sender instanceof Player && sender.hasPermission("simplenick.reload")) || sender instanceof ConsoleCommandSender)
            commands.add(Message.explainReload.replace("%command%", "/simplenick reload"));
        if (commands.isEmpty()) sender.sendMessage(Message.prefix + Message.noPermission);
        else sender.sendMessage(Message.prefix + Message.availableCommands + "\n" + String.join("\n", commands));
    }
}
