package net.psunset.twilightforestfinalboss.data.models;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import twilightforest.block.ForceFieldBlock;

public class TFFBItemModelProvider extends ItemModelProvider {
    public TFFBItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TwilightForestFinalBoss.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        forceField(TFFBBlocks.VIOLET_FRAGILE_FIELD, RLUtl.of("block/forcefield_white"));
    }

    private ItemModelBuilder forceField(DeferredBlock<ForceFieldBlock> block, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(block.getId().getPath(), "item/generated");
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType("minecraft:translucent", 0).end();
        return builder;
    }
}
