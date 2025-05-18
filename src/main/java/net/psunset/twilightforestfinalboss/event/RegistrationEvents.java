package net.psunset.twilightforestfinalboss.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;

@EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {

    @SubscribeEvent
    public static void onRegisterPlacements(RegisterSpawnPlacementsEvent event) {

    }

    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(TFFBEntities.CASTLE_KEEPER.get(), CastleKeeper.createAttributes().build());
    }
}
