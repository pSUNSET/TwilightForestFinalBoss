package net.psunset.twilightforestfinalboss.api;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface TFFBLevelAccessor {
    @Nullable
    Entity tffb$getEntity(UUID uuid);
}
