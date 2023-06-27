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

        Method ws_aa = worldServer.getClass().getMethod("aa");
        Method ws_ac = worldServer.getClass().getMethod("ac");
        Method ws_A = worldServer.getClass().getMethod("A");
        Method ws_af = worldServer.getClass().getMethod("af");
        Method ws_z = worldServer.getClass().getMethod("z");

        Field entityPlayer_e_field = entityPlayer.getClass().getField("e"); // PlayerInteractManager
        Object entityPlayer_e = entityPlayer_e_field.get(entityPlayer);
        Method entityPlayer_e_b = entityPlayer_e.getClass().getMethod("b"); // EnumGamemode
        Method entityPlayer_e_c = entityPlayer_e.getClass().getMethod("c"); // EnumGamemode
        Method entityPlayer_gm = entityPlayer.getClass().getMethod("gm");
        Method entityPlayer_ar = entityPlayer.getClass().getMethod("ar");

        Method biomeManager_a = Class.forName("net.minecraft.world.level.biome.BiomeManager").getMethod("a", long.class);

        // PacketPlayOutRespawn(ResourceKey<DimensionManager> dimensionType, ResourceKey<World> dimension, long sha256Seed, EnumGamemode gameMode,
        // @Nullable EnumGamemode previousGameMode, boolean debugWorld, boolean flatWorld, byte flag, Optional<GlobalPos> lastDeathPos, int portalCooldown)

        Constructor<?> packetConstructor = packetClass.getConstructor(ws_aa.getReturnType(), ws_ac.getReturnType(), long.class,
                entityPlayer_e_b.getReturnType(), entityPlayer_e_c.getReturnType(), ws_af.getReturnType(), ws_z.getReturnType(), byte.class,
                entityPlayer_gm.getReturnType(), entityPlayer_ar.getReturnType());
        return packetConstructor.newInstance(ws_aa.invoke(worldServer), ws_ac.invoke(worldServer),
                biomeManager_a.invoke(null, ws_A.invoke(worldServer)), entityPlayer_e_b.invoke(entityPlayer_e),
                entityPlayer_e_c.invoke(entityPlayer_e), ws_af.invoke(worldServer), ws_z.invoke(worldServer), (byte) 3,
                entityPlayer_gm.invoke(entityPlayer), entityPlayer_ar.invoke(entityPlayer));
    }

    public static Object createTeleportPacket(Player player) throws Exception {
        Class<?> packetClass = ReflectionUtil.getPacketClass("game.PacketPlayOutPosition");
        Constructor<?> packetConstructor = packetClass.getConstructor(double.class, double.class, double.class,
                float.class, float.class, Set.class, int.class);
        return packetConstructor.newInstance(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
                player.getLocation().getYaw(), player.getLocation().getPitch(), Collections.emptySet(), -1337);
    }
}
