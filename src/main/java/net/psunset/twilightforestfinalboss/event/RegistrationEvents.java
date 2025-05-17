package net.psunset.twilightforestfinalboss.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;

@EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CastleKeeper.init());
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TFFBEntities.CASTLE_KEEPER.get(), CastleKeeper.createAttributes().build());
    }
}
