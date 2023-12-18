package com.acikek.pescatore.client;

import net.fabricmc.api.ClientModInitializer;

public class PescatoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MinigameFishEntityRenderer.register();
        MinigameFishModel.register();
    }
}
