package com.hexagonstore.mito.utils.nms;

import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.utils.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NMSUtils {

    public void sendActionbar(Player player, String message, int duration) {
        new BukkitRunnable() {
            int seconds = 1;
            public void run() {
                HotbarMessager.sendHotBarMessage(player, message);
                if(seconds >= duration) cancel();
                seconds++;
            }
        }.runTaskTimer(MitoPlugin.getPlugin(), 0L, 20L);
    }

    public void sendTitle(Player player, String title, String subTitle, int fadeInTime, int showTime, int fadeOutTime) {
        ReflectionUtils.PackageType packageType = ReflectionUtils.PackageType.MINECRAFT_SERVER;

        try {
            Constructor<?> titleConstructor = packageType.getClass("PacketPlayOutTitle")
                    .getConstructor(packageType.getClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                            packageType.getClass("IChatBaseComponent"), int.class, int.class, int.class);

            Object objTitle = packageType.getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}"),
                    objSubTitle = packageType.getClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\": \"" + subTitle + "\"}");

            Object packetTitle = titleConstructor.newInstance(packageType.getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), objTitle, fadeInTime, showTime, fadeOutTime),
                    packetSubTitle = titleConstructor.newInstance(packageType.getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), objSubTitle, fadeInTime, showTime, fadeOutTime);

            titleConstructor.newInstance(packageType.getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null), objTitle, fadeInTime, showTime, fadeOutTime);
            titleConstructor.newInstance(packageType.getClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null), objSubTitle, fadeInTime, showTime, fadeOutTime);

            ReflectionUtils.sendPacket(player, packetTitle);
            ReflectionUtils.sendPacket(player, packetSubTitle);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
