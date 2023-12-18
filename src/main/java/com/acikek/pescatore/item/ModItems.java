package com.acikek.pescatore.item;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, Pescatore.id("main"));

    public static final MinigameFishingRodItem ROOKIE_ROD = MinigameRodTier.ROOKIE.createRod();
    public static final MinigameFishingRodItem ADEPT_ROD = MinigameRodTier.ADEPT.createRod();
    public static final MinigameFishingRodItem EXPERT_ROD = MinigameRodTier.EXPERT.createRod();

    public static final List<Item> ITEMS = new ArrayList<>();

    public static void register(String name, Item item) {
        Registry.register(Registries.ITEM, Pescatore.id(name), item);
        ITEMS.add(item);
    }

    public static void register() {
        register("rookie_rod", ROOKIE_ROD);
        register("adept_rod", ADEPT_ROD);
        register("expert_rod", EXPERT_ROD);
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(entries ->
            ITEMS.forEach(entries::add)
        );
    }
}
