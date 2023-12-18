package com.acikek.pescatore.item;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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

    public static final List<Item> ITEMS = new ArrayList<>();

    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Pescatore.id("main"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .displayName(Text.literal("Pescatore"))
            .icon(PescatoreItems.ROOKIE_ROD::getDefaultStack) // TODO: Fish as icon. TODO: Random fish?
            .entries((displayContext, entries) -> PescatoreItems.ITEMS.forEach(entries::add))
            .build();

    public static void registerItem(String name, Item item) {
        Registry.register(Registries.ITEM, Pescatore.id(name), item);
        ITEMS.add(item);
    }

    public static void registerItems() {
        registerItem("rookie_rod", ROOKIE_ROD);
        registerItem("adept_rod", ADEPT_ROD);
        registerItem("expert_rod", EXPERT_ROD);
    }

    public static void registerItemGroup() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY.getValue(), ITEM_GROUP);
        /*ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(entries ->
                ITEMS.forEach(entries::add)
        );*/
    }
}
