package com.acikek.pescatore.datagen;

import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class PescatoreRecipes extends FabricRecipeProvider {

    public PescatoreRecipes(FabricDataOutput output) {
        super(output);
    }

    public static void generateRodRecipe(RecipeExporter exporter, ItemConvertible baseTool, ItemConvertible rodUpgrade, ItemConvertible baseUpgrade, ItemConvertible output) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, output)
                .pattern("  R")
                .pattern(" TR")
                .pattern("BTR")
                .input('T', baseTool)
                .input('R', rodUpgrade)
                .input('B', baseUpgrade)
                .criterion("has_base_tool", RecipeProvider.conditionsFromItem(baseTool))
                .offerTo(exporter);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        generateRodRecipe(exporter, Items.FISHING_ROD, Items.COPPER_INGOT, Items.IRON_INGOT, PescatoreItems.ROOKIE_ROD);
        generateRodRecipe(exporter, PescatoreItems.ROOKIE_ROD, Items.GOLD_INGOT, Items.NAUTILUS_SHELL, PescatoreItems.ADEPT_ROD);
        generateRodRecipe(exporter, PescatoreItems.ADEPT_ROD, Items.DIAMOND, Items.HEART_OF_THE_SEA, PescatoreItems.EXPERT_ROD);
    }
}
