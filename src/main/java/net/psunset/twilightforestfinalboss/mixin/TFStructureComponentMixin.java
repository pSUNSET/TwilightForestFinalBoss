package net.psunset.twilightforestfinalboss.mixin;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.common.util.Lazy;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.world.components.structures.TFStructureComponent;

import java.util.Set;

@Mixin(TFStructureComponent.class)
public abstract class TFStructureComponentMixin extends StructurePiece {

    @Unique
    private static final Lazy<Set<Block>> OTHER_BLOCKS_NEEDING_POSTPROCESSING = Lazy.of(() -> ImmutableSet.<Block>builder()
            .add(TFFBBlocks.VIOLET_FRAGILE_FIELD.get())
            .build());

    protected TFStructureComponentMixin(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
        super(type, genDepth, boundingBox);
    }

    @Inject(method = "placeBlock", at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE))
    protected void onPlaceBlock(WorldGenLevel worldIn, BlockState blockstateIn, int x, int y, int z, BoundingBox boundingboxIn, CallbackInfo ci) {
        BlockPos blockpos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
        if (OTHER_BLOCKS_NEEDING_POSTPROCESSING.get().contains(blockstateIn.getBlock())) {
            worldIn.getChunk(blockpos).markPosForPostprocessing(blockpos);
        }
    }
}
