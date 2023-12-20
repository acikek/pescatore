package com.acikek.pescatore.entity.fish;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.properties.MinigameFishType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class MinigameFishEntity extends WaterCreatureEntity {

    public static final EntityType<MinigameFishEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.<MinigameFishEntity>create(SpawnGroup.MISC, MinigameFishEntity::new)
                    .dimensions(EntityDimensions.changing(0.70f, 0.35f))
                    .trackRangeChunks(4).trackedUpdateRate(5)
                    .build();

    public static final TrackedData<Float> FISH_SCALE = DataTracker.registerData(MinigameFishEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private MinigameFishType type;
    public final AnimationState animation = new AnimationState();

    public MinigameFishEntity(EntityType<MinigameFishEntity> entityType, World world, MinigameFishType type) {
        super(entityType, world);
        setType(type);
        animation.startIfNotRunning(age);
    }

    public MinigameFishEntity(EntityType<MinigameFishEntity> entityType, World world) {
        this(entityType, world, MinigameFishType.EMPTY);
    }

    public MinigameFishEntity(World world, MinigameFishType type) {
        this(ENTITY_TYPE, world, type);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(FISH_SCALE, 1.0f);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(getScale());
    }

    public float getScale() {
        return getDataTracker().get(FISH_SCALE);
    }

    private void setScale(float scale) {
        getDataTracker().set(FISH_SCALE, scale);
        calculateDimensions();
    }

    public MinigameFishType type() {
        return type;
    }

    public void setType(MinigameFishType type) {
        this.type = type;
        setScale(type.size().scale());
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

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("FishScale", getScale());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setScale(nbt.getFloat("FishScale"));
    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fish"), ENTITY_TYPE);
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createMobAttributes());
    }
}
