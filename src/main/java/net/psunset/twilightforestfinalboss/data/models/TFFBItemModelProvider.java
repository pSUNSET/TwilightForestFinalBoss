package net.psunset.twilightforestfinalboss.data.models;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import net.psunset.twilightforestfinalboss.init.TFFBItems;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import twilightforest.block.ForceFieldBlock;

public class TFFBItemModelProvider extends ItemModelProvider {
    public TFFBItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TwilightForestFinalBoss.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        forceField(TFFBBlocks.VIOLET_FRAGILE_FIELD, RLUtl.of("block/forcefield_white"));

        TFFBItems.SPAWN_EGGS_REGISTRY.getEntries().forEach(item -> {
            getBuilder(item.getId().getPath()).parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
        });
    }

    private ItemModelBuilder forceField(RegistryObject<ForceFieldBlock> block, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(block.getId().getPath(), "item/generated");
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType("minecraft:translucent", 0).end();
        return builder;
    }
}
