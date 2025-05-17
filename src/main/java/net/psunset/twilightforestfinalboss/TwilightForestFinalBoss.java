package net.psunset.twilightforestfinalboss;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;
import net.psunset.twilightforestfinalboss.init.TFFBItems;
import net.psunset.twilightforestfinalboss.init.TFFBTabs;
import org.slf4j.Logger;

@Mod(TwilightForestFinalBoss.ID)
public class TwilightForestFinalBoss {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "twilight_forest_final_boss";
//    private static final String PROTOCOL_VERSION = "1";

    public TwilightForestFinalBoss(ModContainer container, IEventBus bus, Dist dist) {
        TFFBBlocks.REGISTRY.register(bus);
        TFFBItems.REGISTRY.register(bus);
        TFFBEntities.REGISTRY.register(bus);
        TFFBTabs.REGISTRY.register(bus);
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
