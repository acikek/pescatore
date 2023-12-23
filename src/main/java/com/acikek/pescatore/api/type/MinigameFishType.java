package com.acikek.pescatore.api.type;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.properties.MinigameBehavior;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

/**
 * Represents a typeKey of fish that can be caught in the Pescatore minigame.
 * @param size the size properties of the fish
 * @param difficulty the difficulty properties of the fish
 * @param rarity the rarity of the fish
 * @param item the item to reel in once the fish is caught
 */
// TODO: Document
public record MinigameFishType(MinigameFishSize size, MinigameBehavior difficulty, MinigameFishRarity rarity, ItemConvertible item) {

    public static final RegistryKey<Registry<MinigameFishType>> REGISTRY_KEY = RegistryKey.ofRegistry(Pescatore.id("minigame_fish_type"));
    public static final Registry<MinigameFishType> REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
    public static final Codec<MinigameFishType> CODEC = Identifier.CODEC.xmap(REGISTRY::get, MinigameFishType::getId);

    public static final Identifier EMPTY_ID = Pescatore.id("empty");

    public static final MinigameFishType EMPTY = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_1, MinigameFishRarity.COMMON, null
    );

    public Identifier getId() {
        return REGISTRY.getId(this);
    }

    public Stat<Identifier> getStat() {
        return Stats.CUSTOM.getOrCreateStat(getId());
    }

    public void register(Identifier id) {
        Registry.register(REGISTRY, id, this);
        Registry.register(Registries.CUSTOM_STAT, id, id);
        getStat();
    }

    void register(String name) {
        register(Pescatore.id(name));
    }
}
