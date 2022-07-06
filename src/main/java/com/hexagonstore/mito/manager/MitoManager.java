package com.hexagonstore.mito.manager;

import com.hexagonstore.mito.MitoPlugin;
import com.hexagonstore.mito.npc.NPCSpawner;
import com.hexagonstore.mito.task.MitoJoinTask;
import com.hexagonstore.mito.utils.EC_Config;
import com.hexagonstore.mito.utils.LocationSerializer;
import com.hexagonstore.mito.utils.nms.NMSUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class MitoManager {

    private String mito = null;
    private Location mitoLoc = null;

    private NPCSpawner npcSpawner;
    private final EC_Config config;
    private final MitoPlugin main;
    private final NMSUtils nmsUtils;

    private BukkitTask task = null;

    public MitoManager(MitoPlugin main, NMSUtils nmsUtils, EC_Config config) {
        this.main = main;

        this.config = config;
        this.nmsUtils = nmsUtils;

        if (this.config.getString("Mito.atual") != null && !this.config.getString("Mito.atual").isEmpty() && !this.config.getString("Mito.atual").equalsIgnoreCase("none") && !this.config.getString("Mito.atual").equalsIgnoreCase("nulo") && !this.config.getString("Mito.atual").equalsIgnoreCase("null"))
            this.mito = this.config.getString("Mito.atual");

        if (this.config.getString("Mito.local") != null && !this.config.getString("Mito.local").isEmpty() && !this.config.getString("Mito.local").equalsIgnoreCase("none") && !this.config.getString("Mito.local").equalsIgnoreCase("nulo") && !this.config.getString("Mito.local").equalsIgnoreCase("null"))
            mitoLoc = LocationSerializer.getDeserializedLocation(this.config.getString("Mito.local"));
    }

    private void executeAnimation() {
        if (this.mitoLoc == null) return;
        if (this.mito == null) return;
        if (this.mito.isEmpty()) return;
        mitoLoc.getWorld().strikeLightningEffect(mitoLoc);
        List<Entity> bats = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            val entity = mitoLoc.getWorld().spawnEntity(mitoLoc, EntityType.BAT);
            bats.add(entity);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                bats.forEach(Entity::remove);
            }
        }.runTaskLater(main, 20*5);
    }

    public boolean isMito(String playerName) {
        return mito != null && mito.equalsIgnoreCase(playerName);
    }

    public String getMito() {
        return mito;
    }

    public Location getMitoLoc() {
        return mitoLoc;
    }

    public void setMitoLoc(Location mitoLoc) {
        this.mitoLoc = mitoLoc;

        this.config.set("Mito.local", mitoLoc == null ? "nulo" : LocationSerializer.getSerializedLocation(mitoLoc));
        this.config.saveConfig();
    }

    public void updateMito(String mito) {
        this.setMito(mito);
        if (this.mito == null) MitoJoinTask.cancelTask();
        if (this.mito != null) this.send(this.mito, mito);

        this.config.set("Mito.atual", mito == null ? "nulo" : mito);
        this.config.set("Mito.negacoes", 0);

        this.npcSpawner.update();
        this.executeAnimation();

        this.config.saveConfig();
    }

    public void setMito(String mito) {
        this.mito = mito;
    }

    private void send(String oldMito, String newMito) {
        if (config.getBoolean("destaque.title.ativar")) {
            for (Player player : Bukkit.getOnlinePlayers())
                this.nmsUtils.sendTitle(player, config.getString("destaque.title.line.1").replace("{old_mito}", oldMito).replace("{mito}", newMito).replace("&", "§"), config.getString("destaque.title.line.2").replace("{old_mito}", oldMito).replace("{mito}", newMito).replace("&", "§"), config.getInt("destaque.title.fadeIn"), config.getInt("destaque.title.stay"), config.getInt("destaque.title.fadeOut"));
        }

        if (config.getBoolean("destaque.actionbar.ativar")) for (Player player : Bukkit.getOnlinePlayers()) {
            this.nmsUtils.sendActionbar(player, config.getString("destaque.actionbar.message").replace("{old_mito}", oldMito).replace("{mito}", newMito).replace("&", "§"), config.getInt("destaque.actionbar.duration"));
        }

        if (config.getBoolean("destaque.message.ativar")) {
            config.getStringList("destaque.message.text").forEach(line -> {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.sendMessage(line.replace("{old_mito}", oldMito).replace("{mito}", newMito).replace("&", "§"));
            });
        }

        if (config.getBoolean("destaque.sound.ativar")) {
            Sound sound = Sound.valueOf(config.getString("destaque.sound.som"));
            if (config.getBoolean("destaque.sound.all")) {
                for (Player player : Bukkit.getOnlinePlayers())
                    player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
            } else {
                Player player = Bukkit.getPlayer(newMito);
                player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
            }
        }
    }

    public BukkitTask getTask() {
        return task;
    }

    public void setNpcSpawner(NPCSpawner npcSpawner) {
        this.npcSpawner = npcSpawner;
    }
}
