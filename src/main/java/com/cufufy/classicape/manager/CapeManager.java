package com.cufufy.classicape.manager;

import com.cufufy.classicape.ClassiCape;
import com.cufufy.classicape.ClassiPlayer;
import com.cufufy.classicape.cape.Cape;
import com.cufufy.classicape.cape.OwnedCape;
import com.cufufy.classicape.cape.PlayerCape;
import com.cufufy.classicape.contributors.CapeContributors;
import com.cufufy.classicape.database.CapesFile;
import com.cufufy.classicape.database.DatabaseManager;
import com.cufufy.classicape.hook.vault.EconomyManager;
import com.cufufy.classicape.language.LanguageManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class CapeManager {

    public static final List<Cape> DEFAULT_CAPES = new ArrayList<>(
            Arrays.asList(
                    new Cape(
                            "mojang",
                            true,
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjc3MDVlM2U5OTdlNWNlNTIxNjY2M2M5ZTY0YjM5NmZhNDNlZGRlODI1NWZkOTEwZjBjYzgxYTAzMjVlNmIifX19",
                            "§4Mojang Staff",
                            "§7Mojang's official cape",
                            0,
                            0
                    ),
                    new Cape(
                            "minecon_creeper",
                            true,
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk3NmFhYzc2MjEwYjAzZTRjMzg5MWJkZjc5OTMyMmUzMGE3ZThhMTI3MmIyNzkwMzI2YmYwOGYyMTkyYWNkNiJ9fX0=",
                            "§cMinecon 2011",
                            "§7Minecon cape from 2011",
                            0,
                            0
                    ),
                    new Cape(
                            "minecon_pickaxe",
                            true,
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRlNDM1OGQ3MzRhNmUwNjhlYjA3Y2I4ZmM1ZmZkZThiOTQ4MDBlYjM5Njc3NzQyOGE0ZjU1OTMxNWExZmY0ZCJ9fX0=",
                            "§2Minecon 2012",
                            "§7Minecon cape from 2012",
                            0,
                            0
                    ),
                    new Cape(
                            "minecon_piston",
                            true,
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNkYjIzZjc1ODI2NTQ4ZDhmYmI1NjY3MTFmYzZkMzAyNjA4Yzk1YTA4MzFiOWY3ZmNjYWQ0MDYwOTlkODYxMyJ9fX0=",
                            "§aMinecon 2013",
                            "§7Minecon cape from 2013",
                            0,
                            0
                    ),
                    new Cape(
                            "minecon_enderman",
                            true,
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWY1Zjc0YzUxYzcwNTgzZTJhYTRmNWQ0NDQ0N2U1OTQ2ZWQ1MjRiM2M5ODk5YjI5NjU2YTNkZDc4ZDhiNDJmOCJ9fX0=",
                            "§5Minecon 2016",
                            "§7Minecon cape from 2016",
                            0,
                            0
                    ),
                    new Cape(
                            "france",
                            true,
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjkwMzM0OWZhNDViZGQ4NzEyNmQ5Y2QzYzZjMGFiYmE3ZGJkNmY1NmZiOGQ3ODcwMTg3M2ExZTdjOGVlMzNjZiJ9fX0",
                            "§2France §fpatriot §4cape",
                            "§7Not a beret, but still a french cape",
                            0,
                            0
                    ),
                    new Cape(
                            "golden",
                            true,
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDdlZWYzMTllMTllOGMwNDY2ZjU5ZjM0Y2M5MWYwMTZkZDY0NzUyNGM1OGUyNWY1MzFlZDhhYmM0ZDUxMjI1ZSJ9fX0=",
                            "§6Your custom cape",
                            "§7You can add any texture you want",
                            0,
                            0
                    )

            )

    );


    @Getter
    private final Map<String, Cape> capes = new HashMap<>();

    @Getter
    public CapeContributors capeContributors;

    private final ClassiCape instance;
    private final ClassiManager classiManager;
    private final EconomyManager economyManager;

    @Setter
    private DatabaseManager databaseManager;

    private final LanguageManager languageManager;
    @Getter
    private final CapesFile capesFile;


    public CapeManager(ClassiCape instance) {
        this.instance = instance;
        this.classiManager = instance.getClassiManager();
        this.capeContributors = new CapeContributors();
        this.economyManager = instance.getEconomyManager();
        this.languageManager = instance.getLanguageManager();
        this.capesFile = new CapesFile(instance);
        this.capesFile.setCapeManager(this);
        //load cape file with default capes
        this.capesFile.initCapeFile();
    }

    public void applyCape(Player player, Cape cape) {
        ClassiPlayer pp = classiManager.getProdigyPlayerOrCreate(player.getUniqueId());
        //if already has a cape
        if (pp.hasCape()) {
            PlayerCape playerCape = pp.getCape();
            playerCape.despawn();
            pp.setCape(null);
            //if the cape was the same
            if (playerCape.getCape().getKey().equals(cape.getKey())) {
                return;
            }
        }
        PlayerCape playerCape = new PlayerCape(cape);
        playerCape.spawn(player);
        pp.setCape(playerCape);
    }

    public boolean purchaseCape(Player player, Cape cape) {
        boolean purchased = this.economyManager.purchase(player, cape.getPrice());
        if (!purchased) {
            return false;
        }

        if(cape.isSoldOut()) {
            player.sendMessage(languageManager.getLanguage().sold_out);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            player.closeInventory();
            return false;
        }

        ClassiPlayer pp = classiManager.getProdigyPlayerOrCreate(player.getUniqueId());
        OwnedCape ownedCape = new OwnedCape(cape.getKey());
        ownedCape.setBoughtPrice(cape.getPrice());
        ownedCape.setBoughtTime(System.currentTimeMillis());
        ownedCape.setEditionNumber(cape.getNumberSold() + 1);
        cape.incrementSold();
        pp.addOwnedCape(ownedCape);

        applyCape(player, cape);
        databaseManager.getDatabase().saveOwnedCapes(player.getUniqueId());
        databaseManager.getDatabase().saveCape(cape.getKey());
        return true;

    }

    public boolean hasCape(Player player) {
        ClassiPlayer pp = classiManager.getProdigyPlayer(player.getUniqueId());
        return pp != null && pp.hasCape();
    }

    public boolean hasCape(Player player, Cape cape) {
        ClassiPlayer pp = classiManager.getProdigyPlayer(player.getUniqueId());
        return pp != null && pp.hasCape() && pp.getCape().getCape().getKey().equals(cape.getKey());
    }

    public PlayerCape getCurrentCape(Player player) {
        ClassiPlayer pp = classiManager.getProdigyPlayer(player.getUniqueId());
        return pp != null && pp.hasCape() ? pp.getCape() : null;
    }

    public boolean isEnabled(Cape cape, Player player) {
        if (!cape.isEnabled() && !player.hasPermission("classicape.cape.bypass")) {
            return false;
        }
        return true;
    }

    public boolean ownsCape(Player player, Cape cape) {
        if (player.hasPermission("classicape.cape." + cape.getKey()) || player.hasPermission("classicape.cape.*")) {

            return true;
        }
        ClassiPlayer pp = classiManager.getProdigyPlayer(player.getUniqueId());
        if (pp == null) return false;
        Set<OwnedCape> ownedCapes = pp.getOwnedCapes();
        return (ownedCapes.stream().anyMatch(ownedCape -> ownedCape.getKey().equals(cape.getKey())));
    }

    public Set<Cape> getOwnedCapes(Player player) {
        ClassiPlayer pp = classiManager.getProdigyPlayerOrCreate(player.getUniqueId());
        if (pp == null) return new HashSet<>();

        return capes.values().stream().filter(cape -> ownsCape(player, cape))
                .collect(Collectors.toSet()).stream()
                .filter(cap -> isEnabled(cap, player)).collect(Collectors.toSet());
    }

    public OwnedCape getOwnedCape(Player player, String key) {
        ClassiPlayer pp = classiManager.getProdigyPlayer(player.getUniqueId());
        if (pp == null) return null;
        OwnedCape cape = pp.getOwnedCapes().stream().filter(ownedCape -> ownedCape.getKey().equals(key)).findFirst().orElse(null);
        return cape;
    }

    public Set<Cape> getPurchasableCapes(Player player) {
        return capes.values().stream()
                .filter(cape -> !ownsCape(player, cape) && cape.hasPrice())
                .filter(cape -> !cape.isSoldOut())
                .collect(Collectors.toSet()).stream()
                .filter(cap -> isEnabled(cap, player)).collect(Collectors.toSet());
    }

    public void registerCape(String key, Cape cape) {
        capes.put(key, cape);
    }


    public Cape getCape(String key) {
        return capes.get(key);
    }

    public void removeAllCapes() {
        classiManager.getProdigyPlayers().values().forEach(pp -> {
            if (pp.hasCape()) {
                pp.getCape().despawn();
                pp.setCapeWithoutEdition(null);
            }
        });
    }

    public void applyAllCapes() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ClassiPlayer pp = classiManager.getProdigyPlayer(player.getUniqueId());
            if (pp != null && pp.hasCape()) {
                pp.getCape().spawn(player);
            }
        });
    }

    public void clearCapes() {
        capes.clear();
    }
}
