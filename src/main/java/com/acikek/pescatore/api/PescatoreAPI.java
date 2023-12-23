package com.acikek.pescatore.api;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.lookup.MinigameFishTypeLookup;
import com.acikek.pescatore.api.type.MinigameFishType;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;

public class PescatoreAPI {

    public static final Identifier TOTAL_FISH_CAUGHT = Pescatore.id("total_fish_caught");

    public static List<MinigameFishType> getFishTypes() {
        return MinigameFishTypeLookup.create().lookup();
    }

    public static Optional<MinigameFishType> rollType(float value, Random random) {
        return MinigameFishTypeLookup.create()
                .rollRarity(value)
                .random(random);
    }

    public static Optional<MinigameFishType> rollType(Random random) {
        return rollType(random.nextFloat(), random);
    }

    public static Stat<Identifier> getTotalCaughtStat() {
        return Stats.CUSTOM.getOrCreateStat(TOTAL_FISH_CAUGHT);
    }
}
