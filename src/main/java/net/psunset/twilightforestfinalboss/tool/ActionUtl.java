package net.psunset.twilightforestfinalboss.tool;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import org.apache.commons.compress.utils.Lists;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@EventBusSubscriber(modid = TwilightForestFinalBoss.ID)
public class ActionUtl {

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void delayInServer(int tick, Runnable runnable) {
        if (Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.SERVER)){
            workQueue.add(new AbstractMap.SimpleEntry<>(runnable, tick));
        }
    }

    @SubscribeEvent
    protected static void afterServerTick(ServerTickEvent.Post event){
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
