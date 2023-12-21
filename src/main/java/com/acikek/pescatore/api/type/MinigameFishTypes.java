package com.acikek.pescatore.api.type;

import com.acikek.pescatore.api.properties.MinigameBehavior;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import com.acikek.pescatore.item.PescatoreItems;
import org.jetbrains.annotations.ApiStatus;

// TODO: Document
public class MinigameFishTypes {

    public static final MinigameFishType GOLDFISH = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_1, MinigameFishRarity.UNCOMMON, PescatoreItems.GOLDFISH
    );

    public static final MinigameFishType SARDINE = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_2, MinigameFishRarity.COMMON, PescatoreItems.SARDINE
    );

    public static final MinigameFishType CRUCIAN_CARP = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_2, MinigameFishRarity.COMMON, PescatoreItems.CRUCIAN_CARP
    );

    public static final MinigameFishType OLIVE_FLOUNDER = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_3, MinigameFishRarity.UNCOMMON, PescatoreItems.OLIVE_FLOUNDER
    );

    public static final MinigameFishType CARP = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameBehavior.DIFFICULTY_3, MinigameFishRarity.COMMON, PescatoreItems.CARP
    );

    public static final MinigameFishType RAINBOW_TROUT = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_4, MinigameFishRarity.UNCOMMON, PescatoreItems.RAINBOW_TROUT
    );

    public static final MinigameFishType RED_SNAPPER = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameBehavior.DIFFICULTY_5, MinigameFishRarity.UNCOMMON, PescatoreItems.RED_SNAPPER
    );

    public static final MinigameFishType BULLHEAD = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_5, MinigameFishRarity.UNCOMMON, PescatoreItems.BULLHEAD
    );

    public static final MinigameFishType SEA_BASS = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameBehavior.DIFFICULTY_6, MinigameFishRarity.COMMON, PescatoreItems.SEA_BASS
    );

    public static final MinigameFishType TUNA = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_7, MinigameFishRarity.RARE, PescatoreItems.TUNA
    );

    public static final MinigameFishType COELACANTH = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_7, MinigameFishRarity.VERY_RARE, PescatoreItems.COELACANTH
    );

    public static final MinigameFishType PIRANHA = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_8, MinigameFishRarity.RARE, PescatoreItems.PIRANHA
    );

    public static final MinigameFishType STURGEON = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_9, MinigameFishRarity.RARE, PescatoreItems.STURGEON
    );

    public static final MinigameFishType ARAPAIMA = new MinigameFishType(
            MinigameFishSize.GARGANTUAN, MinigameBehavior.DIFFICULTY_9, MinigameFishRarity.RARE, PescatoreItems.ARAPAIMA
    );

    public static final MinigameFishType OCTOPUS = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_10, MinigameFishRarity.RARE, PescatoreItems.OCTOPUS
    );

    public static final MinigameFishType THE_CUBE = new MinigameFishType(
            MinigameFishSize.DINKY, MinigameBehavior.DIFFICULTY_10, MinigameFishRarity.VERY_RARE, PescatoreItems.THE_CUBE
    );

    @ApiStatus.Internal
    public static void register() {
        MinigameFishType.EMPTY.register(MinigameFishType.EMPTY_ID);
        GOLDFISH.register("goldfish");
        SARDINE.register("sardine");
        CRUCIAN_CARP.register("crucian_carp");
        OLIVE_FLOUNDER.register("olive_flounder");
        CARP.register("carp");
        RAINBOW_TROUT.register("rainbow_trout");
        RED_SNAPPER.register("red_snapper");
        BULLHEAD.register("bullhead");
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
