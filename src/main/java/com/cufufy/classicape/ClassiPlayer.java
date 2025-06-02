package com.cufufy.classicape;

import com.cufufy.classicape.cape.Cape;
import com.cufufy.classicape.cape.OwnedCape;
import com.cufufy.classicape.cape.PlayerCape;
import com.cufufy.classicape.manager.CapeManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;


@Getter
@RequiredArgsConstructor
public class ClassiPlayer implements ConfigurationSerializable {

    private static final CapeManager capeManager = ClassiCape.getInstance().getCapeManager();

    @Setter
    private boolean hasEdition = false;

    private final UUID uuid;
    private PlayerCape cape;
    private final Set<OwnedCape> ownedCapes = new HashSet<>();


    public void setCape(PlayerCape cape) {
        this.cape = cape;
        this.hasEdition = true;
    }

    public void setCapeWithoutEdition(PlayerCape cape) {
        this.cape = cape;
    }

    public boolean hasCape() {
        return cape != null;
    }

    public boolean hasEdition() {
        return hasEdition;
    }

    public void addOwnedCape(OwnedCape cape) {
        ownedCapes.add(cape);
        hasEdition = true;
    }

    public void removeOwnedCape(OwnedCape cape) {
        ownedCapes.remove(cape);
        hasEdition = true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("uuid", uuid.toString());
        if (cape != null) {
            data.put("cape", cape.getCape().getKey());
        } else {
            data.put("cape", null);
        }
        List<Map<String, Object>> ownedCapes = new ArrayList<>();
        for (OwnedCape ownedCape : this.ownedCapes) {
            ownedCapes.add(ownedCape.serialize());
        }
        data.put("ownedCapes", ownedCapes);
        return data;
    }


    public static ClassiPlayer deserialize(UUID uuid, Map<String, Object> data) {
        ClassiPlayer pp = new ClassiPlayer(uuid);

        List<Map<String, Object>> ownedCapes = (List<Map<String, Object>>) data.get("ownedCapes");
        if (ownedCapes == null) {
            ownedCapes = new ArrayList<>();
        }
        for (Map<String, Object> ownedCape : ownedCapes) {
            pp.addOwnedCape(OwnedCape.deserialize(ownedCape));
        }

        String capeKey = (String) data.get("cape");
        if (capeKey != null) {
            Cape cape = capeManager.getCape(capeKey);
            if (cape == null) {
                //this means the cape is not available anymore
                pp.setCape(null);
                //we remove the cape from the player owned capes
                pp.ownedCapes.removeIf(ownedCape -> ownedCape.getKey().equals(capeKey));
            } else {
                pp.setCape(new PlayerCape(cape));
            }
        }

        return pp;
    }

    @Override
    public String toString() {
        return "ClassiPlayer{" +
                "hasEdition=" + hasEdition +
                ", uuid=" + uuid +
                ", cape=" + cape +
                ", ownedCapes=" + ownedCapes +
                '}';
    }

}
