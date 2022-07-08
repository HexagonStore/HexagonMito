package com.hexagonstore.mito.events;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LegendChatEvent implements Listener {

    private final MitoManager manager;
    private final EC_Config config;

    public LegendChatEvent(MitoPlugin main, MitoManager manager, EC_Config config) {
        this.manager = manager;
        this.config = config;

        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void evento(ChatMessageEvent e) {
        if (e.getTags().contains(config.getString("key mito")) && manager.isMito(e.getSender().getName()))
            e.setTagValue(config.getString("key mito"), ChatColor.translateAlternateColorCodes('&', config.getString("Tag mito")));
    }
}
