package com.acikek.pescatore.item;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.Locale;

public enum MinigameRodTier {

    ROOKIE(Rarity.COMMON, 1.0f, 100, 150),
    ADEPT(Rarity.UNCOMMON, 1.15f, 70, 100),
    EXPERT(Rarity.RARE, 1.35f, 50, 70),
    AETHER(Rarity.EPIC, 2.0f, 20, 30);

    public final Rarity rarity;
    public final float rarityBonus;
    public final int minDelay;
    public final int maxDelay;

    public final Identifier bobberTexture;

    MinigameRodTier(Rarity rarity, float rarityBonus, int minDelay, int maxDelay) {
        this.rarity = rarity;
        this.rarityBonus = rarityBonus;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        bobberTexture = Pescatore.id("textures/entity/minigame_fishing_hook/" + name().toLowerCase(Locale.ROOT) + ".png");
    }

    public MinigameFishingRodItem createRod() {
        return new MinigameFishingRodItem(new FabricItemSettings().maxDamage(64).rarity(rarity), this);
    }

    public boolean matchesStack(ItemStack stack) {
        return stack.getItem() instanceof MinigameFishingRodItem rodItem && this.equals(rodItem.tier);
    }
}
