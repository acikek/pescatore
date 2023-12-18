package com.acikek.pescatore.datagen;

import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class PescatoreLanguage extends FabricLanguageProvider {

    public PescatoreLanguage(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(PescatoreItems.ROOKIE_ROD, "Rookie Rod");
        translationBuilder.add(PescatoreItems.ADEPT_ROD, "Adept Rod");
        translationBuilder.add(PescatoreItems.EXPERT_ROD, "Expert Rod");
    }
}
