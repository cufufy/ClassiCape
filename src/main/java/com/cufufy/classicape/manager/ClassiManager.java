package com.cufufy.classicape.manager;

import com.cufufy.classicape.ClassiPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClassiManager {

    private final Map<UUID, ClassiPlayer> prodigyPlayers = new HashMap<>();

    public void loadPlayer(UUID uuid) {
        if(prodigyPlayers.containsKey(uuid)) {
            return;
        }
        ClassiPlayer player = new ClassiPlayer(uuid);
        prodigyPlayers.put(uuid, player);
    }

    public ClassiPlayer getProdigyPlayerOrCreate(UUID uuid) {
        if(!prodigyPlayers.containsKey(uuid)) {
            loadPlayer(uuid);
        }
        return prodigyPlayers.get(uuid);
    }

    public ClassiPlayer getProdigyPlayer(UUID uuid) {
        return prodigyPlayers.get(uuid);
    }


    public Map<UUID, ClassiPlayer> getProdigyPlayers() {
        return prodigyPlayers;
    }
}
