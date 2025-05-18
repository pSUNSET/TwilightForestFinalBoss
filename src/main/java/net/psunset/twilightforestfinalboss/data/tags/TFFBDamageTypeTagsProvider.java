package net.psunset.twilightforestfinalboss.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFFBDamageTypeTagsProvider extends TagsProvider<DamageType> {
    public TFFBDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, TwilightForestFinalBoss.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
//        tag(TFFBTags.DamageTypes.CASTLE_KEEPER_IMMUNE_TO)
//                .add(DamageTypes.IN_FIRE)
//                .add(DamageTypes.FALL)
//                .add(DamageTypes.CACTUS)
//                .add(DamageTypes.DRAGON_BREATH)
//                .add(NeoForgeMod.POISON_DAMAGE);
    }
}
