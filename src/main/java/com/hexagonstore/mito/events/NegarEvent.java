package com.hexagonstore.mito.events;

import com.ystoreplugins.yx1.api.event.PlayerDenyX1Event;
import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NegarEvent implements Listener {

    private final MitoManager manager;
    private final EC_Config config;

    public NegarEvent(MitoPlugin main, MitoManager manager, EC_Config config) {
        this.manager = manager;
        this.config = config;

        Bukkit.getServer().getPluginManager().registerEvents(this, main);
    }
    @EventHandler
    void evento(PlayerDenyX1Event e) {
        Player player = e.getPlayer();
        if(manager.isMito(player.getName())) {
            double negacoes = config.getDouble("Mito.negacoes");
            if(negacoes >= config.getDouble("quantidade negar")) {
                for(Player online : Bukkit.getOnlinePlayers()) config.getStringList("Messages.negar").forEach(line -> online.sendMessage(line.replace("{quantidade}", String.valueOf(negacoes)).replace("{mito}", manager.getMito()).replace("&", "§")));
                ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                onlinePlayers.remove(player);
                OfflinePlayer newMito;
                if(Bukkit.getOnlinePlayers().size() == 0)  {
                    newMito = new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers())).get(new Random().nextInt(Bukkit.getOfflinePlayers().length));
                }else {
                    newMito = new ArrayList<>(Bukkit.getOnlinePlayers()).get(new Random().nextInt(Bukkit.getOnlinePlayers().size())).getPlayer();
                    if (newMito == null)
                        newMito = new ArrayList<>(Arrays.asList(Bukkit.getOfflinePlayers())).get(new Random().nextInt(Bukkit.getOfflinePlayers().length));
                }
                manager.updateMito(newMito.getName());
                return;
            }
            config.set("Mito.negacoes", negacoes + 1);
            config.saveConfig();
        }
    }
}
