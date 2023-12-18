package com.acikek.pescatore.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MinigameFishingRodItem extends Item {

    public final MinigameRodTier tier;

    public MinigameFishingRodItem(Settings settings, MinigameRodTier tier) {
        super(settings);
        this.tier = tier;
    }

    private void playSound(World world, PlayerEntity user, SoundEvent event) {
        float pitch = 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), event, SoundCategory.NEUTRAL, 1.0f, pitch);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.fishHook != null) {
            if (!world.isClient()) {
                int i = user.fishHook.use(stack);
                stack.damage(i, user, p -> p.sendToolBreakStatus(hand));
            }
            playSound(world, user, SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE);
            user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            playSound(world, user, SoundEvents.ENTITY_FISHING_BOBBER_THROW);
            if (!world.isClient()) {
                int i = EnchantmentHelper.getLure(stack);
                int j = EnchantmentHelper.getLuckOfTheSea(stack);
                world.spawnEntity(new FishingBobberEntity(user, world, j, i));
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public int getEnchantability() {
        return 1;
    }
}
