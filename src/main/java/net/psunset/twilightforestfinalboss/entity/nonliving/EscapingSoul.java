package net.psunset.twilightforestfinalboss.entity.nonliving;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;
import net.psunset.twilightforestfinalboss.tool.RLUtl;

public class EscapingSoul extends AbstractArrow implements ItemSupplier {
    public static final ItemStack EMPTY_ITEM = new ItemStack(Blocks.AIR);

    public EscapingSoul(EntityType<? extends EscapingSoul> type, Level world) {
        super(type, world);
    }

    public EscapingSoul(EntityType<? extends EscapingSoul> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world, EMPTY_ITEM, null);
    }

    public EscapingSoul(EntityType<? extends EscapingSoul> type, LivingEntity entity, Level world) {
        super(type, entity, world, EMPTY_ITEM, null);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_entity) {
//        return NetworkHooks.getEntitySpawningPacket(this);
        return super.getAddEntityPacket(p_entity);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return EMPTY_ITEM;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return EMPTY_ITEM;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    public void tick() {
        super.tick();

        setNoGravity(true);
        level().addParticle(ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 0.0F, 0.0F, 0.0F);
        level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 0.0F, 0.0F, 0.0F);

        if (this.inGround) {
            this.discard();
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        try {
            super.addAdditionalSaveData(compound);
        } catch (IllegalStateException ignored) {
            // Can't save data for an air block so directly ignore it
        }
    }

    public static EscapingSoul shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1.0F, 5.0F, 5);
    }

    public static EscapingSoul shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
        return shoot(world, entity, source, pullingPower, 5.0F, 5);
    }

    public static EscapingSoul shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        EscapingSoul arrow = new EscapingSoul(TFFBEntities.ESCAPING_SOUL.get(), entity, world);
        arrow.shoot(entity.getViewVector(1.0F).x, entity.getViewVector(1.0F).y, entity.getViewVector(1.0F).z, power * 2.0F, 0.0F);
        arrow.setSilent(true);
        arrow.setCritArrow(false);
        arrow.setBaseDamage(damage);
//        arrow.setKnockback(knockback);
        world.addFreshEntity(arrow);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.arrow.shoot")), SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + power / 2.0F);
        return arrow;
    }

    public static EscapingSoul shoot(LivingEntity entity, LivingEntity target) {
        EscapingSoul arrow = new EscapingSoul(TFFBEntities.ESCAPING_SOUL.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + (double)target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        arrow.shoot(dx, dy - arrow.getY() + Math.hypot(dx, dz) * (double)0.2F, dz, 2.0F, 12.0F);
        arrow.setSilent(true);
        arrow.setBaseDamage(5.0F);
//        arrow.setKnockback(5);
        arrow.setCritArrow(false);
        entity.level().addFreshEntity(arrow);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.arrow.shoot")), SoundSource.PLAYERS, 1.0F, 1.0F / (RandomSource.create().nextFloat() * 0.5F + 1.0F));
        return arrow;
    }
}
