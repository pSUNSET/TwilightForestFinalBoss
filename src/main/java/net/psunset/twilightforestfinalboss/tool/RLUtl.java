package net.psunset.twilightforestfinalboss.tool;

import net.minecraft.resources.ResourceLocation;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import twilightforest.TwilightForestMod;

public class RLUtl {

    public static ResourceLocation of(String name) {
        return ResourceLocation.fromNamespaceAndPath(TwilightForestFinalBoss.ID, name);
    }

    public static ResourceLocation ofVanilla(String name) {
        return ResourceLocation.withDefaultNamespace(name);
    }

    public static ResourceLocation ofTF(String path) {
        return TwilightForestMod.prefix(path);
    }
}
