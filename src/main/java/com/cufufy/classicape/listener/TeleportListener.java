package com.cufufy.classicape.listener;

import com.cufufy.classicape.ClassiCape;
import com.cufufy.classicape.cape.PlayerCape;
import com.cufufy.classicape.manager.CapeManager;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportListener implements Listener {

    private CapeManager capeManager = ClassiCape.getInstance().getCapeManager();


    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e) {

        if (capeManager.hasCape(e.getPlayer())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player player = e.getPlayer();
                    PlayerCape playerCape = capeManager.getCurrentCape(player);

                    playerCape.getCapeDisplay().setLocation(SpigotConversionUtil.fromBukkitLocation(player.getLocation()));
                    playerCape.respawn(player, player);

                    //spawn for others
                    playerCape.spawnForOthers(player);
                }
            }.runTaskLater(ClassiCape.getInstance(), 20L);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
            return;
        }
        Player player = event.getPlayer();
       if (capeManager.hasCape(player)) {
           new BukkitRunnable() {
               @Override
               public void run() {
                   PlayerCape playerCape = capeManager.getCurrentCape(player);
                   playerCape.respawn(player, player);
               }
           }.runTaskLater(ClassiCape.getInstance(), 10L);

        }
    }
}
