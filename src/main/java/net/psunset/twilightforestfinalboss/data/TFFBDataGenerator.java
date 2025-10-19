package net.psunset.twilightforestfinalboss.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.data.blockstates.TFFBBlockStateProvider;
import net.psunset.twilightforestfinalboss.data.lang.TFFBLangProvider;
import net.psunset.twilightforestfinalboss.data.loot_table.TFFBLootTableProvider;
import net.psunset.twilightforestfinalboss.data.models.TFFBItemModelProvider;
import net.psunset.twilightforestfinalboss.data.tags.TFFBBlockTagsProvider;
import net.psunset.twilightforestfinalboss.data.tags.TFFBDamageTypeTagsProvider;
import net.psunset.twilightforestfinalboss.data.tags.TFFBItemTagsProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFFBDataGenerator {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        boolean isServer = event.includeServer();
        boolean isClient = event.includeClient();

        generator.addProvider(isClient, new TFFBBlockStateProvider(output, fileHelper));
        generator.addProvider(isClient, new TFFBItemModelProvider(output, fileHelper));

        generator.addProvider(isClient, new TFFBLangProvider(output));

        generator.addProvider(isServer, new TFFBLootTableProvider(output));

        var blockTagsProvider = new TFFBBlockTagsProvider(output, registries, fileHelper);
        generator.addProvider(isServer, blockTagsProvider);
        generator.addProvider(isServer, new TFFBItemTagsProvider(output, registries, blockTagsProvider.contentsGetter(), fileHelper));
        generator.addProvider(isServer, new TFFBDamageTypeTagsProvider(output, registries, fileHelper));

    }
}
