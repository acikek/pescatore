package com.acikek.pescatore.datagen;

import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.io.IOException;

public class PescatoreLanguage extends FabricLanguageProvider {

    public PescatoreLanguage(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(PescatoreItems.ROOKIE_ROD, "Rookie Rod");
        builder.add(PescatoreItems.ADEPT_ROD, "Adept Rod");
        builder.add(PescatoreItems.EXPERT_ROD, "Expert Rod");
        builder.add(PescatoreItems.GOLDFISH, "Goldfish");
        builder.add(PescatoreItems.SARDINE, "Sardine");
        builder.add(PescatoreItems.CRUCIAN_CARP, "Crucian Carp");
        builder.add(PescatoreItems.OLIVE_FLOUNDER, "Olive Flounder");
        builder.add(PescatoreItems.CARP, "Carp");
        builder.add(PescatoreItems.RAINBOW_TROUT, "Rainbow Trout");
        builder.add(PescatoreItems.RED_SNAPPER, "Red Snapper");
        builder.add(PescatoreItems.BULLHEAD, "Bullhead");
        builder.add(PescatoreItems.SEA_BASS, "Sea Bass");
        builder.add(PescatoreItems.TUNA, "Tuna");
        builder.add(PescatoreItems.COELACANTH, "Coelacanth");
        builder.add(PescatoreItems.PIRANHA, "Piranha");
        builder.add(PescatoreItems.STURGEON, "Sturgeon");
        builder.add(PescatoreItems.ARAPAIMA, "Arapaima");
        builder.add(PescatoreItems.OCTOPUS, "Octopus");
        builder.add(PescatoreItems.THE_CUBE, "The Cube");
        buildExisting(builder);
    }

    public void buildExisting(TranslationBuilder builder) {
        try {
            var existing = dataOutput.getModContainer().findPath("assets/pescatore/lang/en_us.existing.json");
            if (existing.isPresent()) {
                builder.add(existing.get());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
