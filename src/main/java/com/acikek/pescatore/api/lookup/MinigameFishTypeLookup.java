package com.acikek.pescatore.api.lookup;

import com.acikek.pescatore.api.impl.lookup.MinigameFishTypeLookupImpl;
import com.acikek.pescatore.api.properties.MinigameBehavior;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import com.acikek.pescatore.api.type.MinigameFishType;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface MinigameFishTypeLookup {

    static MinigameFishTypeLookup create() {
        return new MinigameFishTypeLookupImpl();
    }

    MinigameFishTypeLookup bySize(Predicate<MinigameFishSize> predicate);

    default MinigameFishTypeLookup byRarity(MinigameFishSize size) {
        return bySize(Predicate.isEqual(size));
    }

    MinigameFishTypeLookup byRarity(Predicate<MinigameFishRarity> predicate);

    default MinigameFishTypeLookup byRarity(MinigameFishRarity rarity) {
        return byRarity(Predicate.isEqual(rarity));
    }

    default MinigameFishTypeLookup rollRarity(float value) {
        return byRarity(MinigameFishRarity.roll(value));
    }

    default MinigameFishTypeLookup rollRarity(Random random) {
        return rollRarity(random.nextFloat());
    }

    MinigameFishTypeLookup byDifficulty(Predicate<MinigameBehavior> predicate);

    default MinigameFishTypeLookup byDifficulty(MinigameBehavior difficulty) {
        return byDifficulty(Predicate.isEqual(difficulty));
    }

    List<MinigameFishType> lookup();

    Optional<MinigameFishType> random(Random random);
}
