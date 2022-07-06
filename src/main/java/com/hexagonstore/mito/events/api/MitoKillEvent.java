package com.hexagonstore.mito.events.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MitoKillEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player mito;
    private Player loser;

    public MitoKillEvent(Player mito, Player loser) {
        this.mito = mito;
        this.loser = loser;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
