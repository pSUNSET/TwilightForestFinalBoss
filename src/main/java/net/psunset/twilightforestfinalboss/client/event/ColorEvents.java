package net.psunset.twilightforestfinalboss.client.event;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.item.BlockItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;

@EventBusSubscriber(modid = TwilightForestFinalBoss.ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
