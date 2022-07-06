package com.hexagonstore.mito.events.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MitoDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player oldMito;
    private Player newMito;

    public MitoDeathEvent(Player oldMito, Player newMito) {
        this.oldMito = oldMito;
        this.newMito = newMito;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
