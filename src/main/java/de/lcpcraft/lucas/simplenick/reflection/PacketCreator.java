package de.lcpcraft.lucas.simplenick.reflection;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class PacketCreator {
    public static Object createPlayerInfoRemovePacket(UUID id) throws Exception {
        Class<?> packetClass = ReflectionUtil.getPacketClass("game.ClientboundPlayerInfoRemovePacket");
        Constructor<?> packetConstructor = packetClass.getConstructor(List.class);
        return packetConstructor.newInstance(Collections.singletonList(id));
    }

    public static Object createPlayerInfoAddPacket(Player player) throws Exception {
        Class<?> packetClass = ReflectionUtil.getPacketClass("game.ClientboundPlayerInfoUpdatePacket");
        Method packetBuilder = packetClass.getMethod("a", Collection.class);
        return packetBuilder.invoke(null, Collections.singletonList(ReflectionUtil.getHandle(player)));
    }

    public static Object createRespawnPacket(Player player) throws Exception {
        Class<?> packetClass = ReflectionUtil.getPacketClass("game.PacketPlayOutRespawn");
        Object entityPlayer = ReflectionUtil.getHandle(player);
        Object craftWorld = Class.forName("org.bukkit.craftbukkit." + ReflectionUtil.getServerVersion() + ".CraftWorld").cast(player.getWorld());
        Method getHandleMethod = craftWorld.getClass().getMethod("getHandle");
        Object worldServer = getHandleMethod.invoke(craftWorld);

        Method ws_Z = worldServer.getClass().getMethod("Z");
        Method ws_ab = worldServer.getClass().getMethod("ab");
        Method ws_A = worldServer.getClass().getMethod("A");
        Method ws_ae = worldServer.getClass().getMethod("ae");
        Method ws_z = worldServer.getClass().getMethod("z");

        Field entityPlayer_d_field = entityPlayer.getClass().getField("d");
        Object entityPlayer_d = entityPlayer_d_field.get(entityPlayer);
        Method entityPlayer_d_b = entityPlayer_d.getClass().getMethod("b");
        Method entityPlayer_d_c = entityPlayer_d.getClass().getMethod("c");
        Method entityPlayer_gi = entityPlayer.getClass().getMethod("gi");

        Method biomeManager_a = Class.forName("net.minecraft.world.level.biome.BiomeManager").getMethod("a", long.class);

        Constructor<?> packetConstructor = packetClass.getConstructor(ws_Z.getReturnType(), ws_ab.getReturnType(),
                biomeManager_a.getReturnType(), entityPlayer_d_b.getReturnType(), entityPlayer_d_c.getReturnType(),
                ws_ae.getReturnType(), ws_z.getReturnType(), byte.class, entityPlayer_gi.getReturnType());
        return packetConstructor.newInstance(ws_Z.invoke(worldServer), ws_ab.invoke(worldServer),
                biomeManager_a.invoke(null, ws_A.invoke(worldServer)),
                entityPlayer_d_b.invoke(entityPlayer_d), entityPlayer_d_c.invoke(entityPlayer_d),
                ws_ae.invoke(worldServer), ws_z.invoke(worldServer), (byte) 3, entityPlayer_gi.invoke(entityPlayer));
    }

    public static Object createTeleportPacket(Player player) throws Exception {
        Class<?> packetClass = ReflectionUtil.getPacketClass("game.PacketPlayOutPosition");
        Constructor<?> packetConstructor = packetClass.getConstructor(double.class, double.class, double.class,
                float.class, float.class, Set.class, int.class);
        return packetConstructor.newInstance(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                player.getLocation().getYaw(), player.getLocation().getPitch(), Collections.emptySet(), -1337);
    }
}
