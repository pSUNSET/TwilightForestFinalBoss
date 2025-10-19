package net.psunset.twilightforestfinalboss.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import twilightforest.data.tags.BlockTagGenerator;

import java.util.concurrent.CompletableFuture;

public class TFFBBlockTagsProvider extends BlockTagGenerator {
    public TFFBBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTagGenerator.COMMON_PROTECTIONS)
                .add(TFFBBlocks.VIOLET_FRAGILE_FIELD.get());

        tag(BlockTagGenerator.ANNIHILATION_INCLUSIONS)
                .add(TFFBBlocks.VIOLET_FRAGILE_FIELD.get());

//        tag(Tags.Blocks.RELOCATION_NOT_SUPPORTED)
//                .add(TFFBBlocks.VIOLET_FRAGILE_FIELD.get());
    }
}
