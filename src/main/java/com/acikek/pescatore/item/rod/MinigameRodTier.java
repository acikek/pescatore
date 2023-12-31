package com.acikek.pescatore.item.rod;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.Locale;

public enum MinigameRodTier {

    ROOKIE(1.0f, 100, 150),
    ADEPT(1.15f, 70, 100),
    EXPERT(1.35f, 50, 70),
    AETHER(2.0f, 20, 30);

    public final float rarityBonus;
    public final int minDelay;
    public final int maxDelay;

    public final Identifier bobberTexture;

    MinigameRodTier(float rarityBonus, int minDelay, int maxDelay) {
        this.rarityBonus = rarityBonus;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        bobberTexture = Pescatore.id("textures/entity/minigame_fishing_hook/" + name().toLowerCase(Locale.ROOT) + ".png");
    }

    public MinigameFishingRodItem createRod(Item.Settings settings) {
        return new MinigameFishingRodItem(settings, this);
    }

    public boolean matchesStack(ItemStack stack) {
        return stack.getItem() instanceof MinigameFishingRodItem rodItem && this.equals(rodItem.tier);
    }
}
