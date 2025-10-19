package net.psunset.twilightforestfinalboss.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;

@Mod.EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {

    @SubscribeEvent
    public static void onRegisterPlacements(SpawnPlacementRegisterEvent event) {

    }

    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        event.put(TFFBEntities.CASTLE_KEEPER.get(), CastleKeeper.createAttributes().build());
    }
}
