package net.psunset.twilightforestfinalboss.entity.boss;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;
import net.psunset.twilightforestfinalboss.tool.ActionUtl;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;
import twilightforest.TwilightForestMod;
import twilightforest.entity.boss.BaseTFBoss;
import twilightforest.entity.boss.Lich;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFEntities;
import twilightforest.init.TFStructures;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CastleKeeper extends BaseTFBoss implements GeoEntity {
    public static final EntityDataAccessor<String> DATA_ANIMATION = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> DATA_TEXTURE = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> DATA_HP_PHASE = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_SHOOT_CD = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.INT); // ATTACK_COOLDOWN
    public static final EntityDataAccessor<Integer> DATA_SWING_CD = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.INT); // ATTACK_COOLDOWN
    public static final EntityDataAccessor<Integer> DATA_STOMP_CD = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.INT); // ATTACK_COOLDOWN
    public static final EntityDataAccessor<Integer> DATA_SPOUT_CD = SynchedEntityData.defineId(CastleKeeper.class, EntityDataSerializers.INT); // ATTACK_COOLDOWN
    public static final Map<BaseTFBoss, CastleKeeper> childToParent = Maps.newHashMap();
    private final AnimatableInstanceCache cache;
    public String animation;
    String oAnimation;

    public CastleKeeper(EntityType<? extends CastleKeeper> type, Level world) {
        super(type, world);
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.animation = "empty";
        this.oAnimation = "empty";
        this.xpReward = 999;
        this.setNoAi(false);
        this.setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ANIMATION, "undefined");
        builder.define(DATA_TEXTURE, "castle_keeper");
        builder.define(DATA_HP_PHASE, 0);
        builder.define(DATA_SHOOT_CD, 0);
        builder.define(DATA_SWING_CD, 0);
        builder.define(DATA_STOMP_CD, 0);
        builder.define(DATA_SPOUT_CD, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new CastleKeeperStompAttackGoal(this));
        this.goalSelector.addGoal(3, new CastleKeeperSwingAttackGoal(this));
        this.goalSelector.addGoal(3, new CastleKeeperSpoutAttackGoal(this));
        this.goalSelector.addGoal(3, new CastleKeeperShootAttackGoal(this));
        this.goalSelector.addGoal(4, new MoveTowardsTargetGoal(this, 1.0F, 999.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, false));
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

    public void setTexture(String texture) {
        this.entityData.set(DATA_TEXTURE, texture);
    }

    public String getTexture() {
        return this.entityData.get(DATA_TEXTURE);
    }

    public int getMidHPPhase() {
        return this.entityData.get(DATA_HP_PHASE);
    }

    public void progressMidHPPhase() {
        entityData.set(DATA_HP_PHASE, entityData.get(DATA_HP_PHASE) + 1);
    }

    private void progressAttackCooldown() {
        int cooldown = this.entityData.get(DATA_SHOOT_CD);
        if (cooldown > 0) {
            this.entityData.set(DATA_SHOOT_CD, cooldown - 1);
        }
        cooldown = this.entityData.get(DATA_SWING_CD);
        if (cooldown > 0) {
            this.entityData.set(DATA_SWING_CD, cooldown - 1);
        }
        cooldown = this.entityData.get(DATA_STOMP_CD);
        if (cooldown > 0) {
            this.entityData.set(DATA_STOMP_CD, cooldown - 1);
        }
        cooldown = this.entityData.get(DATA_SPOUT_CD);
        if (cooldown > 0) {
            this.entityData.set(DATA_SPOUT_CD, cooldown - 1);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
//        return NetworkHooks.getEntitySpawningPacket(this);
        return super.getAddEntityPacket(entity);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (childToParent.values().stream().anyMatch(it -> it.equals(this))) return false;
        if (getMidHPPhase() >= 2) {
            Entity entity = source.getDirectEntity();
            if (entity instanceof AbstractArrow || entity instanceof WindCharge) {
                return false;
            }
        }
        boolean toReturn = super.hurt(source, amount);

        if (toReturn) {
            level().playSound(null, blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.blaze.hurt")), SoundSource.MASTER, 4.0F, -2.0F);

            if (random.nextDouble() < 0.5) {
                spawnNearby(TFEntities.HARBINGER_CUBE.get());
            }

            if (random.nextDouble() < 0.5) {
                spawnNearby(TFEntities.ADHERENT.get());
            }

            float health = getHealth();
            int difficulty = level().getDifficulty().getId();

            if (health <= 400.0F && getMidHPPhase() < 1) {
                progressMidHPPhase();

                delayServerAction(20, it -> {
                    it.spawnNearby(TFEntities.NAGA.get(), child -> {
                        child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                        childToParent.put(child, CastleKeeper.this);
                    });

                    it.delayServerAction(20, _it -> {
                        _it.spawnNearby(TFEntities.NAGA.get(), child -> {
                            child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                            childToParent.put(child, CastleKeeper.this);
                        });
                    });
                });

                if (difficulty >= 2) {
                    delayServerAction(20, it -> {
                        for (int i = 0; i < 2; ++i) {
                            it.spawnNearby(TFEntities.MIST_WOLF.get());
                        }
                        it.delayServerAction(20, _it -> {
                            for (int j = 0; j < 2; ++j) {
                                _it.spawnNearby(TFEntities.KING_SPIDER.get());
                            }
                        });
                    });
                }

            } else if (health <= 300.0F && getMidHPPhase() < 2) {
                progressMidHPPhase();
                setTexture("castle_keeper_damage1");

                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level());
                lightning.moveTo(Vec3.atBottomCenterOf(blockPosition()));
                lightning.setVisualOnly(true);
                level().addFreshEntity(lightning);

                delayServerAction(20, it -> {
                    it.spawnNearby(TFEntities.LICH.get(), child -> {
                        child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                        childToParent.put(child, CastleKeeper.this);
                    });
                    delayServerAction(20, _it -> {
                        _it.spawnNearby(TFEntities.ARMORED_GIANT.get());
                        _it.spawnNearby(TFEntities.GIANT_MINER.get());
                    });
                });

                if (difficulty >= 2) {
                    delayServerAction(20, it -> {
                        it.spawnNearby(TFEntities.ARMORED_GIANT.get());
                        it.delayServerAction(20, _it -> {
                            _it.spawnNearby(TFEntities.GIANT_MINER.get());
                        });
                    });
                }

                level().playSound(null, blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("block.anvil.destroy")), SoundSource.MASTER, 6.0F, 1.0F);

                level().levelEvent(2001, BlockPos.containing(getX(), getY() + (double) 2.5F, getZ()), Block.getId(Blocks.BAMBOO_BLOCK.defaultBlockState()));

            } else if (health <= 200.0F && getMidHPPhase() < 3) {
                progressMidHPPhase();
                delayServerAction(20, it -> {
                    it.spawnNearby(TFEntities.SNOW_QUEEN.get(), child -> {
                        child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                        childToParent.put(child, CastleKeeper.this);
                    });
                });

                if (difficulty >= 2) {
                    delayServerAction(20, it -> {
                        it.spawnNearby(TFEntities.ALPHA_YETI.get(), child -> {
                            child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                            childToParent.put(child, CastleKeeper.this);
                        });
                    });
                }

            } else if (health <= 100.0F && getMidHPPhase() < 4) {
                progressMidHPPhase();
                setTexture("castle_keeper_damage2");

                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level());
                lightning.moveTo(Vec3.atBottomCenterOf(blockPosition()));
                lightning.setVisualOnly(true);
                level().addFreshEntity(lightning);

                delayServerAction(20, it -> {
                    it.spawnNearby(TFEntities.HYDRA.get(), child -> {
                        child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                        childToParent.put(child, CastleKeeper.this);
                    });
                    it.delayServerAction(20, _it -> {
                        _it.spawnNearby(TFEntities.UR_GHAST.get(), child -> {
                            child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                            childToParent.put(child, CastleKeeper.this);
                        });
                    });
                });

                if (difficulty >= 2) {
                    delayServerAction(20, it -> {
                        it.spawnNearby(TFEntities.MINOSHROOM.get(), child -> {
                            child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                            childToParent.put(child, CastleKeeper.this);
                        });
                        it.delayServerAction(20, _it -> {
                            for (int i = 0; i < 6; ++i) {
                                _it.spawnNearby(TFEntities.KNIGHT_PHANTOM.get(), child -> {
                                    child.setRestrictionPoint(GlobalPos.of(child.level().dimension(), child.blockPosition()));
                                    childToParent.put(child, CastleKeeper.this);
                                });
                            }
                        });
                    });
                }

                level().playSound(null, blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("block.anvil.destroy")), SoundSource.MASTER, 6.0F, 1.0F);

                level().levelEvent(2001, BlockPos.containing(getX(), getY() + 3.2, getZ()), Block.getId(Blocks.GREEN_CONCRETE.defaultBlockState()));
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
        progressAttackCooldown();
        String animation = getAnimation();
        if (!animation.equals("undefined")) {
            setAnimation("undefined");
            this.animation = animation;
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
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
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    public BossEvent.BossBarOverlay getBossBarOverlay() {
        return BossEvent.BossBarOverlay.NOTCHED_20;
    }

    @Override
    public int getBossBarColor() {
        return 0xDD2233;
    }

    @Override
    public void die(DamageSource cause) {
        getPersistentData().putDouble("CastleKeeperAttack", 0.0F);
        if (!level().isClientSide()) {
            ((ServerLevel) level()).sendParticles(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 10, 0.4, 1.0F, 0.4, 0.02);

            AbstractArrow arrow = TFFBEntities.ESCAPING_SOUL.get().create(level());
            arrow.setBaseDamage(5.0F);
//                arrow.setKnockback(1);
            arrow.moveTo(position());
            arrow.shoot(Mth.nextDouble(random, -0.4, 0.4), 3.5F, Mth.nextDouble(random, -0.4, 0.4), 2.0F, 0.0F);
            level().addFreshEntity(arrow);
        }
        super.die(cause);
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

    private PlayState movementPredicate(AnimationState event) {
        if (this.animation.equals("empty")) {
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
        if (!this.animation.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || !this.animation.equals(this.oAnimation) && !this.animation.equals("empty")) {
            if (!this.animation.equals(this.oAnimation)) {
                event.getController().forceAnimationReset();
            }

            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animation));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animation = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (this.animation.equals("empty")) {
            this.oAnimation = "empty";
            return PlayState.STOP;
        }

        this.oAnimation = this.animation;
        return PlayState.CONTINUE;
    }

    public String getAnimation() {
        return this.entityData.get(DATA_ANIMATION);
    }

    public void setAnimation(String animation) {
        this.entityData.set(DATA_ANIMATION, animation);
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

    @Nullable
    public <T extends Mob> T spawnNearby(EntityType<T> typeToSpawn) {
        return spawnNearby(typeToSpawn, null);
    }

    /**
     * The method to find good position to spawn copies of {@link Lich#findVecInLOSOf(Entity)}.
     */
    @Nullable
    public <T extends Mob> T spawnNearby(EntityType<T> typeToSpawn, @Nullable Consumer<T> spawnCallback) {
        T entityToSpawn = typeToSpawn.create(this.level());
        if (entityToSpawn == null) return null;
        entityToSpawn.setPos(position());
        double origX = this.getX();
        double origY = this.getY();
        double origZ = this.getZ();

        int tries = 100;
        Vec3 tpPos = position();
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

        entityToSpawn.moveTo(tpPos);
        entityToSpawn.setTarget(getTarget());
        entityToSpawn.targetSelector.removeAllGoals(it -> true);
        entityToSpawn.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(entityToSpawn, Player.class, false));
        if (spawnCallback != null) spawnCallback.accept(entityToSpawn);
        level().addFreshEntity(entityToSpawn);
        return entityToSpawn;
    }

    @Nullable
    public <T extends AbstractHurtingProjectile> T shoot(AbstractHurtingProjectileFactory<T> factory) {
        return shoot(factory, null);
    }

    @Nullable
    public <T extends AbstractHurtingProjectile> T shoot(AbstractHurtingProjectileFactory<T> factory, @Nullable Consumer<T> spawnCallback) {
        Vec3 start = getTarget() != null ?
                getTarget().position().add(Mth.nextInt(random, -8, 8), 10.0, Mth.nextInt(random, -8, 8)) :
                new Vec3(getX() + Mth.nextInt(random, -20, 20), getEyeY() + 10.0, getZ() + Mth.nextInt(random, -20, 20));
        Vec3 movement = new Vec3(random.nextGaussian() * 0.01, -10.0, random.nextGaussian() * 0.01).normalize();
        T projectile = factory.create(level(), this, movement);
        if (projectile == null) return null;
        projectile.setPosRaw(start.x, start.y, start.z);
        if (spawnCallback != null) spawnCallback.accept(projectile);
        level().addFreshEntity(projectile);
        return projectile;
    }

    @FunctionalInterface
    public interface AbstractHurtingProjectileFactory<T extends AbstractHurtingProjectile> {
        T create(Level level, LivingEntity owner, Vec3 movement);
    }

    static class CastleKeeperShootAttackGoal extends Goal {
        private final CastleKeeper keeper;
        public int attackTimer;
        public final int ATTACK_DURATION = 40;
        public final int EXTRA_COOLDOWN = 95;

        protected CastleKeeperShootAttackGoal(CastleKeeper keeper) {
            this.keeper = keeper;
        }

        @Override
        public void start() {
            attackTimer = 0;
        }

        @Override
        public void stop() {
            keeper.entityData.set(DATA_SHOOT_CD, ATTACK_DURATION - attackTimer + EXTRA_COOLDOWN);
        }

        @Override
        public boolean canUse() {
            return keeper.entityData.get(DATA_SHOOT_CD) <= 0;
        }

        @Override
        public void tick() {
            attackTimer++;
            int difficulty = keeper.level().getDifficulty().getId();

            // Start animation
            if (attackTimer == 1) {
                keeper.setAnimation("shoot");
                keeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ATTACK_DURATION, 254, true, false));

            } else if (attackTimer >= 20 && attackTimer <= ATTACK_DURATION) {
                if (attackTimer % (10 - (difficulty * 2)) == 0) {
                    keeper.shoot((_level, _owner, _movement) -> new LargeFireball(_level, _owner, _movement, keeper.getRandom().nextInt(1, 4)), it -> it.setRemainingFireTicks(99999));

                    for (int i = 0; i < 5; i++) {
                        keeper.shoot(SmallFireball::new, it -> it.setRemainingFireTicks(99999));
                    }

                    if (difficulty >= 2) {
                        keeper.shoot(DragonFireball::new);
                    }

                    if (difficulty >= 3) {
                        keeper.shoot(WitherSkull::new);
                    }

                    keeper.level().playSound(null, keeper.blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.ghast.shoot")), SoundSource.MASTER, 2.0F, 1.0F);
                }

            } else if (attackTimer > ATTACK_DURATION) {
                attackTimer = -EXTRA_COOLDOWN;
            }
        }
    }

    static class CastleKeeperSwingAttackGoal extends Goal {
        private final CastleKeeper keeper;
        public int attackTimer;
        public final int ATTACK_DURATION = 25;
        public final int EXTRA_COOLDOWN = 55;

        protected CastleKeeperSwingAttackGoal(CastleKeeper keeper) {
            this.keeper = keeper;
        }

        @Override
        public void start() {
            attackTimer = 0;
        }

        @Override
        public void stop() {
            keeper.entityData.set(DATA_SWING_CD, ATTACK_DURATION - attackTimer + EXTRA_COOLDOWN);
        }

        @Override
        public boolean canUse() {
            if (keeper.getTarget() == null) return false;
            Vec3 pos = keeper.position();
            Vec3 targetPos = keeper.getTarget().position();
            return keeper.entityData.get(DATA_SWING_CD) <= 0 && targetPos.subtract(pos).horizontalDistanceSqr() < 9.0D && targetPos.distanceToSqr(pos) < 18.0D && keeper.hasLineOfSight(keeper.getTarget());
        }

        @Override
        public void tick() {
            attackTimer++;

            // Start animation
            if (attackTimer == 1) {
                keeper.setAnimation("swing");
                keeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ATTACK_DURATION, 254, true, false));

            } else if (attackTimer > ATTACK_DURATION) {
                int difficulty = keeper.level().getDifficulty().getId();

                for (int i = 0; i < 100; i++) {
                    keeper.delayServerAction(Mth.nextInt(keeper.getRandom(), 0, 8), it -> {
                        Vec3 center = new Vec3(it.level().clip(new ClipContext(it.getEyePosition(1.0F), it.getEyePosition(1.0F).add(it.getViewVector(1.0F).scale(Mth.nextInt(it.getRandom(), 2, 5) + difficulty)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, it)).getBlockPos().getX(), it.getY(), it.level().clip(new ClipContext(it.getEyePosition(1.0F), it.getEyePosition(1.0F).add(it.getViewVector(1.0F).scale(Mth.nextInt(it.getRandom(), 2, 5) + difficulty)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, it)).getBlockPos().getZ());

                        for (Entity entityInside : it.level().getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate(1.0F), (e) -> true).stream().sorted(Comparator.comparingDouble(_it -> _it.distanceToSqr(center))).toList()) {
                            if (entityInside != it) {
                                entityInside.hurt(new DamageSource(it.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), it), 8.0F + difficulty);
                            }
                        }
                    });
                }

                keeper.level().playSound(null, keeper.blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.camel.dash")), SoundSource.MASTER, 2.0F, 1.0F);

                attackTimer = -EXTRA_COOLDOWN;
            }
        }
    }

    static class CastleKeeperStompAttackGoal extends Goal {
        private final CastleKeeper keeper;
        public int attackTimer;
        public final int ATTACK_DURATION = 25;
        public final int EXTRA_COOLDOWN = 60;

        protected CastleKeeperStompAttackGoal(CastleKeeper keeper) {
            this.keeper = keeper;
        }

        @Override
        public void start() {
            attackTimer = 0;
        }

        @Override
        public void stop() {
            keeper.entityData.set(DATA_STOMP_CD, ATTACK_DURATION - attackTimer + EXTRA_COOLDOWN);
        }

        @Override
        public boolean canUse() {
            if (keeper.getTarget() == null) return false;
            Vec3 pos = keeper.position();
            Vec3 targetPos = keeper.getTarget().position();
            return keeper.entityData.get(DATA_STOMP_CD) <= 0 && targetPos.subtract(pos).horizontalDistanceSqr() < 25.0D && targetPos.distanceToSqr(pos) < 50.0D && keeper.hasLineOfSight(keeper.getTarget());
        }

        @Override
        public void tick() {
            attackTimer++;

            // Start animation
            if (attackTimer == 1) {
                keeper.setAnimation("stomp");
                keeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ATTACK_DURATION, 254, true, false));

            } else if (attackTimer > ATTACK_DURATION) {
                int difficulty = keeper.level().getDifficulty().getId();

                for (int i = 0; i < 50; ++i) {
                    keeper.level().levelEvent(2001, keeper.blockPosition().offset(Mth.nextInt(keeper.getRandom(), -5, 5), -1, Mth.nextInt(keeper.getRandom(), -5, 5)), Block.getId(keeper.level().getBlockState(keeper.blockPosition().below())));
                }

                for (Entity entityInside : keeper.level().getEntitiesOfClass(Entity.class, (new AABB(keeper.position(), keeper.position())).inflate(4.0F + (difficulty / 2.0F)), (e) -> true).stream().sorted(Comparator.comparingDouble(it -> it.distanceToSqr(keeper.position()))).toList()) {
                    if (entityInside.getType().getCategory().isFriendly()) {
                        entityInside.hurt(new DamageSource(keeper.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), keeper), 9.0F + difficulty);
                        entityInside.push(new Vec3(entityInside.position().x - keeper.position().x, 5.0, entityInside.position().z - keeper.position().z).normalize().multiply(2.0F, 2.5F, 2.0F));
                    }
                }

                keeper.level().playSound(null, keeper.blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.dragon_fireball.explode")), SoundSource.MASTER, 2.0F, -1.0F);

                attackTimer = -EXTRA_COOLDOWN;
            }
        }
    }

    static class CastleKeeperSpoutAttackGoal extends Goal {
        private final CastleKeeper keeper;
        public int attackTimer;
        public final int ATTACK_DURATION = 30;
        public final int EXTRA_COOLDOWN = 70;

        protected CastleKeeperSpoutAttackGoal(CastleKeeper keeper) {
            this.keeper = keeper;
        }

        @Override
        public void start() {
            attackTimer = 0;
        }

        @Override
        public void stop() {
            keeper.entityData.set(DATA_SPOUT_CD, ATTACK_DURATION - attackTimer + EXTRA_COOLDOWN);
        }

        @Override
        public boolean canUse() {
            if (keeper.getTarget() == null) return false;
            Vec3 pos = keeper.position();
            Vec3 targetPos = keeper.getTarget().position();
            return keeper.entityData.get(DATA_STOMP_CD) <= 0 && targetPos.subtract(pos).horizontalDistanceSqr() < 64.0D && targetPos.distanceToSqr(pos) < 128.0D && keeper.hasLineOfSight(keeper.getTarget());
        }

        @Override
        public void tick() {
            attackTimer++;

            // Start animation
            if (attackTimer == 1) {
                keeper.setAnimation("spout");
                keeper.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, ATTACK_DURATION, 254, true, false));

            } else if (attackTimer > 10 && attackTimer <= ATTACK_DURATION) {

                AbstractArrow arrow;
                for (int i = 0; i < 5; i++) {
                    arrow = TFFBEntities.LOBBED_FIREBALL.get().create(keeper.level());
                    arrow.setOwner(keeper);
                    arrow.setBaseDamage(10.0F);
//                arrow.setKnockback(1);
                    arrow.setSilent(true);
                    arrow.setRemainingFireTicks(2000); // 100 secs
                    arrow.moveTo(keeper.getX(), keeper.getEyeY() + 1.0, keeper.getZ());
                    arrow.shoot(Mth.nextDouble(keeper.getRandom(), -1.0, 1.0), 2.0, Mth.nextDouble(keeper.getRandom(), -1.0, 1.0), 1.0F, 0.0F);
                    keeper.level().addFreshEntity(arrow);
                }

                keeper.level().playSound(null, keeper.blockPosition(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.blaze.shoot")), SoundSource.MASTER, 2.0F, 1.0F);

            } else if (attackTimer > ATTACK_DURATION) {
                attackTimer = -EXTRA_COOLDOWN;
            }
        }
    }
}
