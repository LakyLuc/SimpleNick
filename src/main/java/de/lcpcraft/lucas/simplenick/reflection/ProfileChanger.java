package de.lcpcraft.lucas.simplenick.reflection;

import de.lcpcraft.lucas.simplenick.SimpleNick;
import de.lcpcraft.lucas.simplenick.utils.SkinProperty;
import de.lcpcraft.lucas.simplenick.utils.TexturesModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

public class ProfileChanger {
    public static void changeName(Player player, String name) {
        changeName(player, name, false);
    }

    public static void changeName(Player player, String name, boolean disabling) {
        try {
            Method getProfile = player.getClass().getMethod("getProfile");
            getProfile.setAccessible(true);
            Object profile = getProfile.invoke(player);
            Field ff = profile.getClass().getDeclaredField("name");
            ff.setAccessible(true);
            ff.set(profile, name);
            if (!disabling)
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.canSee(player)) {
                        p.hidePlayer(SimpleNick.plugin, player);
                        p.showPlayer(SimpleNick.plugin, player);
                    }
                }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void cacheSkin(Player player) {
        try {
            Object propertiesMap = getProfilePropertiesMap(player);
            Object property = getProfileProperty(propertiesMap);
            if (property != null) {
                Method getValue = property.getClass().getMethod("getValue");
                getValue.setAccessible(true);
                String value = (String) getValue.invoke(property);
                Method getSignature = property.getClass().getMethod("getSignature");
                getSignature.setAccessible(true);
                String signature = (String) getSignature.invoke(property);
                SkinProperty skinProperty = new SkinProperty(value, signature);
                TexturesModel texturesModel = new TexturesModel(player.getUniqueId(), player.getName(), new SkinProperty[]{skinProperty});
                SimpleNick.skinManager.cache(texturesModel);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static boolean changeSkin(Player player, UUID skinOwner) {
        Optional<TexturesModel> skin = SimpleNick.skinManager.getSkin(skinOwner);
        if (skin.isPresent()) {
            TexturesModel texturesModel = skin.get();
            if (texturesModel.getProperties() != null && texturesModel.getProperties().length > 0)
                return changeSkin(player, texturesModel.getProperties()[0]);
        }
        return false;
    }

    public static boolean changeSkin(Player player, TexturesModel texturesModel) {
        if (texturesModel.getProperties() != null && texturesModel.getProperties().length > 0)
            return changeSkin(player, texturesModel.getProperties()[0]);
        return false;
    }

    public static boolean downloadSkin(UUID skinOwner) {
        Optional<TexturesModel> skin = SimpleNick.skinManager.getSkin(skinOwner);
        if (skin.isPresent()) {
            TexturesModel texturesModel = skin.get();
            return texturesModel.getProperties() != null && texturesModel.getProperties().length > 0;
        }
        return false;
    }

    private static boolean changeSkin(Player player, SkinProperty skin) {
        try {
            Object propertiesMap = getProfilePropertiesMap(player);
            Object property = getProfileProperty(propertiesMap);
            if (property != null) {
                Field value = property.getClass().getDeclaredField("value");
                value.setAccessible(true);
                value.set(property, skin.getValue());
                Field signature = property.getClass().getDeclaredField("signature");
                signature.setAccessible(true);
                signature.set(property, skin.getSignature());
                Bukkit.getScheduler().runTaskLater(SimpleNick.plugin, () -> {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.canSee(player)) {
                            p.hidePlayer(SimpleNick.plugin, player);
                            p.showPlayer(SimpleNick.plugin, player);
                        }
                    }
                }, 1L);
                PacketSender.sendPacket(player, PacketCreator.createPlayerInfoRemovePacket(player.getUniqueId()));
                PacketSender.sendPacket(player, PacketCreator.createPlayerInfoAddPacket(player));
                PacketSender.sendPacket(player, PacketCreator.createRespawnPacket(player));
                PacketSender.sendPacket(player, PacketCreator.createTeleportPacket(player));
                player.updateInventory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Object getProfilePropertiesMap(Player player) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method getProfile = player.getClass().getMethod("getProfile");
        getProfile.setAccessible(true);
        Object profile = getProfile.invoke(player);
        Method getProperties = profile.getClass().getMethod("getProperties");
        getProperties.setAccessible(true);
        return getProperties.invoke(profile);
    }

    private static Object getProfileProperty(Object propertiesMap) {
        try {
            Field properties = propertiesMap.getClass().getDeclaredField("properties");
            properties.setAccessible(true);
            Object multimap = properties.get(propertiesMap);
            Method values = multimap.getClass().getMethod("values");
            values.setAccessible(true);
            Object collection = values.invoke(multimap);
            Method isEmpty = collection.getClass().getMethod("isEmpty");
            isEmpty.setAccessible(true);
            boolean empty = (boolean) isEmpty.invoke(collection);
            if (!empty) {
                Method iterator = collection.getClass().getMethod("iterator");
                iterator.setAccessible(true);
                Object iteratorObject = iterator.invoke(collection);
                Method next = iteratorObject.getClass().getMethod("next");
                next.setAccessible(true);
                return next.invoke(iteratorObject);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
