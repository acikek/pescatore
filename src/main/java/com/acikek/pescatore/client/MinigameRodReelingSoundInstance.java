package com.acikek.pescatore.client;

import com.acikek.pescatore.item.rod.MinigameFishingRodItem;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;

public class MinigameRodReelingSoundInstance extends MovingSoundInstance {

    public PlayerEntity player;

    protected MinigameRodReelingSoundInstance(PlayerEntity player) {
        super(MinigameFishingRodItem.REELING_SOUND, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.player = player;
        attenuationType = AttenuationType.LINEAR;
        repeat = true;
        repeatDelay = 0;
    }

    @Override
    public void tick() {
        if (!player.isUsingItem() || !(player.getActiveItem().getItem() instanceof MinigameFishingRodItem)) {
            setDone();
            return;
        }
        x = player.getX();
        y = player.getY();
        z = player.getZ();
        volume = 1.0f;
        pitch = 1.0f;
    }
}
