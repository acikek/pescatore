package com.acikek.pescatore.client;

import com.acikek.pescatore.client.render.bobber.MinigameFishingBobberEntityRenderer;
import com.acikek.pescatore.client.render.fish.MinigameFishEntityRenderer;
import com.acikek.pescatore.client.render.fish.MinigameFishModel;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class PescatoreClient implements ClientModInitializer {

    public static void playReelingSound(PlayerEntity player) {
        MinecraftClient.getInstance().getSoundManager().play(new MinigameRodReelingSoundInstance(player));
    }

    @Override
    public void onInitializeClient() {
        MinigameFishEntityRenderer.register();
        MinigameFishModel.register();
        MinigameFishingBobberEntityRenderer.register();
    }
}
