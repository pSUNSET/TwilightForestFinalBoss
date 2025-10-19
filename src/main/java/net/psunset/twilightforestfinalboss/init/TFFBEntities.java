package net.psunset.twilightforestfinalboss.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.entity.nonliving.EscapingSoul;
import net.psunset.twilightforestfinalboss.entity.nonliving.LobbedFireball;

import java.util.function.Supplier;

public class TFFBEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TwilightForestFinalBoss.ID);;
    public static final RegistryObject<EntityType<CastleKeeper>> CASTLE_KEEPER = register("castle_keeper", Builder.of(CastleKeeper::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).fireImmune().sized(1.0F, 3.6F), () -> new ForgeSpawnEggItem(TFFBEntities.CASTLE_KEEPER, -1, -16777216, new Item.Properties()));
    public static final RegistryObject<EntityType<LobbedFireball>> LOBBED_FIREBALL = register("lobbed_fireball", Builder.<LobbedFireball>of(LobbedFireball::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5F, 0.5F));
    public static final RegistryObject<EntityType<EscapingSoul>> ESCAPING_SOUL = register("escaping_soul", Builder.<EscapingSoul>of(EscapingSoul::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5F, 0.5F));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(name, () -> entityTypeBuilder.build(name));
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder, Supplier<SpawnEggItem> spawnEggItem) {
        RegistryObject<EntityType<T>> toReturn = REGISTRY.register(name, () -> entityTypeBuilder.build(name));
        TFFBItems.SPAWN_EGGS_REGISTRY.register(name + "_spawn_egg", spawnEggItem);
        return toReturn;
    }
}
