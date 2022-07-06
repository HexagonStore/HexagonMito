package com.hexagonstore.mito.events;

import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.events.api.MitoDeathEvent;
import com.hexagonstore.mito.events.api.MitoKillEvent;
import com.hexagonstore.mito.manager.MitoManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {

    private MitoManager manager;

    public DeathEvent(MitoPlugin main, MitoManager manager) {
        this.manager = manager;

        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    void evento(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        Player player = e.getEntity();

        if(manager.getMito().equalsIgnoreCase(player.getName())) {
            Bukkit.getServer().getPluginManager().callEvent(new MitoDeathEvent(player, killer));
            manager.updateMito(killer.getName());
        }else if(manager.getMito().equalsIgnoreCase(killer.getName())) {
            Bukkit.getServer().getPluginManager().callEvent(new MitoKillEvent(killer, player));
        }
    }
}
