package com.acikek.pescatore.api.type;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.properties.MinigameBehavior;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

/**
 * Represents a type of fish that can be caught in the Pescatore minigame.
 * @param size the size properties of the fish
 * @param difficulty the difficulty properties of the fish
 * @param rarity the rarity of the fish
 * @param item the item to reel in once the fish is caught
 */
// TODO: Document
public record MinigameFishType(MinigameFishSize size, MinigameBehavior difficulty, MinigameFishRarity rarity, ItemConvertible item) {

    public static final Registry<MinigameFishType> REGISTRY =
            FabricRegistryBuilder.<MinigameFishType>createSimple(RegistryKey.ofRegistry(Pescatore.id("minigame_fish_type"))).buildAndRegister();

    public static final Identifier EMPTY_ID = Pescatore.id("empty");

    public static final MinigameFishType EMPTY = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_1, MinigameFishRarity.COMMON, null
    );

    public void register(Identifier id) {
        Registry.register(REGISTRY, id, this);
    }

    void register(String name) {
        register(Pescatore.id(name));
    }
}
