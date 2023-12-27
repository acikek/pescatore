package com.acikek.pescatore.datagen;

import com.acikek.pescatore.advancement.MinigameFishCaughtCriterion;
import com.acikek.pescatore.api.PescatoreAPI;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Item;
import net.minecraft.predicate.NumberRange;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PescatoreAdvancements extends FabricAdvancementProvider {

    public static final Identifier BACKGROUND = new Identifier("textures/block/sand.png");

    public AdvancementEntry root;
    public AdvancementEntry novice;
    public AdvancementEntry veryRare;

    protected PescatoreAdvancements(FabricDataOutput output) {
        super(output);
    }

    public static Advancement.Builder builder(Item item, String name, Identifier background, AdvancementFrame frame, boolean hidden) {
        return new Advancement.Builder().display(
                item,
                Text.translatable("advancement.pescatore." + name + ".title"),
                Text.translatable("advancement.pescatore." + name + ".description"),
                background, frame, true, background == null, hidden
        );
    }

    @Override
    public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
        root = builder(PescatoreItems.ROOKIE_ROD, "root", BACKGROUND, AdvancementFrame.TASK, false)
                .criterion("has_rookie_rod", InventoryChangedCriterion.Conditions.items(PescatoreItems.ROOKIE_ROD))
                .build(consumer, "pescatore/root");
        generateRods(consumer);
        generateTotalFishCaught(consumer);
        generateUniqueFish(consumer);
        generateAllTypesCaught(consumer);
    }

    public void generateRods(Consumer<AdvancementEntry> consumer) {
        var adeptRod = builder(PescatoreItems.ADEPT_ROD, "adept_rod", null, AdvancementFrame.TASK, false)
                .parent(root)
                .criterion("has_adept_rod", InventoryChangedCriterion.Conditions.items(PescatoreItems.ADEPT_ROD))
                .build(consumer, "pescatore/adept_rod");
        var expertRod = builder(PescatoreItems.EXPERT_ROD, "expert_rod", null, AdvancementFrame.GOAL, false)
                .parent(adeptRod)
                .criterion("has_expert_rod", InventoryChangedCriterion.Conditions.items(PescatoreItems.EXPERT_ROD))
                .build(consumer, "pescatore/expert_rod");
        builder(PescatoreItems.AETHER_ROD, "aether_rod", null, AdvancementFrame.CHALLENGE, true)
                .parent(expertRod)
                .criterion("has_aether_rod", InventoryChangedCriterion.Conditions.items(PescatoreItems.AETHER_ROD))
                .build(consumer, "pescatore/aether_rod");
    }

    public void generateTotalFishCaught(Consumer<AdvancementEntry> consumer) {
        novice = builder(PescatoreItems.GOLDFISH, "novice", null, AdvancementFrame.TASK, false)
                .parent(root)
                .criterion("catch_5_fish", MinigameFishCaughtCriterion.Conditions.totalCaught(NumberRange.IntRange.atLeast(5)))
                .build(consumer, "pescatore/novice");
        var apprentice = builder(PescatoreItems.CARP, "apprentice", null, AdvancementFrame.TASK, false)
                .parent(novice)
                .criterion("catch_30_fish", MinigameFishCaughtCriterion.Conditions.totalCaught(NumberRange.IntRange.atLeast(30)))
                .build(consumer, "pescatore/apprentice");
        var maven = builder(PescatoreItems.BULLHEAD, "maven", null, AdvancementFrame.GOAL, false)
                .parent(apprentice)
                .criterion("catch_100_fish", MinigameFishCaughtCriterion.Conditions.totalCaught(NumberRange.IntRange.atLeast(100)))
                .build(consumer, "pescatore/maven");
        builder(PescatoreItems.ARAPAIMA, "extraordinaire", null, AdvancementFrame.CHALLENGE, false)
                .parent(maven)
                .criterion("catch_300_fish", MinigameFishCaughtCriterion.Conditions.totalCaught(NumberRange.IntRange.atLeast(300)))
                .build(consumer, "pescatore/extraordinaire");
    }

    public void generateUniqueFish(Consumer<AdvancementEntry> consumer) {
        var rare = builder(PescatoreItems.PIRANHA, "rare", null, AdvancementFrame.TASK, false)
                .parent(novice)
                .criterion("catch_rare_fish", MinigameFishCaughtCriterion.Conditions.rarityCaught(MinigameFishRarity.RARE, NumberRange.IntRange.ANY))
                .build(consumer, "pescatore/rare");
        veryRare = builder(PescatoreItems.COELACANTH, "very_rare", null, AdvancementFrame.GOAL, false)
                .parent(rare)
                .criterion("catch_very_rare_fish", MinigameFishCaughtCriterion.Conditions.rarityCaught(MinigameFishRarity.VERY_RARE, NumberRange.IntRange.ANY))
                .build(consumer, "pescatore/very_rare");
        builder(PescatoreItems.TUNA, "hawg", null, AdvancementFrame.TASK, false)
                .parent(novice)
                .criterion("catch_hawg", MinigameFishCaughtCriterion.Conditions.size(NumberRange.DoubleRange.atLeast(MinigameFishSize.HAWG.scale())))
                .build(consumer, "pescatore/hawg");
    }

    public void generateAllTypesCaught(Consumer<AdvancementEntry> consumer) {
        var builder = builder(PescatoreItems.THE_CUBE, "all", null, AdvancementFrame.CHALLENGE, false).parent(veryRare);
        for (MinigameFishType type : PescatoreAPI.getFishTypes()) {
            String name = type.getId().withPrefixedPath("catch_").getPath();
            builder.criterion(name, MinigameFishCaughtCriterion.Conditions.type(type));
        }
        builder.build(consumer, "pescatore/all");
    }
}
