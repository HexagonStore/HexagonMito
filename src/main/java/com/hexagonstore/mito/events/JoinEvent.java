package com.hexagonstore.mito.events;

import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private final MitoManager manager;
    private final EC_Config config;

    public JoinEvent(MitoPlugin main, MitoManager manager, EC_Config config) {
        this.manager = manager;
        this.config = config;

        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    void evento(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(manager.isMito(player.getName())) {
            config.set("Mito.lastPlayed", 0);
            config.saveConfig();

            if(manager.getTask() != null) manager.getTask().cancel();
            if(config.getBoolean("destaque.join.ativar")) e.setJoinMessage(config.getString("destaque.join.message").replace("{player}", player.getName()).replace("&", "§"));
        }
    }
}
