package com.hexagonstore.mito.manager;

import com.hexagonstore.mito.MitoPlugin;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.scoreboard.*;

public class NPCManager implements Listener {

    private Entity npcEntity;
    private final MitoPlugin main;

    public NPCManager(MitoPlugin main) {
        this.main = main;

        Bukkit.getServer().getPluginManager().registerEvents(this, this.main);
        hideForAll();
    }

    @EventHandler
    void evento(PlayerLoginEvent e) {
        Bukkit.getScheduler().runTaskLater(main, () -> {
            try {
                hide(e.getPlayer());
            } catch (Exception ignored) {}
        }, 20L);
    }

    public void hideForAll() {
        Bukkit.getScheduler().runTaskLater(main, () -> Bukkit.getOnlinePlayers().forEach(this::hide), 20L);
    }

    public void hide(final Player from) {
        if (this.npcEntity == null) return;
        final Player npc = (Player) this.npcEntity;
        final Scoreboard score = from.getScoreboard();
        Team team = null;
        for (final Team t : score.getTeams()) {
            if (t.getName().equalsIgnoreCase("NPCs")) team = t;
        }
        if (team == null) team = score.registerNewTeam("NPCs");

        team.addEntry(npc.getName());
        team.setNameTagVisibility(NameTagVisibility.NEVER);
        from.setScoreboard(score);
    }

    public Entity getNpcEntity() {
        return npcEntity;
    }

    public void setNpcEntity(Entity npcEntity) {
        this.npcEntity = npcEntity;
    }
}
