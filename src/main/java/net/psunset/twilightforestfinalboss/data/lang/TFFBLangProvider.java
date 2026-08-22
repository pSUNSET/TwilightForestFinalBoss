package net.psunset.twilightforestfinalboss.data.lang;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;

public class TFFBLangProvider extends LanguageProvider {
    public TFFBLangProvider(PackOutput output) {
        super(output, TwilightForestFinalBoss.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(TFFBBlocks.VIOLET_FRAGILE_FIELD, "Violet Fragile Field");

        addEntityAndEgg(TFFBEntities.CASTLE_KEEPER, "Castle Keeper");

        addAdvancement("a_tussel_with_calamity", "A Tussel with Calamity", "Defeat the Castle Keeper on top of the Final Castle");
        addAdvancement("no_stone_left_unturned", "No Stone Left Unturned", "Combine the talisman of the cube, a block and chain, and a lamp of cinders to create the cube of annihilation");
    }

    public void addEntityAndEgg(DeferredHolder<EntityType<?>, ? extends EntityType<?>> entity, String name) {
        this.addEntityType(entity, name);
        this.add("item." + TwilightForestFinalBoss.ID + "." + entity.getId().getPath() + "_spawn_egg", name + " Spawn Egg");
    }

    public void addAdvancement(String key, String title, String desc) {
        this.add("advancement." + TwilightForestFinalBoss.ID + "." + key + ".title", title);
        this.add("advancement." + TwilightForestFinalBoss.ID + "." + key + ".desc", desc);
    }
}
