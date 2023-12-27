package com.acikek.pescatore.datagen;

import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.acikek.pescatore.api.type.MinigameFishTypes;
import com.acikek.pescatore.item.PescatoreItems;
import com.ibm.icu.lang.UCharacter;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.Item;
import org.apache.commons.lang3.EnumUtils;

import java.util.Locale;

public class PescatoreLanguage extends FabricLanguageProvider {

    public PescatoreLanguage(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    @Override
    public void generateTranslations(TranslationBuilder builder) {
        buildItems(builder);
        buildFilets(builder);
        buildNoFish(builder);
        buildAdvancements(builder);
        buildTypes(builder);
        buildRarities(builder);
        builder.add("stat.pescatore.total_fish_caught", "Total fish caught");
    }

    public void buildItems(TranslationBuilder builder) {
        builder.add(PescatoreItems.ROOKIE_ROD, "Rookie Rod");
        builder.add(PescatoreItems.ADEPT_ROD, "Adept Rod");
        builder.add(PescatoreItems.EXPERT_ROD, "Expert Rod");
        builder.add(PescatoreItems.AETHER_ROD, "Aether Rod");
        builder.add(PescatoreItems.GOLDFISH_CRACKER, "Goldfish Cracker");
        builder.add(PescatoreItems.EMPTY_SARDINE_CAN, "Empty Sardine Can");
        builder.add(PescatoreItems.SARDINE_CAN, "Canned Sardines");
        builder.add(PescatoreItems.OLIVE_FLOUNDER_PLATE, "Plate o' Flounder");
        builder.add(PescatoreItems.COOKED_OLIVE_FLOUNDER_PLATE, "Flounder Piccata");
        builder.add(PescatoreItems.CARP_PLATE, "Plate o' Carp");
        builder.add(PescatoreItems.COOKED_CARP_PLATE, "Fried Carp");
        builder.add(PescatoreItems.RAINBOW_TROUT_PLATE, "Plate o' Trout");
        builder.add(PescatoreItems.COOKED_RAINBOW_TROUT_PLATE, "Baked Rainbow Trout");
        builder.add(PescatoreItems.TUNA_SANDWICH, "Tuna Sandwich");
        builder.add(PescatoreItems.OCTOPUS_TENTACLE, "Octopus Tentacles");
        builder.add(PescatoreItems.COOKED_OCTOPUS_TENTACLE, "Grilled Tentacles");
        builder.add(PescatoreItems.COELACANTH_CHESTPLATE, "Coelacanth Aegis");
        builder.add(PescatoreItems.PIRANHA_TOOTH_NECKLACE, "Piranha Tooth Necklace");
        builder.add(PescatoreItems.ARAPAIMA_LEGGINGS, "Arapaima Swift Chausses");
    }

    public void buildFilet(TranslationBuilder builder, Item filet, Item cooked, String descriptor) {
        String name = descriptor + " Fish Filet";
        builder.add(filet, "Raw " + name);
        builder.add(cooked, name);
    }

    public void buildFilets(TranslationBuilder builder) {
        buildFilet(builder, PescatoreItems.COMMON_FISH_FILET, PescatoreItems.COOKED_COMMON_FISH_FILET, "Mundane");
        buildFilet(builder, PescatoreItems.UNCOMMON_FISH_FILET, PescatoreItems.COOKED_UNCOMMON_FISH_FILET, "Unique");
        buildFilet(builder, PescatoreItems.RARE_FISH_FILET, PescatoreItems.COOKED_RARE_FISH_FILET, "Exotic");
        buildFilet(builder, PescatoreItems.VERY_RARE_FISH_FILET, PescatoreItems.COOKED_VERY_RARE_FISH_FILET, "Extraordinary");
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
        buildAdvancement(builder, "novice", "Fishing Novice", "Reel in five fish");
        buildAdvancement(builder, "apprentice", "Fishing Apprentice", "Catch thirty fish");
        buildAdvancement(builder, "maven", "Fishing Maven", "Catch one hundred fish");
        buildAdvancement(builder, "extraordinaire", "Fishing Extraordinaire", "Catch three hundred fish");
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

    public void buildRarities(TranslationBuilder builder) {
        for (MinigameFishRarity rarity : EnumUtils.getEnumList(MinigameFishRarity.class)) {
            String desc = UCharacter.toTitleCase(Locale.ROOT, rarity.asString().replace('_', ' '), null, 0);
            builder.add(rarity.getStat().getValue().toTranslationKey("stat"), desc + " fish caught");
            builder.add(rarity.tag.id().toTranslationKey("tag.item"), desc + " Fish");
        }
    }
}
