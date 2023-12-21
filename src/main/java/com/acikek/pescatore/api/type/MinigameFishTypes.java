package com.acikek.pescatore.api.type;

import com.acikek.pescatore.api.properties.MinigameBehavior;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import org.jetbrains.annotations.ApiStatus;

// TODO: Document
public class MinigameFishTypes {

    public static final MinigameFishType GOLDFISH = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_1, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType SARDINE = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_2, MinigameFishRarity.COMMON, null
    );

    public static final MinigameFishType CRUCIAN_CARP = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_2, MinigameFishRarity.COMMON, null
    );

    public static final MinigameFishType FLOUNDER = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_3, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType CARP = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameBehavior.DIFFICULTY_3, MinigameFishRarity.COMMON, null
    );

    public static final MinigameFishType RAINBOW_TROUT = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_4, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType RED_SNAPPER = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameBehavior.DIFFICULTY_5, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType CATFISH = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_5, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType SEA_BASS = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameBehavior.DIFFICULTY_6, MinigameFishRarity.COMMON, null
    );

    public static final MinigameFishType TUNA = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_7, MinigameFishRarity.RARE, null
    );

    public static final MinigameFishType COELACANTH = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_7, MinigameFishRarity.VERY_RARE, null
    );

    public static final MinigameFishType PIRANHA = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_8, MinigameFishRarity.RARE, null
    );

    public static final MinigameFishType STURGEON = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_9, MinigameFishRarity.RARE, null
    );

    public static final MinigameFishType ARAPAIMA = new MinigameFishType(
            MinigameFishSize.GARGANTUAN, MinigameBehavior.DIFFICULTY_9, MinigameFishRarity.RARE, null
    );

    public static final MinigameFishType OCTOPUS = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_10, MinigameFishRarity.RARE, null
    );

    public static final MinigameFishType THE_CUBE = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_10, MinigameFishRarity.VERY_RARE, null
    );

    @ApiStatus.Internal
    public static void register() {
        MinigameFishType.EMPTY.register(MinigameFishType.EMPTY_ID);
        GOLDFISH.register("goldfish");
        SARDINE.register("sardine");
        CRUCIAN_CARP.register("crucian_carp");
        FLOUNDER.register("flounder");
        CARP.register("carp");
        RAINBOW_TROUT.register("rainbow_trout");
        RED_SNAPPER.register("red_snapper");
        CATFISH.register("catfish");
        SEA_BASS.register("sea_bass");
        TUNA.register("tuna");
        COELACANTH.register("coelacanth");
        PIRANHA.register("piranha");
        STURGEON.register("sturgeon");
        ARAPAIMA.register("arapaima");
        OCTOPUS.register("octopus");
        THE_CUBE.register("the_cube");
    }
}
