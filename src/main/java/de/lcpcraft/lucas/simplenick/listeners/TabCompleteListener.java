package de.lcpcraft.lucas.simplenick.listeners;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener implements Listener {
    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        String[] args = e.getBuffer().split(" ");
        List<String> offlinePlayers = new ArrayList<>();
        List<String> complete = new ArrayList<>();
        List<String> complete1 = new ArrayList<>();
        for (OfflinePlayer op : Bukkit.getOfflinePlayers())
            offlinePlayers.add(op.getName());
        if (e.getSender() instanceof Player p) {
            if (args[0].equalsIgnoreCase("/simplenick") || args[0].equalsIgnoreCase("/snick")) {
                boolean completed = false;
                if (args.length == 1) {
                    if (p.hasPermission("simplenick.nick")) {
                        complete1.add("random");
                        complete1.add("skin");
                        complete1.add("reset");
                    }
                    if (p.hasPermission("simplenick.nick.other"))
                        complete1.add("resetall");
                    if (p.hasPermission("simplenick.list"))
                        complete1.add("list");
                    if (p.hasPermission("simplenick.reload"))
                        complete1.add("reload");
                    if (!complete1.isEmpty()) {
                        e.setCompletions(complete1);
                        completed = true;
                    }
                } else if (args.length == 2 && !e.getBuffer().endsWith(" ")) {
                    if (p.hasPermission("simplenick.nick")) {
                        complete1.add("random");
                        complete1.add("skin");
                        complete1.add("reset");
                    }
                    if (p.hasPermission("simplenick.nick.other"))
                        complete1.add("resetall");
                    if (p.hasPermission("simplenick.list"))
                        complete1.add("list");
                    if (p.hasPermission("simplenick.reload"))
                        complete1.add("reload");
                    String pln = args[1];
                    for (String lt : complete1)
                        if (lt.toLowerCase().startsWith(pln.toLowerCase()))
                            complete.add(lt);
                    e.setCompletions(complete);
                    completed = true;
                } else if (args.length == 2 && (e.getBuffer().toLowerCase().startsWith("/simplenick random ") || e.getBuffer().toLowerCase().startsWith("/snick random "))) {
                    if (p.hasPermission("simple.nick.other")) {
                        e.setCompletions(offlinePlayers);
                        completed = true;
                    }
                } else if (args.length == 2 && (e.getBuffer().toLowerCase().startsWith("/simplenick skin ") || e.getBuffer().toLowerCase().startsWith("/snick skin "))) {
                    if (p.hasPermission("simplenick.nick")) {
                        complete1.add("random");
                        complete1.addAll(offlinePlayers);
                        e.setCompletions(complete1);
                        completed = true;
                    }
                } else if (args.length == 2 && (e.getBuffer().toLowerCase().startsWith("/simplenick reset ") || e.getBuffer().toLowerCase().startsWith("/snick reset "))) {
                    if (p.hasPermission("simple.nick.other")) {
                        e.setCompletions(offlinePlayers);
                        completed = true;
                    }
                } else if (args.length == 3 && (e.getBuffer().toLowerCase().startsWith("/simplenick random") || e.getBuffer().toLowerCase().startsWith("/snick random"))
                        && !e.getBuffer().endsWith(" ")) {
                    if (p.hasPermission("simple.nick.other")) {
                        String pln = args[2];
                        for (String lt : offlinePlayers)
                            if (lt.toLowerCase().startsWith(pln.toLowerCase()))
                                complete.add(lt);
                        e.setCompletions(complete);
                        completed = true;
                    }
                } else if (args.length == 3 && (e.getBuffer().toLowerCase().startsWith("/simplenick skin") || e.getBuffer().toLowerCase().startsWith("/snick skin"))
                        && !e.getBuffer().endsWith(" ")) {
                    if (p.hasPermission("simple.nick")) {
                        String pln = args[2];
                        if ("random".startsWith(pln.toLowerCase()))
                            complete.add("random");
                        for (String lt : offlinePlayers)
                            if (lt.toLowerCase().startsWith(pln.toLowerCase()))
                                complete.add(lt);
                        e.setCompletions(complete);
                        completed = true;
                    }
                } else if (args.length == 3 && (e.getBuffer().toLowerCase().startsWith("/simplenick reset") || e.getBuffer().toLowerCase().startsWith("/snick reset"))
                        && !e.getBuffer().endsWith(" ")) {
                    if (p.hasPermission("simple.nick.other")) {
                        String pln = args[2];
                        for (String lt : offlinePlayers)
                            if (lt.toLowerCase().startsWith(pln.toLowerCase()))
                                complete.add(lt);
                        e.setCompletions(complete);
                        completed = true;
                    }
                } else if (args.length == 3 && (e.getBuffer().toLowerCase().startsWith("/simplenick skin ") || e.getBuffer().toLowerCase().startsWith("/snick skin "))
                        && e.getBuffer().endsWith(" ")) {
                    if (p.hasPermission("simplenick.nick.other")) {
                        e.setCompletions(offlinePlayers);
                        completed = true;
                    }
                } else if (args.length == 4 && (e.getBuffer().toLowerCase().startsWith("/simplenick skin ") || e.getBuffer().toLowerCase().startsWith("/snick skin "))
                        && !e.getBuffer().endsWith(" ")) {
                    if (p.hasPermission("simplenick.nick.other")) {
                        String pln = args[3];
                        for (String lt : offlinePlayers)
                            if (lt.toLowerCase().startsWith(pln.toLowerCase()))
                                complete.add(lt);
                        e.setCompletions(complete);
                        completed = true;
                    }
                }
                if (!completed)
                    e.setCompletions(new ArrayList<>());
            }
        }
    }
}
