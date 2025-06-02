package com.cufufy.classicape.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.cufufy.classicape.cape.Cape;
import com.cufufy.classicape.database.Database;
import com.cufufy.classicape.database.SyncronizableDatabase;
import com.cufufy.classicape.inventory.OfficialShopCapeInventory;
import com.cufufy.classicape.language.LanguageManager;
import com.cufufy.classicape.manager.CapeManager;
import com.cufufy.classicape.ClassiCape;
import com.cufufy.classicape.inventory.CapeInventory;
import com.cufufy.classicape.utils.ItemEditor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@CommandAlias("%classicape")
public class CapeCommand extends BaseCommand {

    private final ClassiCape instance;
    private final CapeManager capeManager;
    private final LanguageManager languageManager;
    private final PlayerManager playerManager;

    public CapeCommand(ClassiCape instance) {
        this.instance = instance;
        this.capeManager = instance.getCapeManager();
        this.languageManager = instance.getLanguageManager();
        this.playerManager = instance.getPlayerManager();
    }

    @Default
    @CommandPermission("classicape.help")
    @Subcommand("help")
    public void onCapeHelp(Player player) {
        List<String> help = languageManager.getLanguage().help_commands;
        for (String s : help) {
            player.sendMessage(s);
        }

        if (player.hasPermission("classicape.admin")) {
            player.sendMessage("§e/classicape reload §7- §fReload the capes");
            player.sendMessage("§e/classicape sync §7- §fSynchronize the local configuration with the database");
        }

    }

    boolean toggleCape = false;

    @CommandPermission("classicape.admin")
    @Subcommand("test")
    public void onCapeTest(Player player) {
        byte isCape = (byte) (toggleCape ? 126 : 127);
        EntityData data = new EntityData(17, EntityDataTypes.BYTE, (byte) isCape);
        List<EntityData<?>> metadataList = java.util.List.of(data);
        WrapperPlayServerEntityMetadata metadataPacket = new WrapperPlayServerEntityMetadata(player.getEntityId(), metadataList);
        playerManager.sendPacket(player, metadataPacket);
        toggleCape = !toggleCape;
        player.sendMessage("§aCape toggled to " + (toggleCape ? "ON" : "OFF"));
    }


    @Syntax("<cape>")
    @CommandCompletion("@capes")
    @Subcommand("apply")
    public void onCapeApply(Player player, Cape cape) {
        if (!capeManager.ownsCape(player, cape)) {
            player.sendMessage(languageManager.getLanguage().no_permission);
            return;
        }
        capeManager.applyCape(player, cape);
    }

    @Syntax("<cape> <player>")
    @CommandCompletion("@capes @players")
    @CommandPermission("classicape.admin")
    @Subcommand("force apply")
    public void onCapeApply(CommandSender sender, Cape cape, Player target) {
        capeManager.applyCape(target, cape);
    }

    @Syntax("<cape>")
    @CommandPermission("classicape.menu")
    @Subcommand("menu")
    public void onCapeMenu(Player player) {
        CapeInventory.getInventory().open(player);
    }


    @Syntax("<cape>")
    @CommandPermission("classicape.shop")
    @Subcommand("shop")
    public void onCapeShop(Player player) {
        if(!instance.getEconomyManager().hasEconomySystem()) {
            player.sendMessage(languageManager.getLanguage().no_eco_plugin);
            return;
        }
        OfficialShopCapeInventory.getInventory().open(player);
    }


    @CommandPermission("classicape.admin")
    @Subcommand("reload")
    public void onCapeReload(CommandSender sender) {
        instance.reloadCommand();
        sender.sendMessage("§aCapes reloaded.");
    }

    @CommandPermission("classicape.admin")
    @Subcommand("sync")
    public void onCapeSync(CommandSender sender) {
        Database db = instance.getDatabaseManager().getDatabase();
        if(db instanceof SyncronizableDatabase) {
            ((SyncronizableDatabase) db).synchronize();
            sender.sendMessage("§aCapes synchronized.");
        } else {
            sender.sendMessage("§cThis database is not syncronizable.");
        }
    }

}
