package com.hexagonstore.mito;

import com.hexagonstore.mito.commands.MitoCommand;
import com.hexagonstore.mito.events.*;
import com.hexagonstore.mito.manager.NPCManager;
import com.hexagonstore.mito.npc.NPCSpawner;
import com.hexagonstore.mito.task.MitoJoinTask;
import com.hexagonstore.mito.utils.EC_Config;
import com.hexagonstore.mito.manager.MitoManager;
import com.hexagonstore.mito.utils.nms.NMSUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MitoPlugin extends JavaPlugin {

    @Getter
    private static MitoPlugin plugin;

    private EC_Config config;
    private MitoManager manager;

    private NPCManager npcManager;
    private NPCSpawner npcSpawner;

    private NMSUtils nmsUtils;

    @Override
    public void onEnable() {
        plugin = this;

        config = new EC_Config(null, "config.yml", false);

        nmsUtils = new NMSUtils();
        npcManager = new NPCManager(this);

        manager = new MitoManager(this, nmsUtils, config);
        npcSpawner = new NPCSpawner(manager, npcManager, config, this);

        manager.setNpcSpawner(npcSpawner);

        new JoinEvent(this, manager, config);
        new QuitEvent(this, manager, config);
        new DeathEvent(this, manager);

        if(Bukkit.getPluginManager().isPluginEnabled("yX1")) {
            new NegarEvent(this, manager, config);
        }

        new MitoCommand(config, npcSpawner, this, manager);

        if(config.getLong("Mito.lastPlayed") > 0) MitoJoinTask.runMitoJoin();

        hookChat();

        getLogger().info("Plugin habilitado com sucesso.");
    }

    @Override
    public void onDisable() {
        if(manager.getMito() != null) {
            config.set("Mito.lastPlayed", System.currentTimeMillis());
            config.saveConfig();
        }
        getLogger().info("Plugin desabilitado com sucesso.");
    }

    private void hookChat() {
        String chatPlugin = config.getString("Plugin de chat");
        switch (chatPlugin.toLowerCase()) {
            case "ultimatechat":
            case "uchat":
                if (getServer().getPluginManager().getPlugin("UltimateChat") != null) {
                    getLogger().info("UltimateChat detectado, hookando...");
                    new UltimateChatEvent(this, manager, config);
                } else getLogger().info("UltimateChat não está habilitado, o hook não irá ocorrer!");
                break;
            case "openchat":
            case "legendchat":
            case "nchat":
                if (getServer().getPluginManager().getPlugin("Legendchat") != null) {
                    getLogger().info("LegendChat/OpeNChat/nChat detectado, hookando...");
                    new LegendChatEvent(this, manager, config);
                } else getLogger().info("LegendChat/OpeNChat/nChat não está habilitado, o hook não irá ocorrer!");
                break;
            default:
                getLogger().info("Nenhum plugin de chat detectado.");
                break;
        }
    }

    public NMSUtils getNMSUtils() {
        return nmsUtils;
    }
    public MitoManager getAPI() {
        return manager;
    }
    public EC_Config getCfg() {
        return config;
    }
}
