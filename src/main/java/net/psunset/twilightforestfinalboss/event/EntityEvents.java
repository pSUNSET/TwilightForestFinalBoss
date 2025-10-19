package net.psunset.twilightforestfinalboss.event;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;

@Mod.EventBusSubscriber(modid = TwilightForestFinalBoss.ID)
public class EntityEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Mob boss){
            CastleKeeper.CHILDREN_TO_PARENT.remove(boss);
        }
    }
}
