package com.cufufy.classicape.hook;

import com.cufufy.classicape.ClassiCape;
import com.cufufy.classicape.hook.vault.EconomyManager;

public class HookRegister {


    private ClassiCape instance;
    public HookRegister(ClassiCape instance) {
        this.instance = instance;
    }


    public EconomyManager loadEconomyManager() {
        return new EconomyManager(instance);
    }
}
