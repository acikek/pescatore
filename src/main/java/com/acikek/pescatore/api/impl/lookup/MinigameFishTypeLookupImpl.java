package com.acikek.pescatore.api.impl.lookup;

import com.acikek.pescatore.api.lookup.MinigameFishTypeLookup;
import com.acikek.pescatore.api.properties.MinigameBehavior;
import com.acikek.pescatore.api.properties.MinigameFishRarity;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import com.acikek.pescatore.api.type.MinigameFishType;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MinigameFishTypeLookupImpl implements MinigameFishTypeLookup, AutoCloseable {

    private Stream<MinigameFishType> types = MinigameFishType.REGISTRY.stream();

    @Override
    public MinigameFishTypeLookup bySize(Predicate<MinigameFishSize> predicate) {
        types = types.filter(type -> predicate.test(type.size()));
        return this;
    }

    @Override
    public MinigameFishTypeLookup byRarity(Predicate<MinigameFishRarity> predicate) {
        types = types.filter(type -> predicate.test(type.rarity()));
        return this;
    }

    @Override
    public MinigameFishTypeLookup byDifficulty(Predicate<MinigameBehavior> predicate) {
        types = types.filter(type -> predicate.test(type.difficulty()));
        return this;
    }

    @Override
    public List<MinigameFishType> lookup() {
        return types.toList();
    }

    @Override
    public Optional<MinigameFishType> random(Random random) {
        var list = lookup();
        return list.isEmpty()
                ? Optional.empty()
                : Optional.of(list.get(random.nextInt(list.size())));
    }

    @Override
    public void close() {
        types.close();
    }
}
