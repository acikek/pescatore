package com.acikek.pescatore.item;

import com.acikek.pescatore.advancement.MinigameFishCaughtCriterion;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import com.acikek.pescatore.util.FishMinigamePlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MinigameFishingRodItem extends Item {

    public final MinigameRodTier tier;

    public MinigameFishingRodItem(Settings settings, MinigameRodTier tier) {
        super(settings);
        this.tier = tier;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("MaxHoldTicks");
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        stack.getOrCreateNbt().putBoolean("Reeling", true);
        // TODO: play reeling sound
    }

    private static void playSound(World world, LivingEntity user, SoundEvent event, float pitch) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), event, SoundCategory.NEUTRAL, 1.0f, pitch);
    }

    private static void playRodSound(World world, LivingEntity user, SoundEvent event) {
        float pitch = 0.4f / (world.getRandom().nextFloat() * 0.4f + 1.2f);
        playSound(world, user, event, pitch);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!(user instanceof FishMinigamePlayer player)) {
            return TypedActionResult.fail(stack);
        }
        if (player.pescatore$getHook() != null) {
            if (stack.hasNbt() && stack.getNbt().getBoolean("Reeling")) {
                player.pescatore$getHook().use();
                playSound(world, user, SoundEvents.ENTITY_ITEM_BREAK, 1.0f);
                return TypedActionResult.success(stack, world.isClient());
            }
            if (getMaxUseTime(stack) != 0) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(stack);
            }
            player.pescatore$getHook().use();
            playRodSound(world, user, SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE);
            user.getItemCooldownManager().set(this, 60);
        } else {
            playRodSound(world, user, SoundEvents.ENTITY_FISHING_BOBBER_THROW);
            if (!world.isClient()) {
                // TODO: Use these enchantments in other ways
                int i = EnchantmentHelper.getLure(stack);
                int j = EnchantmentHelper.getLuckOfTheSea(stack);
                world.spawnEntity(new MinigameFishingBobberEntity(user, world, tier));
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
            stack.getOrCreateNbt().putInt("CustomModelData", 1);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    public static boolean isReelSuccess(FishMinigamePlayer player, float progress) {
        float err = MathHelper.abs(1.0f - progress);
        System.out.println(progress + " " + err);
        if (err <= 0.50f) {
            return true;
        }
        if (err > 0.70f) {
            return false;
        }
        return player.pescatore$getHook().spawnedFish.getRandom().nextFloat() >= err;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (!(user instanceof FishMinigamePlayer player)) {
            return;
        }
        MinigameFishingBobberEntity bobber = player.pescatore$getHook();
        MinigameFishType type = bobber.spawnedFish.type();
        float progress = (float) user.getItemUseTime() / type.getPerfectHoldTime();
        bobber.use();
        if (MathHelper.abs(1.0f - progress) > 0.5) {
            playSound(world, user, SoundEvents.ENTITY_ITEM_BREAK, 1.0f);
            bobber.spawnedFish.flee();
            return;
        }
        ItemEntity fishedItem = new ItemEntity(world, bobber.getX(), bobber.getY(), bobber.getZ(), type.item().asItem().getDefaultStack());
        Vec3d between = user.getPos().subtract(bobber.getPos()).multiply(0.1);
        between = between.add(0.0, Math.sqrt(between.length()) * 0.08, 0.0);
        fishedItem.setVelocity(between);
        world.spawnEntity(fishedItem);
        world.spawnEntity(new ExperienceOrbEntity(world, user.getX(), user.getY() + 0.5, user.getZ() + 0.5, user.getRandom().nextInt(6) + 1));
        playRodSound(world, user, SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE);
        bobber.spawnedFish.discard();
        if (user instanceof ServerPlayerEntity serverPlayer) {
            type.incrementStats(serverPlayer);
            MinigameFishCaughtCriterion.INSTANCE.trigger(serverPlayer, stack, type);
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public int getEnchantability() {
        return 1;
    }
}
