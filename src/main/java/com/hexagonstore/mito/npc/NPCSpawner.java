package com.hexagonstore.mito.npc;

import com.hexagonstore.mito.manager.NPCManager;
import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.utils.EC_Config;
import net.citizensnpcs.api.*;
import net.citizensnpcs.api.npc.NPC;
import com.gmail.filoghost.holographicdisplays.api.*;
import org.bukkit.entity.*;
import org.bukkit.*;

import java.util.*;

import net.citizensnpcs.api.npc.*;

public class NPCSpawner {

    private final MitoManager manager;
    private final NPCManager npcManager;
    private final EC_Config config;
    private final MitoPlugin main;

    public NPCSpawner(MitoManager manager, NPCManager npcManager, EC_Config config, MitoPlugin main) {
        this.manager = manager;
        this.npcManager = npcManager;
        this.config = config;
        this.main = main;
    }

    private int npcID = 39992;
    private String s = "§0§9§b§c";

    public void update() {
        if(manager.getMitoLoc() == null) return;
        final Location location = manager.getMitoLoc();

        for (final NPC npc : CitizensAPI.getNPCRegistry()) if (npc.getId() == npcID) npc.despawn();
        for (final Hologram h : HologramsAPI.getHolograms(main)) h.delete();

        final NPCRegistry nPCRegistry = CitizensAPI.getNPCRegistry();
        final NPC nPC = nPCRegistry.createNPC(EntityType.PLAYER, UUID.randomUUID(), npcID, s);


        final String mito = manager.getMito() == null ? "" : manager.getMito();

        nPC.data().set("player-skin-name", mito.isEmpty() ? config.getString("Mito.sem mito.skin") : mito);
        nPC.spawn(location);

        final Location location2 = location.clone().add(0.0, config.getDouble("NPC." + (mito.isEmpty() ? "sem mito.hologram-distance" : "hologram-distance")), 0.0);
        final Hologram hologram = HologramsAPI.createHologram(main, location2);

        config.getStringList("NPC." + (mito.isEmpty() ? "sem mito.hologram" : "hologram")).forEach(hMsg -> hologram.appendTextLine(hMsg.replace("&", "§").replace("{player}", mito).replace("{mito}", mito)));
        npcManager.setNpcEntity(nPC.getEntity());
    }

    public void set() {
        if (manager.getMitoLoc() != null && CitizensAPI.getNPCRegistry().getById(npcID) != null && CitizensAPI.getNPCRegistry().getById(npcID).isSpawned()) {
            CitizensAPI.getNPCRegistry().getById(npcID).despawn();
            HologramsAPI.getHolograms(main).forEach(Hologram::delete);
        }

        final NPCRegistry nPCRegistry = CitizensAPI.getNPCRegistry();
        final NPC nPC = nPCRegistry.createNPC(EntityType.PLAYER, UUID.randomUUID(), npcID, s);

        final Location location = manager.getMitoLoc();
        final String mito = manager.getMito() == null ? "" : manager.getMito();

        nPC.data().set("player-skin-name", mito.isEmpty() ? config.getString("Mito.sem mito.skin") : mito);
        nPC.spawn(location);

        final Location location2 = location.clone().add(0.0, config.getDouble("NPC." + (mito.isEmpty() ? "sem mito.hologram-distance" : "hologram-distance")), 0.0);
        final Hologram hologram = HologramsAPI.createHologram(main, location2);
        config.getStringList("NPC." + (mito.isEmpty() ? "sem mito.hologram" : "hologram")).forEach(hMsg -> hologram.appendTextLine(hMsg.replace("&", "§").replace("{player}", mito).replace("{mito}", mito)));

        npcManager.setNpcEntity(nPC.getEntity());
        npcManager.hideForAll();
    }

    public void unset() {
        if (manager.getMitoLoc() != null && CitizensAPI.getNPCRegistry().getById(npcID) != null && CitizensAPI.getNPCRegistry().getById(npcID).isSpawned()) {
            CitizensAPI.getNPCRegistry().getById(npcID).despawn();
            HologramsAPI.getHolograms(main).forEach(Hologram::delete);
        }
    }

    public int getNpcID() {
        return npcID;
    }
}