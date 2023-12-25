package com.acikek.pescatore.entity.fish;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.acikek.pescatore.entity.MinigameFishingBobberEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

// TODO: flee when bobber is null
public class MinigameFishEntity extends WaterCreatureEntity {

    public static final EntityType<MinigameFishEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.<MinigameFishEntity>create(SpawnGroup.MISC, MinigameFishEntity::new)
                    .dimensions(EntityDimensions.changing(0.70f, 0.35f))
                    .trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE)
                    .build();

    public static final int TOTAL_STRIKE_INTERVAL = 40;

    public static final TrackedData<Integer> TYPE_ID = DataTracker.registerData(MinigameFishEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> BOBBER_ID = DataTracker.registerData(MinigameFishEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Long> RANDOM_SEED = DataTracker.registerData(MinigameFishEntity.class, TrackedDataHandlerRegistry.LONG);

    private MinigameFishType type;
    private MinigameFishingBobberEntity bobber;
    private int orbitOffset = -1;
    private int strikeOffset = -1;
    private int combinedStrikeLength = -1;
    private int combinedStrikeTicks = 0;
    private double strikeInterval = 0;
    private int bitingTicks = 0;
    private int revolutionTicks;
    private int currentRevolution;
    public final AnimationState animation = new AnimationState();

    public MinigameFishEntity(EntityType<MinigameFishEntity> entityType, World world, MinigameFishType type, MinigameFishingBobberEntity bobber, long seed) {
        super(entityType, world);
        setTypeId(type);
        if (bobber != null) {
            setBobberId(bobber);
        }
        setRandomSeed(seed);
        animation.startIfNotRunning(age);
    }

    public MinigameFishEntity(EntityType<MinigameFishEntity> entityType, World world) {
        this(entityType, world, MinigameFishType.EMPTY, null, 0L);
    }

    public MinigameFishEntity(World world, MinigameFishType type, MinigameFishingBobberEntity bobber, long seed) {
        this(ENTITY_TYPE, world, type, bobber, seed);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(TYPE_ID, 0);
        getDataTracker().startTracking(BOBBER_ID, 0);
        getDataTracker().startTracking(RANDOM_SEED, 0L);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (data.equals(TYPE_ID)) {
            type = MinigameFishType.REGISTRY.get(getTypeId());
            calculateDimensions();
        }
        if (data.equals(BOBBER_ID)) {
            int id = getDataTracker().get(BOBBER_ID);
            if (id > 0 && getWorld().getEntityById(id) instanceof MinigameFishingBobberEntity entity) {
                bobber = entity;
            }
        }
        if (data.equals(RANDOM_SEED)) {
            random.setSeed(getRandomSeed());
            if (orbitOffset < 0) {
                initRandomness();
            }
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return super.getDimensions(pose).scaled(type.size().scale());
    }

    public void setTypeId(int id) {
        getDataTracker().set(TYPE_ID, id);
    }

    public void setTypeId(MinigameFishType type) {
        setTypeId(MinigameFishType.REGISTRY.getRawId(type));
    }

    public int getTypeId() {
        return getDataTracker().get(TYPE_ID);
    }

    public MinigameFishType type() {
        return type;
    }

    public void setBobberId(int id) {
        getDataTracker().set(BOBBER_ID, id);
    }

    public void setBobberId(MinigameFishingBobberEntity bobber) {
        setBobberId(bobber.getId());
    }

    public int getOrbitTicks() {
        return age + orbitOffset;
    }

    public long getRandomSeed() {
        return getDataTracker().get(RANDOM_SEED);
    }

    public void setRandomSeed(long seed) {
        getDataTracker().set(RANDOM_SEED, seed);
    }

    public void initRandomness() {
        orbitOffset = random.nextInt(360);
        strikeOffset = 10; //random.nextBetween(80, 160);
        int nibbles = 5; //random.nextBetween(type.difficulty().minNibbles(), type.difficulty().maxNibbles());
        combinedStrikeLength = TOTAL_STRIKE_INTERVAL * nibbles + type.difficulty().strikeDuration();
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
    protected void onBlockCollision(BlockState state) {
        if (!state.isOf(Blocks.WATER)) {
            vanish();
        }
    }

    @Override
    public boolean collidesWith(Entity other) {
        return other instanceof PlayerEntity;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        flee();
    }

    public int getStrikeTicks() {
        return combinedStrikeTicks % TOTAL_STRIKE_INTERVAL;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!touchingWater) {
            vanish();
        }
        if (bitingTicks > 0) {
            bitingTicks++;
            return;
        }
        if (age > strikeOffset) {
            if (getStrikeTicks() == 0) {
                strikeInterval = TOTAL_STRIKE_INTERVAL * (0.6 + random.nextDouble() * 0.3);
            }
            combinedStrikeTicks++;
            if (combinedStrikeTicks >= combinedStrikeLength) {
                bitingTicks = 1;
            }
        }
        // TODO: If player detected within a few blocks, disappear
    }

    public double getOrbitRadius() {
        double base = type.difficulty().orbitDistance();
        if (combinedStrikeTicks <= 0) {
            return base;
        }
        double s = 0.35 * type.size().scale() / base;
        if (bitingTicks > 0) {
            return base * s;
        }
        int ticks = getStrikeTicks();
        int t = type.difficulty().strikeDuration();
        double mul = ticks <= t
                ? ticks * ((s - 1.0) / t) + 1.0
                : (ticks - strikeInterval) * ((1.0 - s) / (strikeInterval - t)) + 1.0;
        return base * Math.min(mul, 1.0);
    }

    public Vec3d getOrbitPosition(double y) {
        double theta = getOrbitTicks() * type.difficulty().orbitSpeed();
        double r = getOrbitRadius();
        Vec3d offset = new Vec3d(r * Math.cos(theta), y, r * Math.sin(theta));
        return bobber.getPos().add(offset);
    }

    @Override
    public void tickMovement() {
        if (bobber != null) {
            // TODO: Interpolate y value when striking
            // TODO: Sin wave of y when not striking
            setPosition(getOrbitPosition(-0.8));
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
        // TODO: Fix summon crash (you shouldn't be summoning anyway but eh)
        // TODO: random type
        setTypeId(MinigameFishType.EMPTY);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("FishTypeId", getTypeId());
        nbt.putLong("FishSeed", getRandomSeed());
        nbt.putInt("OrbitOffset", orbitOffset);
        nbt.putInt("StrikeOffset", strikeOffset);
        nbt.putInt("StrikeLength", combinedStrikeLength);
        if (bobber != null) {
            nbt.putInt("BobberId", bobber.getId());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setTypeId(nbt.getInt("FishTypeId"));
        setRandomSeed(nbt.getLong("FishSeed"));
        orbitOffset = nbt.getInt("OrbitOffset");
        strikeOffset = nbt.getInt("StrikeOffset");
        combinedStrikeLength = nbt.getInt("StrikeLength");
        setBobberId(nbt.getInt("BobberId"));
    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fish"), ENTITY_TYPE);
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createMobAttributes());
    }
}
