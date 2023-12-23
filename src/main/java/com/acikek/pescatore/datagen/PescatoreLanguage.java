package com.acikek.pescatore.datagen;

import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.acikek.pescatore.api.type.MinigameFishTypes;
import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

public class PescatoreLanguage extends FabricLanguageProvider {

    public PescatoreLanguage(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    @Override
    public void generateTranslations(TranslationBuilder builder) {
        buildItems(builder);
        buildNoFish(builder);
        buildAdvancements(builder);
        buildTypes(builder);
        buildStats(builder);
    }

    public void buildItems(TranslationBuilder builder) {
        builder.add(PescatoreItems.ROOKIE_ROD, "Rookie Rod");
        builder.add(PescatoreItems.ADEPT_ROD, "Adept Rod");
        builder.add(PescatoreItems.EXPERT_ROD, "Expert Rod");
        builder.add(PescatoreItems.AETHER_ROD, "Aether Rod");
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

    public void buildType(TranslationBuilder builder, MinigameFishType type, String name, String plural) {
        builder.add(type.item().asItem(), name);
        builder.add(type.getStat().getValue().toTranslationKey("stat"), name + plural + " caught");
    }

    public void buildType(TranslationBuilder builder, MinigameFishType type, String name) {
        buildType(builder, type, name, "");
    }

    public void buildTypes(TranslationBuilder builder) {
        buildType(builder, MinigameFishTypes.GOLDFISH, "Goldfish");
        buildType(builder, MinigameFishTypes.SARDINE, "Sardine", "s");
        buildType(builder, MinigameFishTypes.CRUCIAN_CARP, "Crucian Carp");
        buildType(builder, MinigameFishTypes.OLIVE_FLOUNDER, "Olive Flounder", "s");
        buildType(builder, MinigameFishTypes.CARP, "Carp");
        buildType(builder, MinigameFishTypes.RAINBOW_TROUT, "Rainbow Trout");
        buildType(builder, MinigameFishTypes.RED_SNAPPER, "Red Snapper", "s");
        buildType(builder, MinigameFishTypes.BULLHEAD, "Bullhead", "s");
        buildType(builder, MinigameFishTypes.SEA_BASS, "Sea Bass");
        buildType(builder, MinigameFishTypes.TUNA, "Tuna");
        buildType(builder, MinigameFishTypes.COELACANTH, "Coelacanth", "s");
        buildType(builder, MinigameFishTypes.PIRANHA, "Piranha", "s");
        buildType(builder, MinigameFishTypes.STURGEON, "Sturgeon", "s");
        buildType(builder, MinigameFishTypes.ARAPAIMA, "Arapaima", "s");
        buildType(builder, MinigameFishTypes.OCTOPUS, "Octopus", "es");
        buildType(builder, MinigameFishTypes.THE_CUBE, "The Cube");
    }

    public void buildStats(TranslationBuilder builder) {
        builder.add("stat.pescatore.total_fish_caught", "Total fish caught");
        for (MinigameFishRarity rarity : EnumUtils.getEnumList(MinigameFishRarity.class)) {
            String desc = StringUtils.capitalize(rarity.asString().replace('_', ' ')) + " fish caught";
            builder.add(rarity.getStat().getValue().toTranslationKey("stat"), desc);
        }
    }
}
