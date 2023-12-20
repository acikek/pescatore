package com.acikek.pescatore.mixin;

import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import com.acikek.pescatore.util.FishMinigamePlayer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements FishMinigamePlayer {

    @Unique
    private MinigameFishingBobberEntity hook;


    @Override
    public MinigameFishingBobberEntity pescatore$getHook() {
        return hook;
    }

    @Override
    public void pescatore$setHook(MinigameFishingBobberEntity entity) {
        hook = entity;
    }
}
