package com.acikek.pescatore.item;

import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import com.acikek.pescatore.util.FishMinigamePlayer;
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
        float pitch = 0.4f / (world.getRandom().nextFloat() * 0.4f + 1.2f);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), event, SoundCategory.NEUTRAL, 1.0f, pitch);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!(user instanceof FishMinigamePlayer player)) {
            return TypedActionResult.fail(stack);
        }
        if (player.pescatore$getHook() != null) {
            if (!world.isClient()) {
                int i = player.pescatore$getHook().use(stack);
                stack.damage(i, user, p -> p.sendToolBreakStatus(hand));
            }
            playSound(world, user, SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE);
            user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            playSound(world, user, SoundEvents.ENTITY_FISHING_BOBBER_THROW);
            if (!world.isClient()) {
                // TODO: Use these enchantments in other ways
                int i = EnchantmentHelper.getLure(stack);
                int j = EnchantmentHelper.getLuckOfTheSea(stack);
                world.spawnEntity(new MinigameFishingBobberEntity(user, world, tier));
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
