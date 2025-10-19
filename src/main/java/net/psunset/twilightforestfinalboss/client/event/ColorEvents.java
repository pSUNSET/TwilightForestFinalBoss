package net.psunset.twilightforestfinalboss.client.event;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;

@Mod.EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorEvents {

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, getter, pos, tintIndex) -> 0xFF5C1074, TFFBBlocks.VIOLET_FRAGILE_FIELD.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        BlockColors blockColors = event.getBlockColors();

        event.register((stack, tintIndex) -> stack.getItem() instanceof BlockItem item ? blockColors.getColor(item.getBlock().defaultBlockState(), null, null, tintIndex) : -1,
                TFFBBlocks.VIOLET_FRAGILE_FIELD.get());
    }
}
