package net.psunset.twilightforestfinalboss.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;

public class TFFBItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(TwilightForestFinalBoss.ID);

    public static final DeferredItem<BlockItem> VIOLET_FRAGILE_FIELD = REGISTRY.registerSimpleBlockItem(TFFBBlocks.VIOLET_FRAGILE_FIELD);

    public static final DeferredItem<DeferredSpawnEggItem> CASTLE_KEEPER_SPAWN_EGG = REGISTRY.register("castle_keeper_spawn_egg", () -> new DeferredSpawnEggItem(() -> TFFBEntities.CASTLE_KEEPER.get(), -1, -16777216, new Item.Properties()));
}
