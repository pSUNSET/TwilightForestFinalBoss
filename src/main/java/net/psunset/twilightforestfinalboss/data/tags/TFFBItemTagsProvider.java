package net.psunset.twilightforestfinalboss.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.compat.ModdedItemTagGenerator;

import java.util.concurrent.CompletableFuture;

public class TFFBItemTagsProvider extends ItemTagsProvider {
    public TFFBItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, TwilightForestFinalBoss.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModdedItemTagGenerator.RANDOMIUM_BLACKLIST)
                .add(TFFBBlocks.VIOLET_FRAGILE_FIELD.get().asItem());
    }
}
