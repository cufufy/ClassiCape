package com.cufufy.classicape.commands;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.CommandContexts;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.cufufy.classicape.cape.Cape;
import com.cufufy.classicape.manager.CapeManager;
import com.cufufy.classicape.ClassiCape;

public class CapeContext {

    private final ClassiCape instance;
    private final PaperCommandManager manager;

    public CapeContext(ClassiCape instance) {
        this.instance = instance;
        this.manager = instance.getCommandManager();
    }

    public void register() {
        CommandContexts<BukkitCommandExecutionContext> commandContexts = manager.getCommandContexts();
        commandContexts.registerContext(Cape.class, c -> {
            String name = c.popFirstArg();
            CapeManager capeManager = instance.getCapeManager();
            Cape cape = capeManager.getCape(name);
            if (cape == null) {
                throw new InvalidCommandArgument("Cape with name " + name + " not found...");
            }
            return cape;
        });

    }
}
