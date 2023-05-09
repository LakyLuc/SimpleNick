package de.lcpcraft.lucas.simplenick.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class ReflectionUtil {
    public static String getServerVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getServerVersion() + "." + name);
    }

    public static Class<?> getPacketClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.network.protocol." + name);
    }

    public static Object getHandle(Player player) throws Exception {
        Object craftPlayer = Class.forName("org.bukkit.craftbukkit." + ReflectionUtil.getServerVersion() + ".entity.CraftPlayer").cast(player);
        Method getHandleMethod = craftPlayer.getClass().getMethod("getHandle");
        return getHandleMethod.invoke(craftPlayer);
    }
}
