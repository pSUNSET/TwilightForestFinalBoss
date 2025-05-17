package net.psunset.twilightforestfinalboss.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.entity.nonliving.EscapingSoul;
import net.psunset.twilightforestfinalboss.entity.nonliving.LobbedFireball;

public class TFFBEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, TwilightForestFinalBoss.ID);;
    public static final DeferredHolder<EntityType<?>, EntityType<CastleKeeper>> CASTLE_KEEPER = register("castle_keeper", Builder.of(CastleKeeper::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).fireImmune().sized(1.0F, 3.6F));;
    public static final DeferredHolder<EntityType<?>, EntityType<LobbedFireball>> LOBBED_FIREBALL = register("lobbed_fireball", Builder.<LobbedFireball>of(LobbedFireball::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5F, 0.5F));;
    public static final DeferredHolder<EntityType<?>, EntityType<EscapingSoul>> ESCAPING_SOUL = register("escaping_soul", Builder.<EscapingSoul>of(EscapingSoul::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5F, 0.5F));;

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(name, () -> entityTypeBuilder.build(name));
    }
}
