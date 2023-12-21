package com.acikek.pescatore.item;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Locale;

public enum MinigameRodTier {

    ROOKIE(0.05f, 100, 150),
    ADEPT(0.15f, 70, 100),
    EXPERT(0.25f, 50, 70);

    public final float rareFishChance;
    public final int minDelay;
    public final int maxDelay;

    public final Identifier bobberTexture;

    MinigameRodTier(float rareFishChance, int minDelay, int maxDelay) {
        this.rareFishChance = rareFishChance;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        bobberTexture = Pescatore.id("textures/entity/minigame_fishing_hook/" + name().toLowerCase(Locale.ROOT) + ".png");
    }

    public MinigameFishingRodItem createRod() {
        return new MinigameFishingRodItem(new FabricItemSettings().maxDamage(64), this);
    }

    public boolean matchesStack(ItemStack stack) {
        return stack.getItem() instanceof MinigameFishingRodItem rodItem && this.equals(rodItem.tier);
    }
}
