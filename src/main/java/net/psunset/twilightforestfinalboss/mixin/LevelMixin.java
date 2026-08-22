package net.psunset.twilightforestfinalboss.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.psunset.twilightforestfinalboss.api.TFFBLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(Level.class)
public abstract class LevelMixin implements TFFBLevelAccessor {
    @Shadow
    protected abstract LevelEntityGetter<Entity> getEntities();

    @Unique
    @Override
    public Entity tffb$getEntity(UUID uuid) {
        return getEntities().get(uuid);
    }
}
