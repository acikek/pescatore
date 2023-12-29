package com.acikek.pescatore.client;

import com.acikek.pescatore.client.render.bobber.MinigameFishingBobberEntityRenderer;
import com.acikek.pescatore.client.render.fish.MinigameFishEntityRenderer;
import com.acikek.pescatore.client.render.fish.MinigameFishModel;
import com.acikek.pescatore.item.rod.MinigameFishingRodItem;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

public class PescatoreClient implements ClientModInitializer {

    private static SoundInstance reelingInstance;

    public static void playReelingSound() {
        MinecraftClient client = MinecraftClient.getInstance();
        reelingInstance = new EntityTrackingSoundInstance(
                MinigameFishingRodItem.REELING_SOUND, SoundCategory.PLAYERS, 1.0f, 1.0f,
                client.player, client.world.random.nextLong()
        );
        client.getSoundManager().play(reelingInstance);
    }

    public static void stopReelingSound() {
        MinecraftClient.getInstance().getSoundManager().stop(reelingInstance);
    }

    @Override
    public void onInitializeClient() {
        MinigameFishEntityRenderer.register();
        MinigameFishModel.register();
        MinigameFishingBobberEntityRenderer.register();
    }
}
