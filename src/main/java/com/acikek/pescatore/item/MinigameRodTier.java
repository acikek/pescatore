package com.acikek.pescatore.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;

public record MinigameRodTier(float rareFishChance, int minDelay, int maxDelay) {

    public static final MinigameRodTier ROOKIE = new MinigameRodTier(0.05f, 100, 150);
    public static final MinigameRodTier ADEPT = new MinigameRodTier(0.15f, 70, 100);
    public static final MinigameRodTier EXPERT = new MinigameRodTier(0.25f, 50, 70);

    public MinigameFishingRodItem createRod() {
        return new MinigameFishingRodItem(new FabricItemSettings().maxDamage(64), this);
    }
}
