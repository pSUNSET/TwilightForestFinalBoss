package net.psunset.twilightforestfinalboss.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;

public class TFFBItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestFinalBoss.ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestFinalBoss.ID);
    public static final DeferredRegister<Item> SPAWN_EGGS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TwilightForestFinalBoss.ID);
}
