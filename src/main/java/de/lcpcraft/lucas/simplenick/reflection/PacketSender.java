package de.lcpcraft.lucas.simplenick.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PacketSender {

    public static void sendPacket(Player player, Object packet) throws Exception {
        Object playerConnection = getConnection(player);
        Method sendPacketMethod = playerConnection.getClass().getMethod("a", ReflectionUtil.getPacketClass("Packet"));
        sendPacketMethod.invoke(playerConnection, packet);
    }

    private static Object getConnection(Player player) throws Exception {
        Object entityPlayer = ReflectionUtil.getHandle(player);
        Field playerConnectionField = entityPlayer.getClass().getField("b");
        if (playerConnectionField.getType().getSimpleName().equals("PlayerConnection"))
            return playerConnectionField.get(entityPlayer);
        for (Field field : entityPlayer.getClass().getFields()) {
            if (field.getType().getSimpleName().equals("PlayerConnection")) {
                return field.get(entityPlayer);
            }
        }
        return null;
    }
}
