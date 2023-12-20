package com.acikek.pescatore.entity.fish.properties;

import net.minecraft.item.ItemConvertible;

/**
 * Represents a type of fish that can be caught in the Pescatore minigame.
 * @param size the size properties of the fish
 * @param difficulty the difficulty properties of the fish
 * @param rarity the rarity of the fish
 * @param item the item to reel in once the fish is caught
 */
public record MinigameFishType(MinigameFishSize size, MinigameProperties difficulty, MinigameFishRarity rarity, ItemConvertible item) {

    public static final MinigameFishType EMPTY = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameProperties.DIFFICULTY_1, MinigameFishRarity.COMMON, null
    );

    public static final MinigameFishType GOLDFISH = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameProperties.DIFFICULTY_1, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType CRUCIAN_CARP = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameProperties.DIFFICULTY_2, MinigameFishRarity.COMMON, null
    );

    public static final MinigameFishType RED_SNAPPER = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameProperties.DIFFICULTY_5, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType COELACANTH = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameProperties.DIFFICULTY_7, MinigameFishRarity.VERY_RARE, null
    );

    public static final MinigameFishType ARAPAIMA = new MinigameFishType(
            MinigameFishSize.GARGANTUAN, MinigameProperties.DIFFICULTY_9, MinigameFishRarity.RARE, null
    );
}
