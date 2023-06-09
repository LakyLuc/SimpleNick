package de.lcpcraft.lucas.simplenick.listeners;

import de.lcpcraft.lucas.simplenick.SimpleNick;
import de.lcpcraft.lucas.simplenick.utils.Updater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        SimpleNick.nicknameManager.onPlayerLogin(e.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("simplenick.update"))
            Updater.sendUpdateMessage(e.getPlayer());
        SimpleNick.nicknameManager.onPlayerJoin(e.getPlayer());
    }
}
