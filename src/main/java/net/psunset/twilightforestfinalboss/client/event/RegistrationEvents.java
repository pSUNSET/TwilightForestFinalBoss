package net.psunset.twilightforestfinalboss.client.event;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.client.renderer.CastleKeeperRenderer;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;

@EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegistrationEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TFFBEntities.CASTLE_KEEPER.get(), CastleKeeperRenderer::new);
        event.registerEntityRenderer(TFFBEntities.LOBBED_FIREBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(TFFBEntities.ESCAPING_SOUL.get(), ThrownItemRenderer::new);
    }
}
