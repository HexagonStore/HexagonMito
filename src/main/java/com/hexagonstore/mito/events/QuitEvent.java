package com.hexagonstore.mito.events;

import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.task.MitoJoinTask;
import com.hexagonstore.mito.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    private final MitoManager manager;
    private final EC_Config config;

    public QuitEvent(MitoPlugin main, MitoManager manager, EC_Config config) {
        this.manager = manager;
        this.config = config;

        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    void evento(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if(manager.isMito(player.getName())) {
            config.set("Mito.lastPlayed", player.getLastPlayed());
            config.saveConfig();

            MitoJoinTask.runMitoJoin();
            if(config.getBoolean("destaque.quit.ativar"))  {
                e.setQuitMessage(config.getString("destaque.quit.message").replace("{player}", player.getName()).replace("&", "§"));
            }
        }
    }
}
