package com.hexagonstore.mito.events;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;
import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UltimateChatEvent implements Listener {

    private final MitoManager manager;
    private final EC_Config config;

    public UltimateChatEvent(MitoPlugin main, MitoManager manager, EC_Config config) {
        this.manager = manager;
        this.config = config;

        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    private void evento(SendChannelMessageEvent e) {
        if (manager.isMito(e.getSender().getName()))
            e.addTag(config.getString("key mito"), ChatColor.translateAlternateColorCodes('&', config.getString("Tag mito")));
    }
}
