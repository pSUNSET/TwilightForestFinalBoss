package net.psunset.twilightforestfinalboss.tool;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class EntityUtl {

    public static void shootDisposableProjectile(EntityType<? extends Projectile> projectile, Level level, Entity shooter) {
        Projectile entity = projectile.create(level);
        if (entity == null) return;
        entity.setOwner(shooter);
        entity.moveTo(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        entity.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, 1.0F, 0.0F);
        level.addFreshEntity(entity);
    }
}
