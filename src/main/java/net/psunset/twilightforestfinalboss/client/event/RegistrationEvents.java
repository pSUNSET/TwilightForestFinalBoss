package net.psunset.twilightforestfinalboss.client.event;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.client.renderer.CastleKeeperRenderer;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;

@Mod.EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegistrationEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TFFBEntities.CASTLE_KEEPER.get(), CastleKeeperRenderer::new);
        event.registerEntityRenderer(TFFBEntities.LOBBED_FIREBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(TFFBEntities.ESCAPING_SOUL.get(), ThrownItemRenderer::new);
    }
}
