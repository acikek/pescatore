package com.acikek.pescatore.item;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PescatoreItems {

    public static final MinigameFishingRodItem ROOKIE_ROD = MinigameRodTier.ROOKIE.createRod();
    public static final MinigameFishingRodItem ADEPT_ROD = MinigameRodTier.ADEPT.createRod();
    public static final MinigameFishingRodItem EXPERT_ROD = MinigameRodTier.EXPERT.createRod();

    public static final Item GOLDFISH = new Item(defaultSettings());
    public static final Item SARDINE = new Item(defaultSettings());
    public static final Item CRUCIAN_CARP = new Item(defaultSettings());
    public static final Item OLIVE_FLOUNDER = new Item(defaultSettings());
    public static final Item CARP = new Item(defaultSettings());
    public static final Item RAINBOW_TROUT = new Item(defaultSettings());
    public static final Item RED_SNAPPER = new Item(defaultSettings());
    public static final Item BULLHEAD = new Item(defaultSettings());
    public static final Item SEA_BASS = new Item(defaultSettings());
    public static final Item TUNA = new Item(defaultSettings());
    public static final Item COELACANTH = new Item(defaultSettings());
    public static final Item PIRANHA = new Item(defaultSettings());
    public static final Item STURGEON = new Item(defaultSettings());
    public static final Item ARAPAIMA = new Item(defaultSettings());
    public static final Item OCTOPUS = new Item(defaultSettings());
    public static final Item THE_CUBE = new Item(defaultSettings());

    public static final List<Item> ITEMS = new ArrayList<>();
    public static final List<Item> RODS = new ArrayList<>();
    public static final List<Item> FISH = new ArrayList<>();

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Pescatore.id("main"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.literal("Pescatore"))
            .icon(PescatoreItems.ROOKIE_ROD::getDefaultStack) // TODO: Fish as icon. TODO: Random fish?
            .entries((displayContext, entries) -> PescatoreItems.ITEMS.forEach(entries::add))
            .build();


    public static Item.Settings defaultSettings() {
        return new FabricItemSettings();
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

    public static void registerItems() {
        registerRod("rookie_rod", ROOKIE_ROD);
        registerRod("adept_rod", ADEPT_ROD);
        registerRod("expert_rod", EXPERT_ROD);
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
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            var stacks = RODS.stream().map(Item::getDefaultStack).toList();
            entries.addAfter(Items.FISHING_ROD, stacks);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            var stacks = FISH.stream().map(Item::getDefaultStack).toList();
            entries.addAfter(Items.PUFFERFISH, stacks);
        });
    }

    public static void registerItemGroup() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY.getValue(), ITEM_GROUP);
        /*ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(entries ->
                ITEMS.forEach(entries::add)
        );*/
    }
}
