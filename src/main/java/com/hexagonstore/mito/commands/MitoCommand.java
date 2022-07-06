package com.hexagonstore.mito.commands;

import com.hexagonstore.mito.commands.impl.RemoveNpcSubCommand;
import com.hexagonstore.mito.commands.impl.SetNpcSubCommand;
import com.hexagonstore.mito.commands.impl.UnSetMitoSubCommand;
import com.hexagonstore.mito.npc.NPCSpawner;
import com.hexagonstore.mito.utils.EC_Config;
import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.commands.impl.SetMitoSubCommand;
import com.hexagonstore.mito.manager.MitoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MitoCommand implements CommandExecutor {

    private EC_Config config;
    private MitoManager manager;

    private SetMitoSubCommand setMitoSubCommand;
    private SetNpcSubCommand setNpcSubCommand;
    private RemoveNpcSubCommand removeNpcSubCommand;
    private UnSetMitoSubCommand unSetMitoSubCommand;

    public MitoCommand(EC_Config config, NPCSpawner npcSpawner, MitoPlugin main, MitoManager manager) {
        this.config = config;
        this.manager = manager;

        this.setMitoSubCommand = new SetMitoSubCommand(config, manager);
        this.setNpcSubCommand = new SetNpcSubCommand(config, manager, npcSpawner);
        this.removeNpcSubCommand = new RemoveNpcSubCommand(config, manager, npcSpawner);
        this.unSetMitoSubCommand = new UnSetMitoSubCommand(config, manager);

        main.getCommand("mito").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String lb, String[] a) {
        if (!config.getString("cmd permission.mito").isEmpty() && !s.hasPermission(config.getString("cmd permission.mito"))) {
            s.sendMessage(config.getString("Messages.no_permission").replace("&", "§"));
            return true;
        }

        if (a.length == 0) {
            if (manager.getMito() == null || manager.getMito().isEmpty() || manager.getMito().equalsIgnoreCase("none") || manager.getMito().equalsIgnoreCase("nulo") || manager.getMito().equalsIgnoreCase("null")) {
                s.sendMessage(config.getString("Messages.not_has_mito").replace("&", "§"));
            } else s.sendMessage(config.getString("Messages.mito").replace("{mito}", manager.getMito()).replace("&", "§"));
            return true;
        }

        switch (a[0].toLowerCase()) {
            case "set":
            case "setar":
            case "setarmito":
            case "setmito":
                setMitoSubCommand.execute(s, a);
                break;
            case "setnpc":
            case "setarnpc":
                setNpcSubCommand.execute(s, a);
                break;
            case "delnpc":
            case "deletenpc":
            case "deletarnpc":
            case "removernpc":
            case "removenpc":
                removeNpcSubCommand.execute(s, a);
                break;
            case "unsetmito":
            case "delmito":
            case "removemito":
            case "removermito":
            case "deletemito":
            case "reset":
            case "resetmito":
            case "resetar":
            case "resetarmito":
            case "unset":
                unSetMitoSubCommand.execute(s, a);
                break;
            default:
                if (manager.getMito() == null || manager.getMito().isEmpty() || manager.getMito().equalsIgnoreCase("none") || manager.getMito().equalsIgnoreCase("nulo") || manager.getMito().equalsIgnoreCase("null")) {
                    s.sendMessage(config.getString("Messages.not_has_mito").replace("&", "§"));
                } else
                    s.sendMessage(config.getString("Messages.mito").replace("{mito}", manager.getMito()).replace("&", "§"));
                break;
        }
        return false;
    }
}
