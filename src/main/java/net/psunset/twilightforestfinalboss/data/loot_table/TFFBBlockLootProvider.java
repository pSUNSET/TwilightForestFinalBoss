package net.psunset.twilightforestfinalboss.data.loot_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.init.TFBlocks;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TFFBBlockLootProvider extends BlockLootSubProvider {
    protected TFFBBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Set.of();
    }
}
