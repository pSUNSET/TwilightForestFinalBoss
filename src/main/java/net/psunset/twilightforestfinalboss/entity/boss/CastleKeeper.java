package net.psunset.twilightforestfinalboss.entity.boss;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.Vec3;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.procedure.CastleKeeperOnEntityTickUpdateProcedure;
import net.psunset.twilightforestfinalboss.tool.ActionUtl;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;
import twilightforest.TwilightForestMod;
import twilightforest.data.tags.DamageTypeTagGenerator;
import twilightforest.entity.boss.BaseTFBoss;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructures;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class CastleKeeper extends BaseTFBoss implements GeoEntity {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> MID_HP_PHASE = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.INT);
    private final Set<ServerPlayer> playersWithCredit = Sets.newHashSet();
    private final AnimatableInstanceCache cache;
    public String animationprocedure;
    String prevAnim;

    public CastleKeeper(EntityType<? extends CastleKeeper> type, Level world) {
        super(type, world);
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.animationprocedure = "empty";
        this.prevAnim = "empty";
        this.xpReward = 999;
        this.setNoAi(false);
        this.setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHOOT, false);
        builder.define(ANIMATION, "undefined");
        builder.define(TEXTURE, "castle_keeper");
        builder.define(MID_HP_PHASE, 0);
    }

    public void setTexture(String texture) {
        this.entityData.set(TEXTURE, texture);
    }

    public String getTexture() {
        return this.entityData.get(TEXTURE);
    }

    public int getMidHPPhase() {
        return this.entityData.get(MID_HP_PHASE);
    }

    public void progressMidHPPhase() {
        entityData.set(MID_HP_PHASE, entityData.get(MID_HP_PHASE) + 1);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
//        return NetworkHooks.getEntitySpawningPacket(this);
        return super.getAddEntityPacket(entity);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0F));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean toReturn = super.hurt(source, amount);

        if (toReturn) {
            if (!level().isClientSide()) {
                if (source.getEntity() instanceof ServerPlayer player) {
                    playersWithCredit.add(player);
                }

                level().playSound(null, blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.blaze.hurt")), SoundSource.MASTER, 4.0F, -2.0F);

                if (random.nextDouble() < 0.3) {
                    spawnOther(TFEntities.HARBINGER_CUBE.get(), null);
                }

                if (random.nextDouble() < 0.3) {
                    spawnOther(TFEntities.ADHERENT.get(), null);
                }
            } else {
                level().playLocalSound(getX(), getY(), getZ(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.blaze.hurt")), SoundSource.MASTER, 4.0F, -2.0F, false);
            }
        }

        return toReturn;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return !(source.getEntity() instanceof Player) || super.isInvulnerableTo(source);
    }

    @Override
    public void die(DamageSource source) {
        // Posts advancement done
//        if (source.getEntity() instanceof ServerPlayer player) {
//            AdvancementHolder adv = player.getServer().getAdvancements().get(RLUtl.of("a_tussel_with_calamity"));
//            AdvancementProgress advProgress = player.getAdvancements().getOrStartProgress(adv);
//            if (!advProgress.isDone()) {
//                for(String criteria : advProgress.getRemainingCriteria()) {
//                    player.getAdvancements().award(adv, criteria);
//                }
//            }
//        }

        super.die(source);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (this.level().getDifficulty() != Difficulty.EASY && this.getAttribute(Attributes.MAX_HEALTH) != null) {
            boolean hard = this.level().getDifficulty() == Difficulty.HARD;
            AttributeModifier modifier = new AttributeModifier(TwilightForestMod.prefix("difficulty_health_boost"), hard ? 100 : 60, AttributeModifier.Operation.ADD_VALUE);
            if (!Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).hasModifier(modifier.id())) {
                Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(modifier);
                this.setHealth(this.getMaxHealth());
            }
        }
        getPersistentData().putInt("CastleKeeperAttack", 0);
        return spawnGroupData;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Texture", this.getTexture());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Texture")) {
            this.setTexture(compound.getString("Texture"));
        }
    }

    @Override
    public void tick() {
        super.tick();
        String animation = getSyncedAnimation();
        if (!animation.equals("undefined")) {
            setAnimation("undefined");
            animationprocedure = animation;
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        CastleKeeperOnEntityTickUpdateProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
        this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).scale(1.0F);
    }

    @Override
    public boolean canChangeDimensions(Level oldLevel, Level newLevel) {
        return false;
    }

    @Override
    public BossEvent.BossBarOverlay getBossBarOverlay() {
        return BossEvent.BossBarOverlay.NOTCHED_20;
    }

    @Override
    public int getBossBarColor() {
        return 0xDD2233;
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.MAX_HEALTH, 500.0F)
                .add(Attributes.ARMOR, 25.0F)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 4.0F)
                .add(Attributes.ATTACK_DAMAGE, 6.0F)
                .add(Attributes.FOLLOW_RANGE, 64.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 999.0F)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0F)
                .add(Attributes.STEP_HEIGHT, 1.6D);
    }

    private PlayState movementPredicate(AnimationState event) {
        if (this.animationprocedure.equals("empty")) {
            if (!event.isMoving() && event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) {
                return this.isDeadOrDying() ? event.setAndContinue(RawAnimation.begin().thenPlay("death")) : event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
            } else {
                return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
            }
        } else {
            return PlayState.STOP;
        }
    }

    private PlayState procedurePredicate(AnimationState event) {
        if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || !this.animationprocedure.equals(this.prevAnim) && !this.animationprocedure.equals("empty")) {
            if (!this.animationprocedure.equals(this.prevAnim)) {
                event.getController().forceAnimationReset();
            }

            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationprocedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (this.animationprocedure.equals("empty")) {
            this.prevAnim = "empty";
            return PlayState.STOP;
        }

        this.prevAnim = this.animationprocedure;
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (!this.isRemoved()) {
            if (!this.level().isClientSide()) {
                if (this.isDeathAnimationFinished()) {
                    this.level().broadcastEntityEvent(this, (byte) 60); // makePoofParticles()
                    this.remove(RemovalReason.KILLED);
                    ((ServerLevel) level()).sendParticles(ParticleTypes.FLASH, getX(), getY(), getZ(), 50, 0.5F, 0.5F, 0.5F, 0.0F);
                    ((ServerLevel) level()).sendParticles(ParticleTypes.CLOUD, getX(), getY(), getZ(), 50, 0.5F, 0.5F, 0.5F, 0.0F);
                } else {
                    this.tickBossBar();
                }
            } else {
                // using geckolib
//                this.tickDeathAnimation();
            }
        }
    }

    public String getSyncedAnimation() {
        return this.entityData.get(ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(ANIMATION, animation);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController[]{new AnimationController(this, "movement", 0, this::movementPredicate)});
        data.add(new AnimationController[]{new AnimationController(this, "procedure", 0, this::procedurePredicate)});
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void delayServerAction(int tick, Consumer<CastleKeeper> action) {
        ActionUtl.delayInServer(tick, () -> action.accept(this));
    }

    @Override
    public ResourceKey<Structure> getHomeStructure() {
        return TFStructures.FINAL_CASTLE;
    }

    @Override
    public Block getDeathContainer(RandomSource random) {
        return TFBlocks.MINING_CHEST.get();
    }

    @Override
    public Block getBossSpawner() {
        return TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get();
    }

    @Override
    public int getHomeRadius() {
        return 20;
    }

    @Override
    public boolean isDeathAnimationFinished() {
        return deathTime >= 200;
    }

    public <T extends Mob> void spawnOther(@Nullable EntityType<T> typeToSpawn) {
        spawnOther(typeToSpawn, null);
    }

    /**
     * The method to find good position to spawn copies of {@link Lich#findVecInLOSOf(Entity)}.
     * But returns its own position when nothing is appropriate.
     */
    public <T extends Mob> void spawnOther(@Nullable EntityType<T> typeToSpawn, @Nullable Consumer<T> beforeSpawn) {
        T entityToSpawn = typeToSpawn == null ? null : typeToSpawn.create(this.level());
        if (entityToSpawn == null) return;
        entityToSpawn.setPos(position());
        double origX = this.getX();
        double origY = this.getY();
        double origZ = this.getZ();

        int tries = 100;
        Vec3 tpPos = null;
        for (int i = 0; i < tries; i++) {
            // we abuse LivingEntity.attemptTeleport, which does all the collision/ground checking for us, then teleport back to our original spot
            double tx = entityToSpawn.getX() + this.getRandom().nextGaussian() * 16D;
            double ty = entityToSpawn.getY() + 2;
            double tz = entityToSpawn.getZ() + this.getRandom().nextGaussian() * 16D;

            boolean destClear = this.randomTeleport(tx, ty, tz, false);
            if (destClear) {
                tx = this.getX();
                ty = this.getY();
                tz = this.getZ();
            }
            boolean canSeeTargetAtDest = this.hasLineOfSight(entityToSpawn); // Don't use senses cache because we're in a temporary position
            this.teleportTo(origX, origY, origZ);

            if (i < 85 && ty < entityToSpawn.getY()) continue; // Try to not TP below the playa
            if (destClear && canSeeTargetAtDest && entityToSpawn.position().distanceToSqr(tx, ty, tz) >= 25.0F) {
                tpPos = new Vec3(tx, ty, tz);
            }
        }

        if (tpPos == null) return;
        if (beforeSpawn != null) beforeSpawn.accept(entityToSpawn);
        entityToSpawn.moveTo(tpPos);
        entityToSpawn.setTarget(getTarget());
        level().addFreshEntity(entityToSpawn);
    }
}
