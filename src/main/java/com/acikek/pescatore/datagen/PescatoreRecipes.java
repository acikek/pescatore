package com.acikek.pescatore.datagen;

import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeCategory;

public class PescatoreRecipes extends FabricRecipeProvider {

    public PescatoreRecipes(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        generateRods(exporter);
        generateFilets(exporter);
    }

    public static ShapedRecipeJsonBuilder generateRodBuilder(ItemConvertible baseTool, ItemConvertible output) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, output)
                .pattern("  R")
                .pattern(" TR")
                .pattern("B R")
                .input('T', baseTool)
                .criterion(hasItem(baseTool), RecipeProvider.conditionsFromItem(baseTool));
    }

    public void generateRods(RecipeExporter exporter) {
        generateRodBuilder(Items.FISHING_ROD, PescatoreItems.ROOKIE_ROD)
                .input('R', ConventionalItemTags.COPPER_INGOTS)
                .input('B', ConventionalItemTags.IRON_INGOTS)
                .offerTo(exporter);
        generateRodBuilder(PescatoreItems.ROOKIE_ROD, PescatoreItems.ADEPT_ROD)
                .input('R', ConventionalItemTags.GOLD_INGOTS)
                .input('B', Items.NAUTILUS_SHELL)
                .offerTo(exporter);
        generateRodBuilder(PescatoreItems.ADEPT_ROD, PescatoreItems.EXPERT_ROD)
                .input('R', ConventionalItemTags.DIAMONDS)
                .input('B', Items.HEART_OF_THE_SEA)
                .offerTo(exporter);
        generateRodBuilder(PescatoreItems.EXPERT_ROD, PescatoreItems.AETHER_ROD)
                .input('R', ConventionalItemTags.NETHERITE_INGOTS)
                .input('B', Items.NETHER_STAR)
                .offerTo(exporter);
    }

    public void generateFilet(RecipeExporter exporter, ItemConvertible filet, ItemConvertible cooked, MinigameFishRarity rarity) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, filet)
                .input(rarity.tag)
                .criterion("has_fish", RecipeProvider.conditionsFromTag(rarity.tag))
                .offerTo(exporter);
        float exp = 0.45f * ((rarity.ordinal() / 2.0f) + 1.0f);
        offerFoodCookingRecipe(exporter, "furnace", RecipeSerializer.SMELTING, SmeltingRecipe::new, 200, filet, cooked, exp);
        offerFoodCookingRecipe(exporter, "campfire", RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, 600, filet, cooked, exp);
        offerFoodCookingRecipe(exporter, "smoker", RecipeSerializer.SMOKING, SmokingRecipe::new, 100, filet, cooked, exp);
    }

    public void generateFilets(RecipeExporter exporter) {
        generateFilet(exporter, PescatoreItems.COMMON_FISH_FILET, PescatoreItems.COOKED_COMMON_FISH_FILET, MinigameFishRarity.COMMON);
        generateFilet(exporter, PescatoreItems.UNCOMMON_FISH_FILET, PescatoreItems.COOKED_UNCOMMON_FISH_FILET, MinigameFishRarity.UNCOMMON);
        generateFilet(exporter, PescatoreItems.RARE_FISH_FILET, PescatoreItems.COOKED_RARE_FISH_FILET, MinigameFishRarity.RARE);
        generateFilet(exporter, PescatoreItems.VERY_RARE_FISH_FILET, PescatoreItems.COOKED_VERY_RARE_FISH_FILET, MinigameFishRarity.VERY_RARE);
    }
}
