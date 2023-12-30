package com.acikek.pescatore.entity;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.type.MinigameFishType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
    private Vec3d initialBobberPos;
    private double orbitAngle = -1.0;
    private int strikeOffset = -1;
    private int combinedStrikeLength = -1;
    private int combinedStrikeTicks = 0;
    private double strikeInterval = 0;
    private int bitingTicks = 0;
    private int revolutionTicks;
    private int currentRevolution;
    private int fleeingTicks = 0;
    private Vec3d fleeingVector;
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
                if (bobber.spawnedFish() == null) {
                    bobber.setSpawnedFish(this);
                }
                initialBobberPos = bobber.getPos();
            }
        }
        if (data.equals(RANDOM_SEED)) {
            random.setSeed(getRandomSeed());
            if (orbitAngle < 0.0) {
                initRandomness();
            }
        }
        super.onTrackedDataSet(data);
    }

    public int getStrikeTicks() {
        return combinedStrikeTicks % TOTAL_STRIKE_INTERVAL;
    }

    public boolean isBiting() {
        return bitingTicks > 0;
    }

    public boolean isFleeing() {
        return fleeingTicks > 0;
    }

    public float getAnimationSpeed() {
        if (isBiting()) {
            return 4.0f;
        }
        if (isFleeing()) {
            return 6.0f;
        }
        return 2.0f;
    }

    public void nibble() {
        if (getWorld() instanceof ServerWorld serverWorld) {
            playSound(SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, 1.5f, 1.0f);
            bobber.spawnParticles(serverWorld, ParticleTypes.FISHING, 0.2);
        }
    }

    public void startBiting() {
        bitingTicks = 1;
        var match = bobber.getMatchingStack();
        if (match == null) {
            return;
        }
        NbtCompound nbt = match.getLeft().getOrCreateNbt();
        nbt.putInt("MaxHoldTicks", type.getMaxHoldTime(bobber.getLuckOfTheSea()));
        bite();
    }

    public void bite() {
        if (getWorld() instanceof ServerWorld serverWorld) {
            playSound(SoundEvents.ENTITY_AXOLOTL_SWIM, 1.5f, 1.0f);
            bobber.spawnParticles(serverWorld, ParticleTypes.BUBBLE, 0.2);
            bobber.spawnParticles(serverWorld, ParticleTypes.FISHING, 0.2);
        }
    }

    public void breakLine() {
        if (!getWorld().isClient()) {
            playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
        }
        flee(true);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!touchingWater) {
            vanish();
        }
        if (isFleeing()) {
            fleeingTicks++;
            if (fleeingTicks >= 30) {
                vanish();
            }
            return;
        }
        if (isBiting()) {
            bitingTicks++;
            if (bitingTicks % 7 == 0) {
                bite();
            }
            if (bitingTicks >= type.getMaxHoldTime(bobber.getLuckOfTheSea())) {
                PlayerEntity player = bobber.getPlayerOwner();
                if ((player == null || !player.isUsingItem()) && !isRemoved()) {
                    breakLine();
                }
            }
            bobber.setPosition(initialBobberPos.add(0.0, -0.35 * type.size().scale(), 0.0));
            return;
        }
        if (age <= strikeOffset) {
            return;
        }
        if (getStrikeTicks() == 0) {
            strikeInterval = TOTAL_STRIKE_INTERVAL * (0.6 + random.nextDouble() * 0.3);
        }
        combinedStrikeTicks++;
        if (getStrikeTicks() == type.difficulty().strikeDuration()) {
            nibble();
        }
        if (combinedStrikeTicks >= combinedStrikeLength) {
            startBiting();
        }
    }

    public double getOrbitRadius() {
        double base = type.difficulty().orbitDistance();
        double halfSize = 0.35 * type.size().scale();
        if (combinedStrikeTicks <= 0) {
            return base + halfSize;
        }
        if (bitingTicks > 0) {
            return halfSize;
        }
        int ticks = getStrikeTicks();
        int t = type.difficulty().strikeDuration();
        double mul = ticks <= t
                ? ticks * (1.0 / t) + 1.0
                : (ticks - strikeInterval) * (1.0 / (strikeInterval - t)) + 1.0;
        return base * Math.min(mul, 1.0) + halfSize;
    }

    public double getOrbitSpeed() {
        double base = type.difficulty().orbitSpeed();
        return bitingTicks > 0
                ? base * 0.2
                : base * (getStrikeTicks() < strikeInterval ? 0.75 : 1.0);
    }

    public Vec3d getOrbitPosition() {
        double r = getOrbitRadius();
        // TODO: Interpolation needed?
        double y = -0.4 * type.size().scale(); //-0.4 - r / type.difficulty().orbitDistance() * 0.8;
        Vec3d offset = new Vec3d(r * Math.cos(orbitAngle), y, r * Math.sin(orbitAngle));
        return initialBobberPos.add(offset);
    }

    @Override
    public void tickMovement() {
        if (isFleeing()) {
            double theta = Math.atan2(-fleeingVector.x, fleeingVector.z);
            setYaw((float) MathHelper.wrapDegrees(Math.toDegrees(theta)));
            move(MovementType.SELF, fleeingVector);
            return;
        }
        if (bobber == null || bobber.isRemoved()) {
            flee(false);
            return;
        }
        orbitAngle += getOrbitSpeed();
        setPosition(getOrbitPosition());
        if (!getWorld().getFluidState(getBlockPos()).isOf(Fluids.WATER)) {
            vanish();
        }
        lookAtEntity(bobber, 180.0f, 0.0f);
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

    public long getRandomSeed() {
        return getDataTracker().get(RANDOM_SEED);
    }

    public void setRandomSeed(long seed) {
        getDataTracker().set(RANDOM_SEED, seed);
    }

    public int getNibbles() {
        int value = random.nextBetween(type.difficulty().minNibbles(), type.difficulty().maxNibbles());
        return Math.max(0, value - bobber.getLure());
    }

    public void initRandomness() {
        orbitAngle = random.nextDouble() * Math.PI * 2.0;
        strikeOffset = random.nextBetween(80, 160);
        combinedStrikeLength = TOTAL_STRIKE_INTERVAL * getNibbles() + type.difficulty().strikeDuration();
    }

    public void flee(boolean use) {
        if (use && bobber != null) {
            bobber.use(true);
        }
        if (initialBobberPos == null) {
            vanish();
            return;
        }
        fleeingTicks = 1;
        fleeingVector = getPos().subtract(initialBobberPos)
                .normalize()
                .withAxis(Direction.Axis.Y, 0.0)
                .multiply(0.3);
    }

    public void vanish() {
        if (getWorld() instanceof ServerWorld serverWorld) {
            playSound(SoundEvents.ENTITY_FISH_SWIM, 1.0f, 1.0f);
            serverWorld.spawnParticles(ParticleTypes.BUBBLE, getX(), getY() + 1.0, getZ(), 20, getWidth(), 0.0, getWidth(), 0.2);
            serverWorld.spawnParticles(ParticleTypes.FISHING, getX(), getY() + 1.0, getZ(), 5, getWidth(), 0.0, getWidth(), 0.2);
        }
        remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        if (!state.getFluidState().isOf(Fluids.WATER)) {
            vanish();
        }
    }

    @Override
    public boolean collidesWith(Entity other) {
        return other instanceof PlayerEntity;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        flee(true);
    }

    @Override
    public void onDamaged(DamageSource damageSource) {
        super.onDamaged(damageSource);
        flee(true);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        return null;
        // TODO: Fix summon crash (you shouldn't be summoning anyway but eh)
        // TODO: random type
        // setTypeId(MinigameFishType.EMPTY);
        // return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("FishTypeId", getTypeId());
        nbt.putLong("FishSeed", getRandomSeed());
        if (bobber != null) {
            nbt.putInt("BobberId", bobber.getId());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setTypeId(nbt.getInt("FishTypeId"));
        setRandomSeed(nbt.getLong("FishSeed"));
        setBobberId(nbt.getInt("BobberId"));
    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fish"), ENTITY_TYPE);
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createMobAttributes());
    }
}
