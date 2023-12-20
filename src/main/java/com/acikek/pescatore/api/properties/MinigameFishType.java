package com.acikek.pescatore.api.properties;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a type of fish that can be caught in the Pescatore minigame.
 * @param size the size properties of the fish
 * @param difficulty the difficulty properties of the fish
 * @param rarity the rarity of the fish
 * @param item the item to reel in once the fish is caught
 */
// TODO: Document
// TODO: Move to MinigameFishTypes?
public record MinigameFishType(MinigameFishSize size, MinigameProperties difficulty, MinigameFishRarity rarity, ItemConvertible item) {

    public static final Registry<MinigameFishType> REGISTRY =
            FabricRegistryBuilder.<MinigameFishType>createSimple(RegistryKey.ofRegistry(Pescatore.id("minigame_fish_type"))).buildAndRegister();

    public static final Identifier EMPTY_ID = Pescatore.id("empty");

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

    public void register(Identifier id) {
        Registry.register(REGISTRY, id, this);
    }

    private void register(String name) {
        register(Pescatore.id(name));
    }

    @ApiStatus.Internal
    public static void register() {
        EMPTY.register(EMPTY_ID);
        GOLDFISH.register("goldfish");
        CRUCIAN_CARP.register("crucian_carp");
        RED_SNAPPER.register("red_snapper");
        COELACANTH.register("coelacanth");
        ARAPAIMA.register("arapaima");
    }
}
