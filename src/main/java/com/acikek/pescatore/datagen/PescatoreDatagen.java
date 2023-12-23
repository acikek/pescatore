package com.acikek.pescatore.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PescatoreDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(PescatoreAdvancements::new);
        pack.addProvider(PescatoreLanguage::new);
        pack.addProvider(PescatoreModels::new);
        pack.addProvider(PescatoreRecipes::new);
    }
}
