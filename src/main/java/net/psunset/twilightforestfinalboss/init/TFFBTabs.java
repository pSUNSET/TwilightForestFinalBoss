package net.psunset.twilightforestfinalboss.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;

@Mod.EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TFFBTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TwilightForestFinalBoss.ID);

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            TFFBItems.SPAWN_EGGS_REGISTRY.getEntries().forEach(entry -> tabData.accept(entry.get()));
        }
    }
}
