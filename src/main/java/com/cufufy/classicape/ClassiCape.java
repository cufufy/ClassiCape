package com.cufufy.classicape;

import co.aikar.commands.CommandReplacements;
import co.aikar.commands.PaperCommandManager;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.cufufy.classicape.commands.CapeCommand;
import com.cufufy.classicape.commands.CapeCompletion;
import com.cufufy.classicape.commands.CapeContext;
import com.cufufy.classicape.configs.Configuration;
import com.cufufy.classicape.database.DatabaseManager;
import com.cufufy.classicape.hook.HookRegister;
import com.cufufy.classicape.hook.vault.EconomyManager;
import com.cufufy.classicape.language.LanguageManager;
import com.cufufy.classicape.listener.CapeListener;
import com.cufufy.classicape.listener.JoinQuitListener;
import com.cufufy.classicape.listener.PacketEventsListener;
import com.cufufy.classicape.listener.TeleportListener;
import com.cufufy.classicape.manager.CapeManager;
import com.cufufy.classicape.manager.ClassiManager;

import fr.depends.minuskube.inv.InventoryManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

@Getter
public final class ClassiCape extends JavaPlugin {

    @Getter
    private static ClassiCape instance;
    @Getter
    private static InventoryManager invManager;

    private LanguageManager languageManager;
    private EconomyManager economyManager;
    private PaperCommandManager commandManager;
    private ClassiManager classiManager;
    private CapeManager capeManager;
    private DatabaseManager databaseManager;
    private Configuration configuration;

    private PlayerManager playerManager;


    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false) // read only ?
                .checkForUpdates(true)
                .bStats(false);
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        instance = this;

        this.configuration = new Configuration(this);
        configuration.load();

        this.languageManager = new LanguageManager(this);
        languageManager.load();


        HookRegister hookRegister = new HookRegister(this);
        this.economyManager = hookRegister.loadEconomyManager();


        invManager = new InventoryManager(this);
        invManager.init();


        this.classiManager = new ClassiManager();

        //circular dependency here, capeManager needs databaseManager and databaseManager needs capeManager
        capeManager = new CapeManager(this);

        this.databaseManager = new DatabaseManager(this, configuration.getDatabaseType());
        databaseManager.initialize();
        capeManager.setDatabaseManager(databaseManager);

        capeManager.applyAllCapes();

        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener(this));
        PacketEvents.getAPI().init();
        this.playerManager = PacketEvents.getAPI().getPlayerManager();
        loadCommands();

        SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);

        APIConfig settings = new APIConfig(PacketEvents.getAPI())
                .tickTickables()
                .usePlatformLogger();

        EntityLib.init(platform, settings);

        getServer().getPluginManager().registerEvents(new CapeListener(), this);
        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
        new Metrics(this, 21468);
    }

    public void reloadCommand() {
        this.classiManager.getProdigyPlayers().keySet().forEach(uuid -> {
            this.databaseManager.getDatabase().savePlayer(uuid);
        });

        configuration.load();
        languageManager.load();
        databaseManager.close();
        capeManager.removeAllCapes();
        databaseManager.getDatabase().reload();
        capeManager.clearCapes();
        capeManager.getCapesFile().initCapeFile();
        capeManager.applyAllCapes();
        loadCommands();
    }

    private void loadCommands() {
        this.commandManager = new PaperCommandManager(this);
        commandManager.getLocales().setDefaultLocale(Locale.ENGLISH);
        new CapeCompletion(this).register();
        new CapeContext(this).register();

        CommandReplacements replacements = commandManager.getCommandReplacements();
        replacements.addReplacement("classicape", configuration.getCustomCommand());
        commandManager.registerCommand(new CapeCommand(this));
    }

    @Override
    public void onDisable() {
        this.classiManager.getProdigyPlayers().keySet().forEach(uuid -> {
            this.databaseManager.getDatabase().savePlayer(uuid);
        });
        this.databaseManager.close();
        capeManager.removeAllCapes();

        PacketEvents.getAPI().terminate();
    }

}
