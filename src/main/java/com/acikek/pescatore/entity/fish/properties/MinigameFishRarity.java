package com.acikek.pescatore.entity.fish.properties;

import org.apache.commons.lang3.EnumUtils;

/**
 * Represents the rarity of a minigame fish.
 */
public enum MinigameFishRarity {

    /**
     * A common variety with a 65% chance of spawning.
     */
    COMMON(1.0f),
    /**
     * An uncommon variety with a 20% chance of spawning.
     */
    UNCOMMON(0.35f),
    /**
     * A rare variety with a 10% chance of spawning.
     */
    RARE(0.15f),
    /**
     * A very rare variety with a 5% chance of spawning.
     */
    VERY_RARE(0.05f);

    /**
     * The 'fallback' chance value to compare to when rolling.
     */
    private final float chance;

    /**
     * Constructs the rarity instance.
     * @param chance the chance value.
     */
    MinigameFishRarity(float chance) {
        this.chance = chance;
    }

    /**
     * @param random the random roll, from {@code 0.0f} to {@code 1.0f}.
     * @return a rarity instance based on the random roll
     */
    public static MinigameFishRarity roll(float random) {
        for (MinigameFishRarity rarity : EnumUtils.getEnumList(MinigameFishRarity.class)) {
            if (random < rarity.chance) {
                return rarity;
            }
        }
        return MinigameFishRarity.COMMON;
    }
}
