package com.acikek.pescatore.entity;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

public class MinigameFishingBobberEntity extends ProjectileEntity {

    public static final EntityType<MinigameFishingBobberEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.<MinigameFishingBobberEntity>create(SpawnGroup.MISC, MinigameFishingBobberEntity::new)
                    .disableSaving().disableSummon()
                    .trackRangeChunks(4).trackedUpdateRate(5)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .build();

    public MinigameFishingBobberEntity(EntityType<? extends MinigameFishingBobberEntity> entityType, World world) {
        super(entityType, world);
    }

    public MinigameFishingBobberEntity(World world) {
        this(ENTITY_TYPE, world);
    }

    protected boolean removeIfInvalid(PlayerEntity player) {
        ItemStack itemStack = player.getMainHandStack();
        ItemStack itemStack2 = player.getOffHandStack();
        boolean bl = itemStack.isOf(Items.FISHING_ROD);
        boolean bl2 = itemStack2.isOf(Items.FISHING_ROD);
        if (!player.isRemoved() && player.isAlive() && (bl || bl2) && !(this.squaredDistanceTo(player) > 1024.0)) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    protected void initDataTracker() {

    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fishing_bobber"), ENTITY_TYPE);
    }
}
