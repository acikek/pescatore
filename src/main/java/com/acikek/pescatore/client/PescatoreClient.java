package com.acikek.pescatore.client;

import com.acikek.pescatore.client.render.bobber.MinigameFishingBobberEntityRenderer;
import com.acikek.pescatore.client.render.fish.MinigameFishEntityRenderer;
import com.acikek.pescatore.client.render.fish.MinigameFishModel;
import net.fabricmc.api.ClientModInitializer;

public class PescatoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MinigameFishEntityRenderer.register();
        MinigameFishModel.register();
        MinigameFishingBobberEntityRenderer.register();
    }
}
