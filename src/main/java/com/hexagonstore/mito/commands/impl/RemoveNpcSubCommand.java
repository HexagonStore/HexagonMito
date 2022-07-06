package com.hexagonstore.mito.commands.impl;

import com.hexagonstore.mito.commands.sub.SubCommand;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.npc.NPCSpawner;
import com.hexagonstore.mito.utils.EC_Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveNpcSubCommand implements SubCommand {

    private EC_Config config;
    private MitoManager manager;
    private NPCSpawner npcSpawner;

    public RemoveNpcSubCommand(EC_Config config, MitoManager manager, NPCSpawner npcSpawner) {
        this.config = config;
        this.manager = manager;
        this.npcSpawner = npcSpawner;
    }

    private static float randomVector() {
        return -0.1f + (float)(Math.random() * 0.2);
    }

    @Override
    public void execute(CommandSender s, String[] a) {
        if(!(s instanceof Player)) {
            s.sendMessage(config.getString("Messages.no_console").replace("&", "§"));
            return;
        }

        Player player = (Player) s;
        if(!player.hasPermission(config.getString("cmd permission.removenpc"))) {
            player.sendMessage(config.getString("Messages.no_permission").replace("&", "§"));
            return;
        }

        if(manager.getMitoLoc() == null) {
            player.sendMessage(config.getString("Messages.not_spawned").replace("&", "§"));
            return;
        }

        npcSpawner.unset();
        manager.setMitoLoc(null);
        player.sendMessage(config.getString("Messages.unsetnpc").replace("&", "§"));
    }
}
