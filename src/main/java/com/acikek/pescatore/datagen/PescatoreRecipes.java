package com.acikek.pescatore.datagen;

import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
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

    public static ShapedRecipeJsonBuilder generateRodBuilder(ItemConvertible baseTool, ItemConvertible output) {
        return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, output)
                .pattern("  R")
                .pattern(" TR")
                .pattern("BTR")
                .input('T', baseTool)
                .criterion("has_base_tool", RecipeProvider.conditionsFromItem(baseTool));
    }

    @Override
    public void generate(RecipeExporter exporter) {
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
    }
}
