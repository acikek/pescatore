package com.acikek.pescatore.api;

import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.type.MinigameFishType;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class PescatoreAPI {

    public static List<MinigameFishType> getTypesByRarity(MinigameFishRarity rarity) {
        return MinigameFishType.REGISTRY.stream()
                .filter(type -> type.rarity() == rarity)
                .toList();
    }

    public static List<MinigameFishType> rollTypes(float value) {
        return getTypesByRarity(MinigameFishRarity.roll(value));
    }

    public static MinigameFishType rollType(float value, Random random) {
        var types = rollTypes(value);
        return types.get(random.nextInt(types.size()));
    }

    public static MinigameFishType rollType(Random random) {
        return rollType(random.nextFloat(), random);
    }
}
