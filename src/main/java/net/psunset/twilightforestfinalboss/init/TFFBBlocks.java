package net.psunset.twilightforestfinalboss.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import twilightforest.block.ForceFieldBlock;

import java.util.function.Supplier;

public class TFFBBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(TwilightForestFinalBoss.ID);

    public static final DeferredBlock<ForceFieldBlock> VIOLET_FRAGILE_FIELD = register("violet_fragile_field", () -> new ForceFieldBlock(BlockBehaviour.Properties.of().lightLevel((state) -> 2).mapColor(DyeColor.PURPLE).noLootTable().noOcclusion().pushReaction(PushReaction.BLOCK).sound(SoundType.GLASS).instabreak()) {
        @Override
        public boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
            return true;
        }
    });

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block) {
        return register(name, block, true);
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block, boolean hasItem) {
        DeferredBlock<T> toReturn = REGISTRY.register(name, block);
        if (hasItem) TFFBItems.BLOCK_ITEMS_REGISTRY.registerSimpleBlockItem(name, toReturn);
        return toReturn;
    }
}
