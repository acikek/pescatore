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

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PescatoreRecipes extends FabricRecipeProvider {

    public PescatoreRecipes(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        generateRods(exporter);
        generateMisc(exporter);
        generateCookedFoods(exporter);
        generateFillets(exporter);
        generateEquipment(exporter);
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

    public void generateCooking(RecipeExporter exporter, ItemConvertible raw, ItemConvertible cooked, float xp) {
        offerFoodCookingRecipe(exporter, "furnace", RecipeSerializer.SMELTING, SmeltingRecipe::new, 200, raw, cooked, xp);
        offerFoodCookingRecipe(exporter, "campfire", RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new, 600, raw, cooked, xp);
        offerFoodCookingRecipe(exporter, "smoker", RecipeSerializer.SMOKING, SmokingRecipe::new, 100, raw, cooked, xp);
    }

    public void generateFillet(RecipeExporter exporter, ItemConvertible fillet, ItemConvertible cooked, MinigameFishRarity rarity) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, fillet)
                .input(rarity.tag)
                .criterion("has_fish", RecipeProvider.conditionsFromTag(rarity.tag))
                .offerTo(exporter);
        BigDecimal xp = BigDecimal.valueOf(0.45f * ((rarity.ordinal() / 2.0f) + 1.0f))
                .setScale(3, RoundingMode.HALF_UP);
        generateCooking(exporter, fillet, cooked, xp.floatValue());
    }

    public void generateFillets(RecipeExporter exporter) {
        generateFillet(exporter, PescatoreItems.COMMON_FISH_FILLET, PescatoreItems.COOKED_COMMON_FISH_FILLET, MinigameFishRarity.COMMON);
        generateFillet(exporter, PescatoreItems.UNCOMMON_FISH_FILLET, PescatoreItems.COOKED_UNCOMMON_FISH_FILLET, MinigameFishRarity.UNCOMMON);
        generateFillet(exporter, PescatoreItems.RARE_FISH_FILLET, PescatoreItems.COOKED_RARE_FISH_FILLET, MinigameFishRarity.RARE);
        generateFillet(exporter, PescatoreItems.VERY_RARE_FISH_FILLET, PescatoreItems.COOKED_VERY_RARE_FISH_FILLET, MinigameFishRarity.VERY_RARE);
    }

    public void generateMisc(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.GOLDFISH_CRACKER, 16)
                .pattern("B")
                .pattern("G")
                .pattern("B")
                .input('B', Items.BREAD)
                .input('G', PescatoreItems.GOLDFISH)
                .criterion(hasItem(PescatoreItems.GOLDFISH), RecipeProvider.conditionsFromItem(PescatoreItems.GOLDFISH))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.TUNA_SANDWICH)
                .pattern("BT")
                .pattern("EB")
                .input('B', Items.BREAD)
                .input('T', PescatoreItems.TUNA)
                .input('E', Items.EGG)
                .criterion(hasItem(PescatoreItems.TUNA), RecipeProvider.conditionsFromItem(PescatoreItems.TUNA))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.EMPTY_SARDINE_CAN)
                .pattern("N N")
                .pattern("NIN")
                .input('N', Items.IRON_NUGGET)
                .input('I', ConventionalItemTags.IRON_INGOTS)
                .criterion("has_iron_ingot", RecipeProvider.conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.SARDINE_CAN)
                .pattern("SS")
                .pattern("SC")
                .input('S', PescatoreItems.SARDINE)
                .input('C', PescatoreItems.EMPTY_SARDINE_CAN)
                .criterion(hasItem(PescatoreItems.SARDINE), RecipeProvider.conditionsFromItem(PescatoreItems.SARDINE))
                .offerTo(exporter);
    }

    public void generateCookedFoods(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.OLIVE_FLOUNDER_PLATE)
                .pattern("FL")
                .pattern("GG")
                .input('F', PescatoreItems.OLIVE_FLOUNDER)
                .input('L', ConventionalItemTags.YELLOW_DYES)
                .input('G', Items.SEAGRASS)
                .criterion(hasItem(PescatoreItems.OLIVE_FLOUNDER), RecipeProvider.conditionsFromItem(PescatoreItems.OLIVE_FLOUNDER))
                .offerTo(exporter);
        generateCooking(exporter, PescatoreItems.OLIVE_FLOUNDER_PLATE, PescatoreItems.COOKED_OLIVE_FLOUNDER_PLATE, 0.45f);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.CARP_PLATE)
                .pattern("CL")
                .pattern("GP")
                .input('C', PescatoreItems.CARP)
                .input('L', ConventionalItemTags.YELLOW_DYES)
                .input('G', Items.SEAGRASS)
                .input('P', Items.BAKED_POTATO)
                .criterion(hasItem(PescatoreItems.CARP), RecipeProvider.conditionsFromItem(PescatoreItems.CARP))
                .offerTo(exporter);
        generateCooking(exporter, PescatoreItems.CARP_PLATE, PescatoreItems.COOKED_CARP_PLATE, 0.45f);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.RAINBOW_TROUT_PLATE)
                .pattern("TL")
                .pattern("LG")
                .input('T', PescatoreItems.RAINBOW_TROUT)
                .input('L', ConventionalItemTags.YELLOW_DYES)
                .input('G', Items.SEAGRASS)
                .criterion(hasItem(PescatoreItems.RAINBOW_TROUT), RecipeProvider.conditionsFromItem(PescatoreItems.RAINBOW_TROUT))
                .offerTo(exporter);
        generateCooking(exporter, PescatoreItems.RAINBOW_TROUT_PLATE, PescatoreItems.COOKED_RAINBOW_TROUT_PLATE, 0.45f);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, PescatoreItems.OCTOPUS_TENTACLE, 8)
                .input(PescatoreItems.OCTOPUS)
                .criterion(hasItem(PescatoreItems.OCTOPUS), RecipeProvider.conditionsFromItem(PescatoreItems.OCTOPUS))
                .offerTo(exporter);
        generateCooking(exporter, PescatoreItems.OCTOPUS_TENTACLE, PescatoreItems.COOKED_OCTOPUS_TENTACLE, 0.45f);
    }

    public void generateEquipment(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PescatoreItems.COELACANTH_CHESTPLATE)
                .pattern("I I")
                .pattern("CBC")
                .pattern("III")
                .input('I', ConventionalItemTags.IRON_INGOTS)
                .input('C', PescatoreItems.COELACANTH)
                .input('B', Items.IRON_BLOCK)
                .criterion("has_iron", RecipeProvider.conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
                .criterion(hasItem(PescatoreItems.COELACANTH), RecipeProvider.conditionsFromItem(PescatoreItems.COELACANTH))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PescatoreItems.PIRANHA_TOOTH_NECKLACE)
                .pattern("SSS")
                .pattern("S S")
                .pattern(" P ")
                .input('S', Items.STRING)
                .input('P', PescatoreItems.PIRANHA)
                .criterion(hasItem(PescatoreItems.PIRANHA), RecipeProvider.conditionsFromItem(PescatoreItems.PIRANHA))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, PescatoreItems.ARAPAIMA_LEGGINGS)
                .pattern("LAL")
                .pattern("L L")
                .pattern("A A")
                .input('L', Items.LEATHER)
                .input('A', PescatoreItems.ARAPAIMA)
                .criterion(hasItem(PescatoreItems.ARAPAIMA), RecipeProvider.conditionsFromItem(PescatoreItems.ARAPAIMA))
                .offerTo(exporter);
    }
}
