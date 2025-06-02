package com.cufufy.classicape.language;

import com.cufufy.classicape.ClassiCape;
import com.cufufy.classicape.language.abstraction.LanguageManagerAbstract;

public class LanguageManager extends LanguageManagerAbstract {

    public LanguageManager(ClassiCape instance) {
        super(instance,new Language(),
                "languages",
                instance.getDataFolder().getPath());
    }

    public Language getLanguage() {
        return (Language) language;
    }

}
