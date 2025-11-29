package net.psunset.twilightforestfinalboss.init;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.loot.entries.OptionalLootItem;

public class TFFBLootPoolEntries {

    public static final DeferredRegister<LootPoolEntryType> REGISTRY = DeferredRegister.create(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE, TwilightForestFinalBoss.ID);

    public static final DeferredHolder<LootPoolEntryType, LootPoolEntryType> OPTIONAL_ITEM = register("optional_item", OptionalLootItem.CODEC);

    private static DeferredHolder<LootPoolEntryType, LootPoolEntryType> register(String name, MapCodec<? extends LootPoolEntryContainer> codec) {
        return REGISTRY.register(name, () -> new LootPoolEntryType(codec));
    }
}
