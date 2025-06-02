package com.cufufy.classicape.database;

import com.cufufy.classicape.ClassiCape;
import com.cufufy.classicape.cape.Cape;
import com.cufufy.classicape.manager.CapeManager;
import com.cufufy.classicape.manager.ClassiManager;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CapesFile {


    private final ClassiCape instance;
    private ClassiManager manager;
    @Setter
    private CapeManager capeManager;
    public CapesFile(ClassiCape instance) {
        this.instance = instance;
        this.manager = instance.getClassiManager();

    }



    private void loadCapes(File file) {
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        for (String key : data.getKeys(false)) {
            String texture = data.getString(key + ".texture");
            String name = data.getString(key + ".name");
            String description = data.getString(key + ".description");
            Integer price = data.getInt(key + ".price");
            Integer limitedEdition = data.getInt(key + ".limited_edition");
            boolean enabled = data.getBoolean(key + ".enabled");
            Integer numberSold = data.getInt(key + ".number_sold");
            Cape cape = new Cape(key, enabled, texture, name, description, price, limitedEdition);
            cape.setNumberSold(numberSold);
            capeManager.registerCape(key, cape);
        }
    }

    private void loadDefaultCapes(File file) {
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        List<Cape> capes = capeManager.DEFAULT_CAPES;
        for (Cape cape : capes) {
            data.set(cape.getKey() + ".enabled", cape.isEnabled());
            data.set(cape.getKey() + ".texture", cape.getTexture());
            data.set(cape.getKey() + ".name", cape.getName());
            data.set(cape.getKey() + ".description", cape.getDescription());
            data.set(cape.getKey() + ".price", cape.getPrice());
            data.set(cape.getKey() + ".limited_edition", cape.getLimitedEdition());
            data.set(cape.getKey() + ".number_sold", cape.getNumberSold());
            capeManager.registerCape(cape.getKey(), cape);
        }
        try {
            data.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File initCapeFile() {
        File file = new File(instance.getDataFolder(), "capes.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                loadDefaultCapes(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loadCapes(file);
        }
        return file;
    }

}
