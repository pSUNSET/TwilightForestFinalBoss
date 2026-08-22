package net.psunset.twilightforestfinalboss;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.psunset.twilightforestfinalboss.init.*;
import org.slf4j.Logger;

@Mod(TwilightForestFinalBoss.ID)
public class TwilightForestFinalBoss {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "twilight_forest_final_boss";

    public TwilightForestFinalBoss(ModContainer container, IEventBus bus, Dist dist) {
        TFFBBlocks.REGISTRY.register(bus);
        TFFBItems.REGISTRY.register(bus);
        TFFBItems.BLOCK_ITEMS_REGISTRY.register(bus);
        TFFBItems.SPAWN_EGGS_REGISTRY.register(bus);
        TFFBEntities.REGISTRY.register(bus);
        TFFBTabs.REGISTRY.register(bus);
        TFFBLootPoolEntries.REGISTRY.register(bus);
    }
}
