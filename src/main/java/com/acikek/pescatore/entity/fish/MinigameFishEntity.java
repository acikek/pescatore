package com.acikek.pescatore.entity.fish;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class MinigameFishEntity extends WaterCreatureEntity {

    public static final EntityType<MinigameFishEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MinigameFishEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build();

    public AnimationState animation = new AnimationState();

    public MinigameFishEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        animation.startIfNotRunning(age);
    }

    public void flee() {
        // TODO: "Disappear" animation (run away, fade out)
        // TODO: Sound effects at the very least
        remove(RemovalReason.DISCARDED);
    }

    public void vanish() {
        // TODO: Particles
        playSound(SoundEvents.ENTITY_FISH_SWIM, 1.0f, 1.0f);
        remove(RemovalReason.DISCARDED);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!isSubmergedIn(FluidTags.WATER)) {
            vanish();
        }
        // TODO: If player detected within a few blocks, disappear
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
        flee();
    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fish"), ENTITY_TYPE);
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createMobAttributes());
    }
}
