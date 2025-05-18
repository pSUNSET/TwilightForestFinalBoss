package net.psunset.twilightforestfinalboss.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import twilightforest.entity.boss.BaseTFBoss;

@EventBusSubscriber(modid = TwilightForestFinalBoss.ID)
public class EntityEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof BaseTFBoss boss){
            CastleKeeper.childToParent.remove(boss);
        }
    }
}
