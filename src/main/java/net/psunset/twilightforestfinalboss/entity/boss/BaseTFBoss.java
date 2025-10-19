package net.psunset.twilightforestfinalboss.entity.boss;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFConfig;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.boss.IBossLootBuffer;
import twilightforest.loot.TFLootTables;
import twilightforest.util.EntityUtil;
import twilightforest.util.LandmarkUtil;

import java.util.Objects;
import java.util.Optional;

/**
 * Copy from <a href="https://github.com/TeamTwilight/twilightforest/blob/1.21.1/src/main/java/twilightforest/entity/boss/BaseTFBoss.java">Twilight Forest Repo</a>
 */
public abstract class BaseTFBoss extends Monster implements IBossLootBuffer, EnforcedHomePoint {
    private static final EntityDataAccessor<Optional<GlobalPos>> HOME_POINT = SynchedEntityData.defineId(BaseTFBoss.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);

    private final ServerBossEvent bossEvent;
    private final NonNullList<ItemStack> dyingInventory = NonNullList.withSize(27, ItemStack.EMPTY);

    protected BaseTFBoss(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.bossEvent = this.createBossBar();
    }

    public abstract ResourceKey<Structure> getHomeStructure();

    public abstract Block getDeathContainer(RandomSource random);

    public abstract Block getBossSpawner();

    protected boolean shouldSpawnLoot() {
        return this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
    }

    protected boolean shouldCreateSpawner() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(HOME_POINT, Optional.empty());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.getBossBar().addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.getBossBar().removePlayer(player);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        this.saveHomePointToNbt(compound);
        this.addDeathItemsSaveData(compound);
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readDeathItemsSaveData(compound);
        this.loadHomePointFromNbt(compound);
    }

    @Override
    public void lavaHurt() {
        if (!this.fireImmune()) {
            this.setSecondsOnFire(5);
            if (this.hurt(this.damageSources().lava(), 4.0F)) {
                this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.getRandom().nextFloat() * 0.4F);
                AABB bounds = this.getBoundingBox().inflate(9D);
                for (double x = bounds.minX; x < bounds.maxX; x++) {
                    for (double z = bounds.minZ; z < bounds.maxZ; z++) {
                        for (double y = bounds.minY; y < bounds.maxY; y++) {
                            BlockPos pos = BlockPos.containing(x, y, z);
                            BlockState state = this.level().getBlockState(pos);
                            if (state.is(Blocks.LAVA)) {
                                this.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (this.shouldSpawnLoot() && this.level() instanceof ServerLevel server) this.postmortem(server, cause);
    }

    // mark the boss structure as conquered, separate method, so it can be overridden
    protected void postmortem(ServerLevel serverLevel, DamageSource cause) {
        this.getBossBar().setProgress(0.0F);
        IBossLootBuffer.saveDropsIntoBoss(this, TFLootTables.createLootParams(this, true, cause).create(LootContextParamSets.ENTITY), serverLevel);
        LandmarkUtil.markStructureConquered(serverLevel, this, this.getHomeStructure(), true);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.level() instanceof ServerLevel serverLevel) this.postRemoval(serverLevel, reason);
        super.remove(reason);
    }

    // drop loot into a chest after removal, separate method, so it can be overridden
    protected void postRemoval(ServerLevel serverLevel, RemovalReason reason) {
        if (reason.equals(RemovalReason.KILLED) && this.shouldSpawnLoot()) {
            IBossLootBuffer.depositDropsIntoChest(this, this.getDeathContainer(this.getRandom()).defaultBlockState().setValue(ChestBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(this.level().getRandom())), EntityUtil.bossChestLocation(this), serverLevel);
        }
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL) {
            if (this.shouldCreateSpawner() && this.isRestrictionPointValid(this.level().dimension()) && this.level().isLoaded(Objects.requireNonNull(this.getRestrictionPoint()).pos())) {
                this.placeSpawner(this.getRestrictionPoint().pos());
            }
            this.discard();
        } else {
            super.checkDespawn();
        }
    }

    //Separate method, cuz Lich needs it
    public void placeSpawner(BlockPos pos) {
        this.level().setBlockAndUpdate(pos, this.getBossSpawner().defaultBlockState());
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected boolean shouldDropLoot() {
        return !TFConfig.COMMON_CONFIG.bossDropChests.get();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

//    @Override
//    public boolean canUsePortal(boolean force) {
//        return false;
//    }

    @Override
    public @Nullable GlobalPos getRestrictionPoint() {
        return this.getEntityData().get(HOME_POINT).orElse(null);
    }

    @Override
    public void setRestrictionPoint(@Nullable GlobalPos pos) {
        this.getEntityData().set(HOME_POINT, Optional.ofNullable(pos));
    }

    @Override
    public NonNullList<ItemStack> getItemStacks() {
        return this.dyingInventory;
    }

    @Override
    protected void tickDeath() {
        this.deathTime++;
        if (!this.isRemoved()) {
            if (!this.level().isClientSide()) {
                if (this.isDeathAnimationFinished()) {
                    this.level().broadcastEntityEvent(this, (byte) 60); // makePoofParticles()
                    this.remove(RemovalReason.KILLED);
                } else this.tickBossBar();
            } else this.tickDeathAnimation();
        }
    }

    public boolean isDeathAnimationFinished() {
        return this.deathTime >= 20;
    }

    public void tickDeathAnimation() {

    }

    public ServerBossEvent getBossBar() {
        return this.bossEvent;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!this.level().isClientSide()) this.tickBossBar();
    }

    protected void tickBossBar() {
        this.getBossBar().setProgress(this.getHealth() / this.getMaxHealth());
    }

    protected ServerBossEvent createBossBar() {
        return new ServerBossEvent(this.getBossBarTitle(), this.getBossBarColor(), this.getBossBarOverlay());
    }

    public Component getBossBarTitle() {
        return this.getDisplayName() != null ? this.getDisplayName() : this.getTypeName();
    }

    public abstract BossEvent.BossBarColor getBossBarColor();

    public BossEvent.BossBarOverlay getBossBarOverlay() {
        return BossEvent.BossBarOverlay.PROGRESS;
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getBossBarTitle());
    }
}