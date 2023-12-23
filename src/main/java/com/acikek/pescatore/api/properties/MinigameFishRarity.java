package com.acikek.pescatore.api.properties;

import com.acikek.pescatore.Pescatore;
import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;

/**
 * Represents the rarity of a minigame fish.
 */
public enum MinigameFishRarity implements StringIdentifiable {

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
     * A statistic for how many fish of this rarity a player has caught.
     */
    private final Identifier stat;

    /**
     * A tag for fish items within this rarity.
     */
    public final TagKey<Item> tag;

    /**
     * Constructs the rarity instance.
     * @param chance the chance value.
     */
    MinigameFishRarity(float chance) {
        this.chance = chance;
        stat = Pescatore.id(asString());
        tag = TagKey.of(RegistryKeys.ITEM, Pescatore.id(asString() + "_fish"));
    }

    /**
     * @return a statistic for how many fish of this rarity a player has caught
     */
    public Stat<Identifier> getStat() {
        return Stats.CUSTOM.getOrCreateStat(stat);
    }

    /**
     * A string-backed coded for a {@link MinigameFishRarity} instance.
     */
    public static final Codec<MinigameFishRarity> CODEC = StringIdentifiable.createCodec(MinigameFishRarity::values);

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

    @ApiStatus.Internal
    public static void registerStats() {
        for (MinigameFishRarity rarity : EnumUtils.getEnumList(MinigameFishRarity.class)) {
            Registry.register(Registries.CUSTOM_STAT, rarity.stat, rarity.stat);
            rarity.getStat();
        }
    }

    @Override
    public String asString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
