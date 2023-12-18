package com.acikek.pescatore;

import com.acikek.pescatore.entity.MinigameFishEntity;
import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pescatore implements ModInitializer {

    public static final String ID = "pescatore";

    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static Identifier id(String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Pescatore...");
        MinigameFishEntity.register();
        MinigameFishingBobberEntity.register();
        PescatoreItems.registerItems();
        PescatoreItems.registerItemGroup();
    }
}
