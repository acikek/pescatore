package com.acikek.pescatore.entity;

import com.acikek.pescatore.Pescatore;
import com.acikek.pescatore.api.properties.MinigameFishType;
import com.acikek.pescatore.entity.fish.MinigameFishEntity;
import com.acikek.pescatore.item.MinigameRodTier;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MinigameFishingBobberEntity extends ProjectileEntity {

    public static final EntityType<MinigameFishingBobberEntity> ENTITY_TYPE =
            FabricEntityTypeBuilder.<MinigameFishingBobberEntity>create(SpawnGroup.MISC, MinigameFishingBobberEntity::new)
                    .disableSaving().disableSummon()
                    .trackRangeChunks(4).trackedUpdateRate(5)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .build();

    public static final TrackedData<Integer> TIER = DataTracker.registerData(MinigameFishingBobberEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int removalTimer;
    private boolean bobbing;
    private MinigameFishType type;

    public MinigameFishingBobberEntity(EntityType<MinigameFishingBobberEntity> entityType, World world, MinigameRodTier tier) {
        super(entityType, world);
        ignoreCameraFrustum = true;
        setTier(tier);
    }

    public MinigameFishingBobberEntity(EntityType<MinigameFishingBobberEntity> entityType, World world) {
        this(entityType, world, MinigameRodTier.ROOKIE);
    }

    public MinigameFishingBobberEntity(World world, MinigameRodTier tier) {
        this(ENTITY_TYPE, world, tier);
    }

    public MinigameFishingBobberEntity(PlayerEntity player, World world, MinigameRodTier tier) {
        this(world, tier);
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
        var types = MinigameFishType.REGISTRY.stream().toList();
        type = types.get(getWorld().random.nextInt(types.size()));
        // TODO: not this
        MinigameFishEntity entity = new MinigameFishEntity(getWorld(), type);
        entity.setPosition(getPos().add(0.0, -2.0, 0.0));
        getWorld().spawnEntity(entity);
    }

    public void tick() {
        //this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.getWorld().getTime());
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
        float waterHeight = 0.0f;
        BlockPos blockPos = getBlockPos();
        FluidState fluidState = getWorld().getFluidState(blockPos);
        if (fluidState.isIn(FluidTags.WATER)) {
            waterHeight = fluidState.getHeight(getWorld(), blockPos);
        }
        boolean inWater = waterHeight > 0.0f;
        if (!bobbing && inWater) {
            setBobbing();
            return;
        }
        if (bobbing) {
            Vec3d veclocity = this.getVelocity();
            double d = this.getY() + veclocity.y - (blockPos.getY() + waterHeight);
            if (Math.abs(d) < 0.01) {
                d += Math.signum(d) * 0.1;
            }
            setVelocity(veclocity.x * 0.9, veclocity.y - d * random.nextFloat() * 0.2, veclocity.z * 0.9);
        }
        /*if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
            this.inOpenWater = true;
        } else {
            this.inOpenWater = this.inOpenWater && this.outOfOpenWaterTicks < 10 && this.isOpenOrWaterAround(blockPos);
        }

        if (bl) {
            this.outOfOpenWaterTicks = Math.max(0, this.outOfOpenWaterTicks - 1);
            if (this.caughtFish) {
                this.setVelocity(this.getVelocity().add(0.0, -0.1 * (double)this.velocityRandom.nextFloat() * (double)this.velocityRandom.nextFloat(), 0.0));
            }

            if (!this.getWorld().isClient) {
                this.tickFishingLogic(blockPos);
            }
        } else {
            this.outOfOpenWaterTicks = Math.min(10, this.outOfOpenWaterTicks + 1);
        }*/

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

    // Returns how much damage rod should take
    public int use(ItemStack usedItem) {
        PlayerEntity player = getPlayerOwner();
        if (getWorld().isClient() || player == null || removeIfInvalid(player)) {
            return 0;
        }
        int damage = 0;
        // TODO: Actual logic here
        if (isOnGround()) {
            damage = 2;
        }
        discard();
        return damage;
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

    /*@Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
    }*/

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

    public MinigameRodTier getTier() {
        return EnumUtils.fromInteger(getDataTracker().get(TIER), MinigameRodTier.class);
    }

    public void setTier(int tier) {
        getDataTracker().set(TIER, tier);
    }

    public void setTier(MinigameRodTier tier) {
        setTier(tier.ordinal());
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
