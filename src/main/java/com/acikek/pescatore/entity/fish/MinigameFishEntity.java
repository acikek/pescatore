package com.acikek.pescatore.entity.fish;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MinigameFishEntity extends WaterCreatureEntity {

    public static final EntityType<MinigameFishEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.<MinigameFishEntity>create(SpawnGroup.MISC, MinigameFishEntity::new)
                    .dimensions(EntityDimensions.changing(0.70f, 0.35f))
                    .trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE)
                    .build();

    public static final TrackedData<Float> FISH_SCALE = DataTracker.registerData(MinigameFishEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Integer> BOBBER_ID = DataTracker.registerData(MinigameFishEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> TICK_OFFSET = DataTracker.registerData(MinigameFishEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private MinigameFishType type;
    private MinigameFishingBobberEntity bobber;
    public final AnimationState animation = new AnimationState();

    public MinigameFishEntity(EntityType<MinigameFishEntity> entityType, World world, MinigameFishType type, MinigameFishingBobberEntity bobber, int tickOffset) {
        super(entityType, world);
        setType(type);
        if (bobber != null) {
            setBobberId(bobber);
        }
        setTickOffset(tickOffset);
        reinitDimensions();
        animation.startIfNotRunning(age);
    }

    public MinigameFishEntity(EntityType<MinigameFishEntity> entityType, World world) {
        this(entityType, world, MinigameFishType.EMPTY, null, 0);
    }

    public MinigameFishEntity(World world, MinigameFishType type, MinigameFishingBobberEntity bobber, int tickOffset) {
        this(ENTITY_TYPE, world, type, bobber, tickOffset);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(FISH_SCALE, 1.0f);
        getDataTracker().startTracking(BOBBER_ID, 0);
        getDataTracker().startTracking(TICK_OFFSET, 0);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (data.equals(FISH_SCALE)) {
            calculateDimensions();
        }
        if (data.equals(BOBBER_ID)) {
            int id = getDataTracker().get(BOBBER_ID);
            if (id > 0 && getWorld().getEntityById(id) instanceof MinigameFishingBobberEntity entity) {
                bobber = entity;
            }
        }
        super.onTrackedDataSet(data);
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
    }

    public MinigameFishType type() {
        return type;
    }

    public void setType(MinigameFishType type) {
        this.type = type;
        setScale(type.size().scale());
    }

    public void setBobberId(int id) {
        getDataTracker().set(BOBBER_ID, id);
    }

    public void setBobberId(MinigameFishingBobberEntity bobber) {
        setBobberId(bobber.getId());
    }

    public int getTickOffset() {
        return getDataTracker().get(TICK_OFFSET);
    }

    public void setTickOffset(int offset) {
        getDataTracker().set(TICK_OFFSET, offset);
    }

    public int getOrbitTicks() {
        return age + getTickOffset();
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
        if (!touchingWater) {
            vanish();
        }
        // TODO: If player detected within a few blocks, disappear
    }

    @FunctionalInterface
    public interface OrbitPhysics {
        Vec3d get(double r, double theta, double omega, double y);

        OrbitPhysics POSITION = (r, theta, omega, y) -> new Vec3d(r * Math.cos(theta), y, r * Math.sin(theta));
        OrbitPhysics VELOCITY = (r, theta, omega, y) -> new Vec3d(r * omega * -Math.sin(theta), y, r * omega * Math.cos(theta));
    }

    public Vec3d getOrbitVector(OrbitPhysics physics, double y) {
        double omega = type.difficulty().orbitSpeed();
        double theta = getOrbitTicks() * omega;
        double r = type.difficulty().orbitDistance();
        return physics.get(r, theta, omega, y);
    }

    @Override
    public void tickMovement() {
        move(MovementType.SELF, getOrbitVector(OrbitPhysics.VELOCITY, 0.0));
        if (bobber != null) {
            lookAtEntity(bobber, 360.0f, 0.0f);
        }
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
        flee();
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        setType(MinigameFishType.EMPTY);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("FishScale", getScale());
        nbt.putInt("TickOffset", getTickOffset());
        if (bobber != null) {
            nbt.putInt("BobberId", bobber.getId());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setScale(nbt.getFloat("FishScale"));
        setTickOffset(nbt.getInt("TickOffset"));
        setBobberId(nbt.getInt("BobberId"));
    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fish"), ENTITY_TYPE);
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createMobAttributes());
    }
}
