package com.acikek.pescatore.item;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.item.armor.ArapaimaLeggings;
import com.acikek.pescatore.item.armor.CoelacanthChestplate;
import com.acikek.pescatore.item.rod.MinigameFishingRodItem;
import com.acikek.pescatore.item.rod.MinigameRodTier;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PescatoreItems {

    public static final MinigameFishingRodItem ROOKIE_ROD = MinigameRodTier.ROOKIE.createRod(defaultSettings().maxDamage(64).rarity(Rarity.COMMON));
    public static final MinigameFishingRodItem ADEPT_ROD = MinigameRodTier.ADEPT.createRod(defaultSettings().maxDamage(128).rarity(Rarity.UNCOMMON));
    public static final MinigameFishingRodItem EXPERT_ROD = MinigameRodTier.EXPERT.createRod(defaultSettings().maxDamage(512).rarity(Rarity.RARE));
    public static final MinigameFishingRodItem AETHER_ROD = MinigameRodTier.AETHER.createRod(defaultSettings().maxDamage(1024).rarity(Rarity.EPIC));

    public static final Item GOLDFISH = new Item(fishSettings());
    public static final Item SARDINE = new Item(fishSettings());
    public static final Item CRUCIAN_CARP = new Item(fishSettings());
    public static final Item OLIVE_FLOUNDER = new Item(fishSettings());
    public static final Item CARP = new Item(fishSettings());
    public static final Item RAINBOW_TROUT = new Item(fishSettings());
    public static final Item RED_SNAPPER = new Item(fishSettings());
    public static final Item BULLHEAD = new Item(fishSettings());
    public static final Item SEA_BASS = new Item(fishSettings());
    public static final Item TUNA = new Item(fishSettings());
    public static final Item COELACANTH = new Item(fishSettings());
    public static final Item PIRANHA = new Item(fishSettings());
    public static final Item STURGEON = new Item(fishSettings());
    public static final Item ARAPAIMA = new Item(fishSettings());
    public static final Item OCTOPUS = new Item(fishSettings());
    public static final Item THE_CUBE = new Item(fishSettings());

    public static final Item GOLDFISH_CRACKER = new Item(defaultSettings().food(
            new FoodComponent.Builder()
                    .hunger(1)
                    .saturationModifier(1.0f)
                    .snack()
                    .build()
    ));

    public static final Item TUNA_SANDWICH = new Item(defaultSettings().food(
            new FoodComponent.Builder()
                    .hunger(6)
                    .saturationModifier(16.0f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 900, 0), 1.0f)
                    .build()
    ));

    public static final Item EMPTY_SARDINE_CAN = new Item(defaultSettings());
    public static final Item SARDINE_CAN = new SardineCanItem(foodSettings(3, 2.0f).maxDamage(6));
    public static final Item OLIVE_FLOUNDER_PLATE = new Item(foodSettings(2, 0.0f));
    public static final Item COOKED_OLIVE_FLOUNDER_PLATE = new Item(foodSettings(8, 8.0f));
    public static final Item CARP_PLATE = new Item(foodSettings(1, 0.0f));
    public static final Item COOKED_CARP_PLATE = new Item(foodSettings(3, 7.0f));
    public static final Item RAINBOW_TROUT_PLATE = new Item(foodSettings(2, 0.0f));
    public static final Item COOKED_RAINBOW_TROUT_PLATE = new Item(foodSettings(9, 4.0f));
    public static final Item OCTOPUS_TENTACLE = new Item(foodSettings(1, 0.0f));
    public static final Item COOKED_OCTOPUS_TENTACLE = new Item(foodSettings(3, 6.0f));

    public static final Item COMMON_FISH_FILLET = new Item(foodSettings(2, 1.0f));
    public static final Item COOKED_COMMON_FISH_FILLET = new Item(foodSettings(5, 4.0f));
    public static final Item UNCOMMON_FISH_FILLET = new Item(foodSettings(3, 3.0f));
    public static final Item COOKED_UNCOMMON_FISH_FILLET = new Item(foodSettings(7, 6.0f));
    public static final Item RARE_FISH_FILLET = new Item(foodSettings(4, 4.0f));
    public static final Item COOKED_RARE_FISH_FILLET = new Item(foodSettings(8, 8.0f));
    public static final Item VERY_RARE_FISH_FILLET = new Item(foodSettings(5, 6.0f));
    public static final Item COOKED_VERY_RARE_FISH_FILLET = new Item(foodSettings(10, 10.0f));

    public static final Item COELACANTH_CHESTPLATE = new CoelacanthChestplate(defaultSettings());
    public static final Item PIRANHA_TOOTH_NECKLACE = new PiranhaNecklaceItem(defaultSettings());
    public static final Item ARAPAIMA_LEGGINGS = new ArapaimaLeggings(defaultSettings());

    public static final List<Item> ITEMS = new ArrayList<>();
    public static final List<Item> RODS = new ArrayList<>();
    public static final List<Item> FISH = new ArrayList<>();
    public static final List<Item> FOOD = new ArrayList<>();

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Pescatore.id("main"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.literal("Pescatore"))
            .icon(PescatoreItems.ROOKIE_ROD::getDefaultStack)
            .entries((displayContext, entries) -> PescatoreItems.ITEMS.forEach(entries::add))
            .build();


    public static Item.Settings defaultSettings() {
        return new FabricItemSettings();
    }

    public static Item.Settings foodSettings(int hunger, float saturation) {
        return defaultSettings().food(
                new FoodComponent.Builder()
                        .hunger(hunger)
                        .saturationModifier(saturation)
                        .meat()
                        .build()
        );
    }

    public static Item.Settings fishSettings() {
        return foodSettings(1, 0);
    }

    public static void registerItem(String name, Item item) {
        Registry.register(Registries.ITEM, Pescatore.id(name), item);
        ITEMS.add(item);
    }

    public static void registerRod(String name, Item item) {
        registerItem(name, item);
        RODS.add(item);
    }

    public static void registerFish(String name, Item item) {
        registerItem(name, item);
        FISH.add(item);
    }

    public static void registerFood(String name, Item item) {
        registerItem(name, item);
        FOOD.add(item);
    }

    public static void registerItems() {
        registerRod("rookie_rod", ROOKIE_ROD);
        registerRod("adept_rod", ADEPT_ROD);
        registerRod("expert_rod", EXPERT_ROD);
        registerRod("aether_rod", AETHER_ROD);
        registerFish("goldfish", GOLDFISH);
        registerFish("sardine", SARDINE);
        registerFish("crucian_carp", CRUCIAN_CARP);
        registerFish("olive_flounder", OLIVE_FLOUNDER);
        registerFish("carp", CARP);
        registerFish("rainbow_trout", RAINBOW_TROUT);
        registerFish("red_snapper", RED_SNAPPER);
        registerFish("bullhead", BULLHEAD);
        registerFish("sea_bass", SEA_BASS);
        registerFish("tuna", TUNA);
        registerFish("coelacanth", COELACANTH);
        registerFish("piranha", PIRANHA);
        registerFish("sturgeon", STURGEON);
        registerFish("arapaima", ARAPAIMA);
        registerFish("octopus", OCTOPUS);
        registerFish("the_cube", THE_CUBE);
        registerFood("goldfish_cracker", GOLDFISH_CRACKER);
        registerFood("empty_sardine_can", EMPTY_SARDINE_CAN);
        registerFood("sardine_can", SARDINE_CAN);
        registerFood("olive_flounder_plate", OLIVE_FLOUNDER_PLATE);
        registerFood("cooked_olive_flounder_plate", COOKED_OLIVE_FLOUNDER_PLATE);
        registerFood("carp_plate", CARP_PLATE);
        registerFood("cooked_carp_plate", COOKED_CARP_PLATE);
        registerFood("rainbow_trout_plate", RAINBOW_TROUT_PLATE);
        registerFood("cooked_rainbow_trout_plate", COOKED_RAINBOW_TROUT_PLATE);
        registerFood("tuna_sandwich", TUNA_SANDWICH);
        registerFood("octopus_tentacle", OCTOPUS_TENTACLE);
        registerFood("cooked_octopus_tentacle", COOKED_OCTOPUS_TENTACLE);
        registerFood("common_fish_fillet", COMMON_FISH_FILLET);
        registerFood("cooked_common_fish_fillet", COOKED_COMMON_FISH_FILLET);
        registerFood("uncommon_fish_fillet", UNCOMMON_FISH_FILLET);
        registerFood("cooked_uncommon_fish_fillet", COOKED_UNCOMMON_FISH_FILLET);
        registerFood("rare_fish_fillet", RARE_FISH_FILLET);
        registerFood("cooked_rare_fish_fillet", COOKED_RARE_FISH_FILLET);
        registerFood("very_rare_fish_fillet", VERY_RARE_FISH_FILLET);
        registerFood("cooked_very_rare_fish_fillet", COOKED_VERY_RARE_FISH_FILLET);
        registerItem("coelacanth_chestplate", COELACANTH_CHESTPLATE);
        registerItem("piranha_tooth_necklace", PIRANHA_TOOTH_NECKLACE);
        registerItem("arapaima_leggings", ARAPAIMA_LEGGINGS);
    }

    public static void registerItemGroupEntries() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            var stacks = RODS.stream().map(Item::getDefaultStack).toList();
            entries.addAfter(Items.FISHING_ROD, stacks);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            List<Item> items = new ArrayList<>(FISH);
            items.addAll(FOOD);
            var stacks = items.stream().map(Item::getDefaultStack).toList();
            entries.addAfter(Items.PUFFERFISH, stacks);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            var stacks = Stream.of(COELACANTH_CHESTPLATE, PIRANHA_TOOTH_NECKLACE, ARAPAIMA_LEGGINGS)
                    .map(Item::getDefaultStack).toList();
            entries.addAfter(Items.TURTLE_HELMET, stacks);
        });
    }

    public static void registerItemGroup() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY.getValue(), ITEM_GROUP);
    }

    public static void register() {
        registerItems();
        registerItemGroupEntries();
        registerItemGroup();
    }
}
