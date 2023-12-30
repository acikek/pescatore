package com.acikek.pescatore.entity;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.lookup.MinigameFishTypeLookup;
import com.acikek.pescatore.api.properties.MinigameFishSize;
import com.acikek.pescatore.api.type.MinigameFishType;
import com.acikek.pescatore.item.rod.MinigameRodTier;
import com.acikek.pescatore.util.FishMinigamePlayer;
import com.sun.jna.platform.EnumUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

// TODO: Ambient sound of conduit something
public class MinigameFishingBobberEntity extends ProjectileEntity {

    public static final EntityType<MinigameFishingBobberEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.<MinigameFishingBobberEntity>create(SpawnGroup.MISC, MinigameFishingBobberEntity::new)
                    .disableSaving().disableSummon()
                    .trackRangeChunks(4).trackedUpdateRate(5)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .build();

    public static final TrackedData<Integer> TIER = DataTracker.registerData(MinigameFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> LURE = DataTracker.registerData(MinigameFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Integer> LUCK_OF_THE_SEA = DataTracker.registerData(MinigameFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int removalTimer;
    private boolean bobbing;
    private MinigameFishType type;
    private MinigameFishEntity spawnedFish;
    private int appearTimer;

    public final Predicate<BlockPos> notWater = pos -> !getWorld().getFluidState(pos).isOf(Fluids.WATER);
    public final Predicate<BlockPos> belowMe = pos -> getBlockPos().subtract(pos).getY() > 0;

    public MinigameFishingBobberEntity(EntityType<MinigameFishingBobberEntity> entityType, World world, MinigameRodTier tier, int lure, int luckOfTheSea) {
        super(entityType, world);
        ignoreCameraFrustum = true;
        setTier(tier);
        setLure(lure);
        setLuckOfTheSea(luckOfTheSea);
        appearTimer = world.getRandom().nextBetween(tier.minDelay, tier.maxDelay);
    }

    public MinigameFishingBobberEntity(EntityType<MinigameFishingBobberEntity> entityType, World world) {
        this(entityType, world, MinigameRodTier.ROOKIE, 0, 0);
    }

    public MinigameFishingBobberEntity(World world, MinigameRodTier tier, int lure, int luckOfTheSea) {
        this(ENTITY_TYPE, world, tier, lure, luckOfTheSea);
    }

    public MinigameFishingBobberEntity(PlayerEntity player, World world, MinigameRodTier tier, int lure, int luckOfTheSea) {
        this(world, tier, lure, luckOfTheSea);
        setOwner(player);
        float pitch = player.getPitch();
        float yaw = player.getYaw();
        float yawX = MathHelper.cos(-yaw * MathHelper.RADIANS_PER_DEGREE - MathHelper.PI);
        float yawY = MathHelper.sin(-yaw * MathHelper.RADIANS_PER_DEGREE - MathHelper.PI);
        float pitchX = -MathHelper.cos(-pitch * MathHelper.RADIANS_PER_DEGREE);
        float pitchY = MathHelper.sin(-pitch * MathHelper.RADIANS_PER_DEGREE);
        double x = player.getX() - yawY * 0.3;
        double y = player.getEyeY();
        double z = player.getZ() - yawX * 0.3;
        refreshPositionAndAngles(x, y, z, yaw, pitch);
        Vec3d velocity = new Vec3d(-yawY, MathHelper.clamp(-pitchY / pitchX, -5.0f, 5.0f), -yawX);
        double len = velocity.length();
        velocity = velocity.multiply(
                0.6 / len + this.random.nextTriangular(0.5, 0.0103365),
                0.6 / len + this.random.nextTriangular(0.5, 0.0103365),
                0.6 / len + this.random.nextTriangular(0.5, 0.0103365));
        setVelocity(velocity);
        setYaw((float) (MathHelper.atan2(velocity.x, velocity.z) * MathHelper.DEGREES_PER_RADIAN));
        setPitch((float) (MathHelper.atan2(velocity.y, velocity.horizontalLength()) * MathHelper.DEGREES_PER_RADIAN));
        prevYaw = getYaw();
        prevPitch = getPitch();
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 4096.0;
    }

    protected boolean removeIfInvalid(PlayerEntity player) {
        boolean inHand = getTier().matchesStack(player.getMainHandStack());
        boolean inOffhand = getTier().matchesStack(player.getOffHandStack());
        if (!player.isRemoved() && player.isAlive() && (inHand || inOffhand) && squaredDistanceTo(player) <= 1024.0) {
            return false;
        }
        this.discard();
        return true;
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(TIER, 0);
        getDataTracker().startTracking(LURE, 0);
        getDataTracker().startTracking(LUCK_OF_THE_SEA, 0);
    }

    public boolean tickRemovalTimer() {
        if (!isOnGround()) {
            removalTimer = 0;
            return false;
        }
        removalTimer++;
        if (removalTimer >= 120) {
            discard();
            return true;
        }
        return false;
    }

    public void setBobbing() {
        setVelocity(this.getVelocity().multiply(0.3, 0.2, 0.3));
        bobbing = true;
    }

    public void trySpawnFish() {
        var orbitSolid = BlockPos.findClosest(getBlockPos(), 7, 0, notWater);
        var maxOrbitDistance = orbitSolid.map(pos -> Math.sqrt(pos.getSquaredDistance(getBlockPos())) - 1.5);
        var belowSolid = BlockPos.findClosest(getBlockPos(), maxOrbitDistance.orElse(7.0).intValue(), 1, notWater.and(belowMe));
        float roll = getWorld().random.nextFloat() / getTier().rarityBonus;
        var lookup = MinigameFishTypeLookup.create();
        maxOrbitDistance.ifPresent(dist -> lookup.byDifficulty(diff -> diff.orbitDistance() <= dist));
        belowSolid.ifPresent(pos -> lookup.bySize(size -> size.compareTo(MinigameFishSize.FATTY) <= 0));
        var randomType = lookup.rollRarity(roll).random(getWorld().random);
        if (randomType.isEmpty()) {
            int message = getWorld().random.nextInt(5);
            getPlayerOwner().sendMessage(Text.translatable("message.pescatore.no_fish_" + message), true);
            return;
        }
        type = randomType.get();
        MinigameFishEntity entity = new MinigameFishEntity(getWorld(), type, this, random.nextLong());
        // TODO: find lower spot, spawn there, and rise up?
        entity.setPosition(entity.getOrbitPosition());
        // TODO: particle fx
        getWorld().spawnEntity(entity);
        spawnedFish = entity;
    }

    public void tickBobbing(BlockPos blockPos, float waterHeight) {
        if (!getWorld().isClient() && appearTimer > 0) {
            if (getVelocity().length() < 0.1) {
                appearTimer--;
                if (appearTimer == 0) {
                    trySpawnFish();
                }
            }
        }
        Vec3d veclocity = this.getVelocity();
        double d = this.getY() + veclocity.y - (blockPos.getY() + waterHeight);
        if (Math.abs(d) < 0.01) {
            d += Math.signum(d) * 0.1;
        }
        setVelocity(veclocity.x * 0.9, veclocity.y - d * random.nextFloat() * 0.2, veclocity.z * 0.9);
    }

    public void tick() {
        super.tick();
        PlayerEntity player = this.getPlayerOwner();
        if (player == null) {
            discard();
            return;
        }
        if (!getWorld().isClient() && removeIfInvalid(player)) {
            return;
        }
        if (tickRemovalTimer()) {
            return;
        }
        BlockPos blockPos = getBlockPos();
        FluidState fluidState = getWorld().getFluidState(blockPos);
        float waterHeight = fluidState.isIn(FluidTags.WATER)
                ? fluidState.getHeight(getWorld(), getBlockPos())
                : 0.0f;
        boolean inWater = waterHeight > 0.0f;
        if (!bobbing && inWater) {
            setBobbing();
            return;
        }
        if (bobbing) {
            tickBobbing(blockPos, waterHeight);
        }
        if (!fluidState.isIn(FluidTags.WATER)) {
            setVelocity(getVelocity().add(0.0, -0.03, 0.0));
        }
        move(MovementType.SELF, getVelocity());
        updateRotation();
        if (!bobbing && (isOnGround() || horizontalCollision)) {
            setVelocity(Vec3d.ZERO);
        }
        setVelocity(getVelocity().multiply(0.92));
        refreshPosition();
    }

    public void use() {
        PlayerEntity player = getPlayerOwner();
        if (getWorld().isClient() || player == null || removeIfInvalid(player)) {
            return;
        }
        var match = getMatchingStack();
        if (match == null) {
            return;
        }
        ItemStack stack = match.getLeft();
        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();
            nbt.remove("CustomModelData");
            nbt.remove("Reeling");
            nbt.remove("MaxHoldTicks");
        }
        player.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        int damage = isOnGround() ? 2 : 0;
        if (spawnedFish != null) {
            damage += (int) type.size().scale();
            damage = Math.min(5, damage);
        }
        stack.damage(damage, player, p -> p.sendToolBreakStatus(match.getRight()));
        discard();
    }

    public <T extends ParticleEffect> void spawnParticles(ServerWorld world, T type, double speed) {
        world.spawnParticles(type, getX(), getY() + 0.5, getZ(), (int) (1.0f + getWidth() * 20.0f), getWidth(), 0.0, getWidth(), speed);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return false;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        setVelocity(getVelocity().normalize().multiply(blockHitResult.squaredDistanceTo(this)));
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        setPlayerFishHook(null);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        setPlayerFishHook(null);
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        setPlayerFishHook(this);
    }

    private void setPlayerFishHook(@Nullable MinigameFishingBobberEntity hook) {
        if (getPlayerOwner() instanceof FishMinigamePlayer player) {
            player.pescatore$setHook(hook);
        }
    }

    @Nullable
    public PlayerEntity getPlayerOwner() {
        return getOwner() instanceof PlayerEntity player ? player : null;
    }

    public void setTier(int tier) {
        getDataTracker().set(TIER, tier);
    }

    public void setTier(MinigameRodTier tier) {
        setTier(tier.ordinal());
    }

    public MinigameRodTier getTier() {
        return EnumUtils.fromInteger(getDataTracker().get(TIER), MinigameRodTier.class);
    }

    public void setLure(int lure) {
        getDataTracker().set(LURE, lure);
    }

    public int getLure() {
        return getDataTracker().get(LURE);
    }

    public void setLuckOfTheSea(int luckOfTheSea) {
        getDataTracker().set(LUCK_OF_THE_SEA, luckOfTheSea);
    }

    public int getLuckOfTheSea() {
        return getDataTracker().get(LUCK_OF_THE_SEA);
    }

    public MinigameFishEntity spawnedFish() {
        return spawnedFish;
    }

    public void setSpawnedFish(MinigameFishEntity spawnedFish) {
        this.spawnedFish = spawnedFish;
    }

    public Pair<ItemStack, Hand> getMatchingStack() {
        PlayerEntity player = getPlayerOwner();
        if (player == null) {
            return null;
        }
        ItemStack main = player.getStackInHand(Hand.MAIN_HAND);
        if (getTier().matchesStack(main)) {
            return new Pair<>(main, Hand.MAIN_HAND);
        }
        ItemStack offhand = player.getStackInHand(Hand.OFF_HAND);
        if (getTier().matchesStack(offhand)) {
            return new Pair<>(offhand, Hand.OFF_HAND);
        }
        return null;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        Entity entity = this.getOwner();
        return new EntitySpawnS2CPacket(this, entity == null ? this.getId() : entity.getId());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        if (getPlayerOwner() == null) {
            kill();
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Tier", getTier().ordinal());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setTier(nbt.getInt("Tier"));
    }

    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, Pescatore.id("minigame_fishing_bobber"), ENTITY_TYPE);
    }
}
