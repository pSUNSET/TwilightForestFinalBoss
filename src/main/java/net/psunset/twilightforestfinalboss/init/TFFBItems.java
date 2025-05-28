package net.psunset.twilightforestfinalboss.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;

public class TFFBItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(TwilightForestFinalBoss.ID);
    public static final DeferredRegister.Items BLOCK_ITEMS_REGISTRY = DeferredRegister.createItems(TwilightForestFinalBoss.ID);
    public static final DeferredRegister.Items SPAWN_EGGS_REGISTRY = DeferredRegister.createItems(TwilightForestFinalBoss.ID);
}
