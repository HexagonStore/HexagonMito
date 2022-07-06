package com.hexagonstore.mito.commands.impl;

import com.hexagonstore.mito.utils.EC_Config;
import com.hexagonstore.mito.commands.sub.SubCommand;
import com.hexagonstore.mito.manager.MitoManager;
import org.bukkit.command.CommandSender;


public class UnSetMitoSubCommand implements SubCommand {

    private EC_Config config;
    private MitoManager manager;

    public UnSetMitoSubCommand(EC_Config config, MitoManager manager) {
        this.config = config;
        this.manager = manager;
    }

    @Override
    public void execute(CommandSender s, String[] a) {
        if (!s.hasPermission(config.getString("cmd permission.unsetmito"))) {
            s.sendMessage(config.getString("Messages.no_permission").replace("&", "§"));
            return;
        }
        if (manager.getMito() == null) {
            s.sendMessage(config.getString("Messages.no_has_mito").replace("&", "§"));
            return;
        }

        manager.updateMito(null);
        s.sendMessage(config.getString("Messages.unsetmito").replace("&", "§"));
    }
}
