package net.psunset.twilightforestfinalboss.tool;

import com.google.common.collect.Lists;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod.EventBusSubscriber(modid = TwilightForestFinalBoss.ID)
public class ActionUtl {

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void delayInServer(int tick, Runnable runnable) {
        if (Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.SERVER)){
            workQueue.add(new AbstractMap.SimpleEntry<>(runnable, tick));
        }
    }

    @SubscribeEvent
    public static void afterServerTick(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.END) { // ServerTickEvent.Post in NeoForge
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = Lists.newArrayList();
            workQueue.forEach(it -> {
                it.setValue(it.getValue() - 1);
                if (it.getValue() == 0) {
                    actions.add(it);
                }
            });
            actions.forEach(it -> it.getKey().run());
            workQueue.removeAll(actions);
        }
    }
}
