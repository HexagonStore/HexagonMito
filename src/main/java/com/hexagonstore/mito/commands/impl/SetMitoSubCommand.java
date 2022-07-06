package com.hexagonstore.mito.commands.impl;

import com.hexagonstore.mito.commands.sub.SubCommand;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.utils.EC_Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class SetMitoSubCommand implements SubCommand {

    private EC_Config config;
    private MitoManager manager;

    public SetMitoSubCommand(EC_Config config, MitoManager manager) {
        this.config = config;
        this.manager = manager;
    }

    @Override
    public void execute(CommandSender s, String[] a) {
        if (!s.hasPermission(config.getString("cmd permission.setmito"))) {
            s.sendMessage(config.getString("Messages.no_permission").replace("&", "§"));
            return;
        }

        if (a.length < 2) {
            s.sendMessage(config.getString("Messages.no_args.setmito").replace("&", "§"));
            return;
        }
        if (a[1].equalsIgnoreCase("random")) {
            Player newMito = new ArrayList<>(Bukkit.getServer().getOnlinePlayers()).get(new Random().nextInt(Bukkit.getServer().getOnlinePlayers().size()));
            if(newMito == null) {
                s.sendMessage(config.getString("Messages.impossible_random"));
                return;
            }
            manager.updateMito(newMito.getName());
            s.sendMessage(config.getString("Messages.random_setmito").replace("{player}", newMito.getName()).replace("&", "§"));
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(a[1]);
        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            s.sendMessage(config.getString("Messages.player_not_exists").replace("{player}", a[0]).replace("&", "§"));
            return;
        }

        if (manager.getMito() != null && manager.isMito(offlinePlayer.getName())) {
            s.sendMessage(config.getString("Messages.already_mito").replace("{player}", offlinePlayer.getName()).replace("&", "§"));
            return;
        }

        manager.updateMito(offlinePlayer.getName());
        s.sendMessage(config.getString("Messages.setmito").replace("{player}", offlinePlayer.getName()).replace("&", "§"));
    }
}
