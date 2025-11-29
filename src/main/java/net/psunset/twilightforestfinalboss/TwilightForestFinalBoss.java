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
//    private static final String PROTOCOL_VERSION = "1";

    public TwilightForestFinalBoss(ModContainer container, IEventBus bus, Dist dist) {
        TFFBBlocks.REGISTRY.register(bus);
        TFFBItems.REGISTRY.register(bus);
        TFFBItems.BLOCK_ITEMS_REGISTRY.register(bus);
        TFFBItems.SPAWN_EGGS_REGISTRY.register(bus);
        TFFBEntities.REGISTRY.register(bus);
        TFFBTabs.REGISTRY.register(bus);
        TFFBLootPoolEntries.REGISTRY.register(bus);
    }

//    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(RLUtl.of("twilight_forest_final_boss"), () -> "1", "1"::equals, "1"::equals);
//
//    private static int messageID = 0;
//
//    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
//        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
//        messageID++;
//    }
}
