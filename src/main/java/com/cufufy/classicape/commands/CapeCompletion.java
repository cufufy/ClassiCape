package com.cufufy.classicape.commands;

import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import com.cufufy.classicape.manager.CapeManager;
import com.cufufy.classicape.ClassiCape;

import java.util.Set;
import java.util.stream.Collectors;

public class CapeCompletion {

    private ClassiCape instance;
    private CapeManager capeManager;
    private PaperCommandManager manager;

    public CapeCompletion(ClassiCape instance) {
        this.instance = instance;
        this.capeManager = instance.getCapeManager();
        this.manager = instance.getCommandManager();
    }

    public void register() {
        CommandCompletions<BukkitCommandCompletionContext> commandCompletions = manager.getCommandCompletions();
        commandCompletions.registerAsyncCompletion("capes", c -> {
            Set<String> list = instance.getCapeManager().getCapes().keySet().stream().filter(
                    cape -> capeManager.ownsCape(c.getPlayer(), capeManager.getCape(cape))
            ).collect(Collectors.toSet());
            if (list.isEmpty()) return null;
            else return list;
        });


    }
}
