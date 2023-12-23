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
        buildItems(builder);
        buildAdvancements(builder);
        buildNoFish(builder);
    }

    public void buildNoFish(TranslationBuilder builder, int i, String message) {
        builder.add("message.pescatore.no_fish_" + i, message);
    }

    public void buildNoFish(TranslationBuilder builder) {
        buildNoFish(builder, 0, "No luck...");
        buildNoFish(builder, 1, "Guess they're not here...");
        buildNoFish(builder, 2, "I should find a better spot...");
        buildNoFish(builder, 3, "This won't work...");
        buildNoFish(builder, 4, "I need to cast farther...");
    }

    public void buildAdvancement(TranslationBuilder builder, String name, String title, String description) {
        builder.add("advancement.pescatore." + name + ".title", title);
        builder.add("advancement.pescatore." + name + ".description", description);
    }

    public void buildAdvancements(TranslationBuilder builder) {
        buildAdvancement(builder, "root", "Pescatore", "Will you catch them all?");
        buildAdvancement(builder, "adept_rod", "Lightning Rod...?", "Upgrade to an Adept Rod");
        buildAdvancement(builder, "expert_rod", "Got a license for that?", "Upgrade to an Expert Rod");
        buildAdvancement(builder, "aether_rod", "For Science!", "Did you really need that?");
        buildAdvancement(builder, "novice", "Fishing Novice", "Reel in your first fish");
        buildAdvancement(builder, "apprentice", "Fishing Apprentice", "Catch ten fish");
        buildAdvancement(builder, "maven", "Fishing Maven", "Catch thirty fish");
        buildAdvancement(builder, "rare", "Afishionado", "Catch a rare fish");
        buildAdvancement(builder, "very_rare", "The Depths", "Catch a very rare fish");
        buildAdvancement(builder, "hawg", "Hawg", "Catch an exceptionally large fish");
        buildAdvancement(builder, "all", "Aquarist of Atlantis", "Catch every type of fish");
    }

    public void buildItems(TranslationBuilder builder) {
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
    }
}
