package com.hexagonstore.mito.commands.sub;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    public void execute(CommandSender s, String[] a);
}
