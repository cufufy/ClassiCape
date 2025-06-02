package com.cufufy.classicape.listener;

import com.cufufy.classicape.manager.CapeManager;
import com.cufufy.classicape.cape.PlayerCape;
import com.cufufy.classicape.ClassiCape;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CapeListener implements Listener {

    private final CapeManager capeManager = ClassiCape.getInstance().getCapeManager();

    @EventHandler
    public void sneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();

        if (!capeManager.hasCape(player)) {
            return;
        }
        PlayerCape playerCape = capeManager.getCurrentCape(player);
        if (playerCape.getCapeDisplay() == null) return;
        playerCape.onSneakEvent(e.isSneaking());
    }

}
