package com.acikek.pescatore.util;

import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import com.acikek.pescatore.entity.fish.MinigameFishEntity;

public interface FishMinigamePlayer {

    MinigameFishingBobberEntity pescatore$getHook();

    void pescatore$setHook(MinigameFishingBobberEntity entity);

    MinigameFishEntity pescatore$getFish();

    void pescatore$setFish(MinigameFishEntity entity);
}
