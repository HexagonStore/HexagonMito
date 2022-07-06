package com.hexagonstore.mito.commands.impl;

import com.hexagonstore.mito.npc.NPCSpawner;
import com.hexagonstore.mito.utils.EC_Config;
import com.hexagonstore.mito.commands.sub.SubCommand;
import com.hexagonstore.mito.manager.MitoManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetNpcSubCommand implements SubCommand {

    private EC_Config config;
    private MitoManager manager;
    private NPCSpawner npcSpawner;

    public SetNpcSubCommand(EC_Config config, MitoManager manager, NPCSpawner npcSpawner) {
        this.config = config;
        this.manager = manager;
        this.npcSpawner = npcSpawner;
    }

    @Override
    public void execute(CommandSender s, String[] a) {
        if(!(s instanceof Player)) {
            s.sendMessage(config.getString("Messages.no_console").replace("&", "§"));
            return;
        }

        Player player = (Player) s;
        if(!player.hasPermission(config.getString("cmd permission.setnpc"))) {
            player.sendMessage(config.getString("Messages.no_permission").replace("&", "§"));
            return;
        }

        manager.setMitoLoc(player.getLocation());
        npcSpawner.set();
        player.sendMessage(config.getString("Messages.setnpc").replace("&", "§"));
    }
}
