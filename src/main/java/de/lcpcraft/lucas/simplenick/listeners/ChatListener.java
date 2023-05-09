package de.lcpcraft.lucas.simplenick.listeners;

import de.lcpcraft.lucas.simplenick.SimpleNick;
import de.lcpcraft.lucas.simplenick.utils.Message;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent e) {
        if (!SimpleNick.customChatFormat()) return;
        e.setCancelled(true);
        String cleanName = SimpleNick.nicknameManager.getNickname(e.getPlayer());
        String rankedName = SimpleNick.nicknameManager.getRankedName(e.getPlayer(), cleanName);
        String[] message = Message.chatFormat.replace("%player%", rankedName).split("%message%");
        e.getPlayer().getServer().sendMessage(Component.text(message[0]).append(e.message())
                .append((message.length > 1 ? Component.text(message[1]) : Component.empty())));
    }
}
