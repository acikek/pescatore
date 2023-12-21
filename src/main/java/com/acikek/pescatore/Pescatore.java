package com.acikek.pescatore;

import com.acikek.pescatore.api.type.MinigameFishTypes;
import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import com.acikek.pescatore.entity.fish.MinigameFishEntity;
import com.acikek.pescatore.item.PescatoreItems;
import net.fabricmc.api.ModInitializer;
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
        MinigameFishTypes.register();
        MinigameFishEntity.register();
        MinigameFishingBobberEntity.register();
        PescatoreItems.registerItems();
        PescatoreItems.registerItemGroup();
    }
}
