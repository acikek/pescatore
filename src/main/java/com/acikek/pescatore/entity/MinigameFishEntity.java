package com.acikek.pescatore.entity;

import com.acikek.pescatore.Pescatore;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

public class MinigameFishEntity extends WaterCreatureEntity {

    public static final EntityType<MinigameFishEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MinigameFishEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                    .build();

    protected MinigameFishEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fish"), ENTITY_TYPE);
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createMobAttributes());
    }
}
