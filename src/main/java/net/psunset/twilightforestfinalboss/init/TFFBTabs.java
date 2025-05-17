package net.psunset.twilightforestfinalboss.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;

@EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = EventBusSubscriber.Bus.MOD)
public class TFFBTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TwilightForestFinalBoss.ID);

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
        if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            tabData.accept(TFFBItems.CASTLE_KEEPER_SPAWN_EGG.get());
        }
    }
}
