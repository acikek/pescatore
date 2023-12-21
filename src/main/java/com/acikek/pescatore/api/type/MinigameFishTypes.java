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

    public static final MinigameFishType CRUCIAN_CARP = new MinigameFishType(
            MinigameFishSize.REGULAR, MinigameBehavior.DIFFICULTY_2, MinigameFishRarity.COMMON, null
    );

    public static final MinigameFishType RED_SNAPPER = new MinigameFishType(
            MinigameFishSize.FATTY, MinigameBehavior.DIFFICULTY_5, MinigameFishRarity.UNCOMMON, null
    );

    public static final MinigameFishType COELACANTH = new MinigameFishType(
            MinigameFishSize.HAWG, MinigameBehavior.DIFFICULTY_7, MinigameFishRarity.VERY_RARE, null
    );

    public static final MinigameFishType ARAPAIMA = new MinigameFishType(
            MinigameFishSize.GARGANTUAN, MinigameBehavior.DIFFICULTY_9, MinigameFishRarity.RARE, null
    );

    @ApiStatus.Internal
    public static void register() {
        MinigameFishType.EMPTY.register(MinigameFishType.EMPTY_ID);
        GOLDFISH.register("goldfish");
        CRUCIAN_CARP.register("crucian_carp");
        RED_SNAPPER.register("red_snapper");
        COELACANTH.register("coelacanth");
        ARAPAIMA.register("arapaima");
    }
}
