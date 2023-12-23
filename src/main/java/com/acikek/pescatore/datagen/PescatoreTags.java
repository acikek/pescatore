package com.acikek.pescatore.datagen;

import com.acikek.pescatore.api.PescatoreAPI;
import com.acikek.pescatore.api.type.MinigameFishType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class PescatoreTags extends FabricTagProvider.ItemTagProvider {

    public PescatoreTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        for (MinigameFishType type : PescatoreAPI.getFishTypes()) {
            getOrCreateTagBuilder(type.rarity().tag).add(type.item().asItem());
        }
    }
}
