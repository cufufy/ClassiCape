package com.cufufy.classicape.database;

import com.cufufy.classicape.ClassiCape;
import com.cufufy.classicape.ClassiPlayer;
import com.cufufy.classicape.cape.Cape;
import com.cufufy.classicape.manager.CapeManager;
import com.cufufy.classicape.manager.ClassiManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class FileDatabase implements Database {

    private final ClassiCape instance;
    private final ClassiManager manager;
    private final CapeManager capeManager;

    public FileDatabase(ClassiCape instance) {
        this.instance = instance;
        this.manager = instance.getClassiManager();
        this.capeManager = instance.getCapeManager();
    }

    public void initialize() {
        initPlayersFile();

        Bukkit.getOnlinePlayers().forEach(p -> {
            loadPlayer(p.getUniqueId());
        });
    }


    private File initPlayersFile() {
        File file = new File(instance.getDataFolder(), "players.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public void close() {
    }

    @Override
    public void loadPlayer(UUID uuid) {
        File file = initPlayersFile();
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        if (data.contains(uuid.toString())) {
            ClassiPlayer pp = ClassiPlayer
                    .deserialize(uuid, data.getConfigurationSection(uuid.toString()).getValues(true));
            manager.getProdigyPlayers().put(uuid, pp);
        }

    }

    public void savePlayer(UUID uuid) {
        ClassiPlayer pp = manager.getProdigyPlayer(uuid);
        if (pp == null) {
            return;
        }

        if (!pp.hasEdition()) {
            return;
        }
        File file = initPlayersFile();
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> map = pp.serialize();
        data.set(uuid.toString(), map);
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pp.setHasEdition(false);
    }


    @Override
    public void saveOwnedCapes(UUID uuid) {
        File file = initPlayersFile();
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        ClassiPlayer pp = manager.getProdigyPlayer(uuid);
        data.set(uuid + ".owned_capes.", pp.getOwnedCapes());
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCape(String key) {
        Cape cape = capeManager.getCape(key);
        File file = this.capeManager.getCapesFile().initCapeFile();
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        data.set(key + ".enabled", cape.isEnabled());
        data.set(key + ".texture", cape.getTexture());
        data.set(key + ".name", cape.getName());
        data.set(key + ".description", cape.getDescription());
        data.set(key + ".price", cape.getPrice());
        data.set(key + ".limited_edition", cape.getLimitedEdition());
        data.set(key + ".number_sold", cape.getNumberSold());
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        initPlayersFile();
        this.capeManager.getCapesFile().initCapeFile();
    }
}
