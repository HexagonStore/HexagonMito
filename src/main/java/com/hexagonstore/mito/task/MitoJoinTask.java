package com.hexagonstore.mito.task;

import com.hexagonstore.mito.utils.EC_Config;
import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.manager.MitoManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MitoJoinTask {

    private static BukkitTask task;
    private final static MitoManager manager;
    private static long lastPlayed;
    private final static EC_Config config;

    static {
        task = null;
        manager = MitoPlugin.getPlugin().getAPI();
        lastPlayed = MitoPlugin.getPlugin().getCfg().getLong("lastPlayed");
        config = MitoPlugin.getPlugin().getCfg();
    }

    public static boolean isRunning() {
        return task != null;
    }

    public static void runMitoJoin() {
        String mito = manager.getMito();
        if (mito == null) return;
        if(task != null) task.cancel();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(mito);
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(offlinePlayer.isOnline()) cancel();
                long hours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - lastPlayed);
                if (hours >= config.getInt("tempo mito join")) {
                    boolean cancel = false;
                    OfflinePlayer newMito;
                    if(Bukkit.getOnlinePlayers().size() == 0)  {
                        newMito = new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers())).get(new Random().nextInt(Bukkit.getOfflinePlayers().length));
                    }else {
                        newMito = new ArrayList<>(Bukkit.getOnlinePlayers()).get(new Random().nextInt(Bukkit.getOnlinePlayers().size())).getPlayer();
                        if(newMito == null) newMito = new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers())).get(new Random().nextInt(Bukkit.getOfflinePlayers().length));
                        if(newMito.isOnline()) {
                            cancel = true;
                            config.set("Mito.lastPlayed", 0);
                        }else config.set("Mito.lastPlayed", newMito.getLastPlayed());
                        config.saveConfig();
                    }
                    manager.updateMito(newMito.getName());
                    if(cancel) cancel();
                }
            }
        }.runTaskTimer(MitoPlugin.getPlugin(), 0L, 60 * 20L);
    }

    public static long getLastPlayed() {
        return lastPlayed;
    }

    public static void setLastPlayed(long lastPlayed) {
        MitoJoinTask.lastPlayed = lastPlayed;
    }

    public static void cancelTask() {
        if(isRunning()) task.cancel();
    }
}
