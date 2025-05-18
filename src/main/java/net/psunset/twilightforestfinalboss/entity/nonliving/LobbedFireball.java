package net.psunset.twilightforestfinalboss.entity.nonliving;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;
import net.psunset.twilightforestfinalboss.tool.RLUtl;

public class LobbedFireball extends AbstractArrow implements ItemSupplier {
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(Items.FIRE_CHARGE);

    public LobbedFireball(EntityType<? extends LobbedFireball> type, Level world) {
        super(type, world);
    }

    public LobbedFireball(EntityType<? extends LobbedFireball> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world, PROJECTILE_ITEM, null);
    }

    public LobbedFireball(EntityType<? extends LobbedFireball> type, LivingEntity entity, Level world) {
        super(type, entity, world, PROJECTILE_ITEM, null);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity p_entity) {
//        return NetworkHooks.getEntitySpawningPacket(this);
        return super.getAddEntityPacket(p_entity);
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getItem() {
        return PROJECTILE_ITEM;
    }

    protected ItemStack getDefaultPickupItem() {
        return PROJECTILE_ITEM;
    }

    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        level().setBlockAndUpdate(result.getBlockPos().relative(result.getDirection()), Blocks.FIRE.defaultBlockState());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().setRemainingFireTicks(100); // 5 secs
    }

    public void tick() {
        super.tick();

        for (int i = 0; i < 15; ++i) {
            level().addParticle(ParticleTypes.FLAME, getX(), getY(), getZ(), Mth.nextDouble(RandomSource.create(), -0.2, 0.2), Mth.nextDouble(RandomSource.create(), -0.2, 0.2), Mth.nextDouble(RandomSource.create(), -0.2, 0.2));
        }

        if (this.inGround) {
            this.discard();
        }
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    public static LobbedFireball shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1.0F, 5.0F, 5);
    }

    public static LobbedFireball shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
        return shoot(world, entity, source, pullingPower, 5.0F, 5);
    }

    public static LobbedFireball shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        LobbedFireball fireball = new LobbedFireball(TFFBEntities.LOBBED_FIREBALL.get(), entity, world);
        fireball.shoot(entity.getViewVector(1.0F).x, entity.getViewVector(1.0F).y, entity.getViewVector(1.0F).z, power * 2.0F, 0.0F);
        fireball.setSilent(true);
        fireball.setCritArrow(false);
        fireball.setBaseDamage(damage);
//        fireball.setKnockback(knockback);
        fireball.setRemainingFireTicks(2000); // 100 secs
        world.addFreshEntity(fireball);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.arrow.shoot")), SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + power / 2.0F);
        return fireball;
    }

    public static LobbedFireball shoot(LivingEntity entity, LivingEntity target) {
        LobbedFireball fireball = new LobbedFireball(TFFBEntities.LOBBED_FIREBALL.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + (double)target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        fireball.shoot(dx, dy - fireball.getY() + Math.hypot(dx, dz) * (double)0.2F, dz, 2.0F, 12.0F);
        fireball.setSilent(true);
        fireball.setBaseDamage(5.0F);
//        fireball.setKnockback(5);
        fireball.setCritArrow(false);
        fireball.setRemainingFireTicks(2000); // 100 secs
        entity.level().addFreshEntity(fireball);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), BuiltInRegistries.SOUND_EVENT.get(RLUtl.ofVanilla("entity.arrow.shoot")), SoundSource.PLAYERS, 1.0F, 1.0F / (RandomSource.create().nextFloat() * 0.5F + 1.0F));
        return fireball;
    }
}
