package com.acikek.pescatore.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class SardineCanItem extends Item {

    public SardineCanItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack used = super.finishUsing(stack, world, user);
        if (!(user instanceof PlayerEntity player) || !player.getAbilities().creativeMode) {
            used.increment(1); // Whatever
        }
        ServerPlayerEntity serverPlayer = null;
        if (user instanceof ServerPlayerEntity player) {
            serverPlayer = player;
        }
        System.out.println(used);
        if (used.damage(1, world.random, serverPlayer)) {
            return PescatoreItems.EMPTY_SARDINE_CAN.getDefaultStack();
        }
        System.out.println(used);
        return used;
    }
}
