package net.psunset.twilightforestfinalboss.procedure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;
import net.psunset.twilightforestfinalboss.tool.EntityUtl;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import org.jetbrains.annotations.NotNull;
import twilightforest.init.TFEntities;

import java.util.Comparator;


public class CastleKeeperOnEntityTickUpdateProcedure {
    /**
     * This class isn't inlined because maintain this class is a little bit harder.
     * So separate it to an independent method.
     */
    public static void execute(Level level, double x, double y, double z, @NotNull CastleKeeper entity) {
        if (entity.getTarget() != null) {
            if (entity.getPersistentData().getInt("CastleKeeperAttack") == 0) {
                entity.getPersistentData().putInt("CastleKeeperAttack", 400);
            } else {
                entity.getPersistentData().putInt("CastleKeeperAttack", entity.getPersistentData().getInt("CastleKeeperAttack") - 1);
            }

            int attackTickCountdown = entity.getPersistentData().getInt("CastleKeeperAttack");
            int difficulty = level.getDifficulty().getId();

            if (attackTickCountdown == 390) {
                entity.setAnimation("shoot");
                if (!level.isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 254, true, false));
                }

            } else if (attackTickCountdown <= 370 && attackTickCountdown >= 350 && attackTickCountdown % (10 - (difficulty * 2)) == 0) {
                if (!level.isClientSide()) {
                    EntityUtl.shootDisposableProjectile(EntityType.FIREBALL, level, entity);
                    EntityUtl.shootDisposableProjectile(EntityType.SMALL_FIREBALL, level, entity);

                    if (difficulty >= 2) {
                        EntityUtl.shootDisposableProjectile(EntityType.DRAGON_FIREBALL, level, entity);
                    }

                    if (difficulty >= 3) {
                        EntityUtl.shootDisposableProjectile(EntityType.WITHER_SKULL, level, entity);
                    }

                    level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.ghast.shoot")), SoundSource.MASTER, 2.0F, 1.0F);
                } else {
                    level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.ghast.shoot")), SoundSource.MASTER, 2.0F, 1.0F, false);
                }

            } else if (attackTickCountdown == 300) {
                entity.setAnimation("swing");
                if (!level.isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 254, true, false));
                }

            } else if (attackTickCountdown == 275) {
                if (!level.isClientSide()) {
                    for (int i = 0; i < 100; ++i) {
                        entity.delayServerAction(Mth.nextInt(level.getRandom(), 0, 8), it -> {
                            Vec3 center = new Vec3(it.level().clip(new ClipContext(it.getEyePosition(1.0F), it.getEyePosition(1.0F).add(it.getViewVector(1.0F).scale(Mth.nextInt(it.getRandom(), 2, 5) + difficulty)), Block.OUTLINE, Fluid.NONE, it)).getBlockPos().getX(), it.getY(), it.level().clip(new ClipContext(it.getEyePosition(1.0F), it.getEyePosition(1.0F).add(it.getViewVector(1.0F).scale(Mth.nextInt(it.getRandom(), 2, 5) + difficulty)), Block.OUTLINE, Fluid.NONE, it)).getBlockPos().getZ());

                            for (Entity entityInside : it.level().getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate(1.0F), (e) -> true).stream().sorted(Comparator.comparingDouble(_it -> _it.distanceToSqr(center))).toList()) {
                                if (entityInside != it) {
                                    entityInside.hurt(new DamageSource(it.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), it), 2.0F + difficulty);
                                }
                            }
                        });
                    }

                    level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.camel.dash")), SoundSource.MASTER, 2.0F, 1.0F);
                } else {
                    level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.camel.dash")), SoundSource.MASTER, 2.0F, 1.0F, false);
                }

            } else if (attackTickCountdown == 240) {
                entity.setAnimation("stomp");
                if (!level.isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 254, true, false));
                }

            } else if (attackTickCountdown == 215) {
                for (int i = 0; i < 50; ++i) {
                    level.levelEvent(2001, BlockPos.containing(x + (double) Mth.nextInt(level.getRandom(), -5, 5), y - (double) 1.0F, z + (double) Mth.nextInt(level.getRandom(), -5, 5)), net.minecraft.world.level.block.Block.getId(level.getBlockState(BlockPos.containing(x, y - (double) 1.0F, z))));
                }

                if (!level.isClientSide()) {
                    Vec3 center = new Vec3(x, y, z);

                    for (Entity entityInside : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate(4.0F + (difficulty / 2.0F)), (e) -> true).stream().sorted(Comparator.comparingDouble(it -> it.distanceToSqr(center))).toList()) {
                        if (entityInside instanceof Player) {
                            entityInside.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), entity), 9.0F + difficulty);
                            entityInside.setDeltaMovement(entity.getLookAngle().multiply(2.0F, 2.5F, 2.0F));
                        }
                    }

                    level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.dragon_fireball.explode")), SoundSource.MASTER, 2.0F, -1.0F);
                } else {
                    level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.dragon_fireball.explode")), SoundSource.MASTER, 2.0F, -1.0F, false);
                }

            } else if (attackTickCountdown == 150) {
                entity.setAnimation("spout");
                if (!level.isClientSide()) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 254, true, false));
                }

            } else if (attackTickCountdown < 140 && attackTickCountdown > 120) {
                if (!level.isClientSide()) {
                    AbstractArrow arrow = TFFBEntities.LOBBED_FIREBALL.get().create(level);
                    arrow.setOwner(entity);
                    arrow.setBaseDamage(10.0F);
//                    arrow.setKnockback(1);
                    arrow.setSilent(true);
                    arrow.setRemainingFireTicks(2000); // 100 secs
                    arrow.moveTo(x, y + 3.6, z);
                    arrow.shoot(Mth.nextDouble(level.getRandom(), -1.0, 1.0), 2.0, Mth.nextDouble(level.getRandom(), -1.0, 1.0), 1.0F, 0.0F);
                    level.addFreshEntity(arrow);

                    level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.blaze.shoot")), SoundSource.MASTER, 2.0F, 1.0F);
                } else {
                    level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.blaze.shoot")), SoundSource.MASTER, 2.0F, 1.0F, false);
                }
            }

            float healthInTime = entity.getHealth();

            if (healthInTime <= 400.0F && entity.getMidHPPhase() < 1) {
                entity.progressMidHPPhase();
                if (!level.isClientSide()) {

                    if (true) {
                        entity.delayServerAction(20, it -> {
                            it.spawnOther(TFEntities.NAGA.get(), naga -> {
                                naga.setCanPickUpLoot(false);
                            });

                            it.delayServerAction(20, _it -> {
                                _it.spawnOther(TFEntities.NAGA.get());
                            });
                        });
                    }

                    if (difficulty >= 2) {
                        entity.delayServerAction(20, it -> {
                            for (int i = 0; i < 2; ++i) {
                                it.spawnOther(TFEntities.MIST_WOLF.get());
                            }
                            it.delayServerAction(20, _it -> {
                                for (int j = 0; j < 2; ++j) {
                                    _it.spawnOther(TFEntities.KING_SPIDER.get());
                                }
                            });
                        });

                    }
                }

            } else if (healthInTime <= 300.0F && entity.getMidHPPhase() < 2) {
                entity.progressMidHPPhase();
                entity.setTexture("castle_keeper_damage1");

                if (!level.isClientSide()) {
                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                    lightning.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
                    lightning.setVisualOnly(true);
                    level.addFreshEntity(lightning);

                    entity.delayServerAction(20, it -> {
                        it.spawnOther(TFEntities.LICH.get());
                        it.delayServerAction(20, _it -> {
                            _it.spawnOther(TFEntities.LICH.get());
                        });
                    });

                    if (difficulty >= 2) {
                        entity.delayServerAction(20, it -> {
                            it.spawnOther(TFEntities.ARMORED_GIANT.get());
                            it.delayServerAction(20, _it -> {
                                _it.spawnOther(TFEntities.GIANT_MINER.get());
                            });
                        });
                    }

                    level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("block.anvil.destroy")), SoundSource.MASTER, 6.0F, 1.0F);
                } else {
                    level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("block.anvil.destroy")), SoundSource.MASTER, 6.0F, 1.0F, false);
                }

                level.levelEvent(2001, BlockPos.containing(x, y + (double) 2.5F, z), net.minecraft.world.level.block.Block.getId(Blocks.BAMBOO_BLOCK.defaultBlockState()));

            } else if (healthInTime <= 200.0F && entity.getMidHPPhase() < 3) {
                entity.progressMidHPPhase();
                if (!level.isClientSide()) {
                    entity.delayServerAction(20, it -> {
                        it.spawnOther(TFEntities.SNOW_QUEEN.get());
                    });

                    if (difficulty >= 2) {
                        entity.delayServerAction(20, it -> {
                            it.spawnOther(TFEntities.ALPHA_YETI.get());
                        });
                    }
                }

            } else if (healthInTime <= 100.0F && entity.getMidHPPhase() < 4) {
                entity.progressMidHPPhase();
                entity.setTexture("castle_keeper_damage2");

                if (!level.isClientSide()) {
                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                    lightning.moveTo(Vec3.atBottomCenterOf(BlockPos.containing(x, y, z)));
                    lightning.setVisualOnly(true);
                    level.addFreshEntity(lightning);

                    entity.delayServerAction(20, it -> {
                        it.spawnOther(TFEntities.HYDRA.get());
                        it.delayServerAction(20, _it -> {
                            _it.spawnOther(TFEntities.UR_GHAST.get());
                        });
                    });

                    if (difficulty >= 2) {
                        entity.delayServerAction(20, it -> {
                            it.spawnOther(TFEntities.MINOSHROOM.get());
                            it.delayServerAction(20, _it -> {
                                for (int i = 0; i < 6; ++i) {
                                    _it.spawnOther(TFEntities.KNIGHT_PHANTOM.get());
                                }
                            });
                        });
                    }

                    level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("block.anvil.destroy")), SoundSource.MASTER, 6.0F, 1.0F);
                } else {
                    level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("block.anvil.destroy")), SoundSource.MASTER, 6.0F, 1.0F, false);
                }

                level.levelEvent(2001, BlockPos.containing(x, y + 3.2, z), net.minecraft.world.level.block.Block.getId(Blocks.GREEN_CONCRETE.defaultBlockState()));
            }
        }

        if (entity.getHealth() == 0.0F) {
            entity.getPersistentData().putDouble("CastleKeeperAttack", 0.0F);
            if (!level.isClientSide()) {
                ((ServerLevel) level).sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 10, 0.4, 1.0F, 0.4, 0.02);

                AbstractArrow arrow = TFFBEntities.ESCAPING_SOUL.get().create(level);
                arrow.setBaseDamage(5.0F);
//                arrow.setKnockback(1);
                arrow.moveTo(x, y, z);
                arrow.shoot(Mth.nextDouble(level.getRandom(), -0.4, 0.4), 3.5F, Mth.nextDouble(level.getRandom(), -0.4, 0.4), 2.0F, 0.0F);
                level.addFreshEntity(arrow);
            }
        }
    }
}
