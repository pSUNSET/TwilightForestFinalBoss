package net.psunset.twilightforestfinalboss.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFBlocks;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.finalcastle.FinalCastleBossGazeboComponent;

@Mixin(FinalCastleBossGazeboComponent.class)
public abstract class FinalCastleBossGazeboComponentMixin extends TFStructureComponentOld {
    @Unique
    private static final ResourceLocation NEW_GAZEBO_POOL = RLUtl.of("fragile_gazebo");

    public FinalCastleBossGazeboComponentMixin(StructurePieceType piece, CompoundTag nbt) {
        super(piece, nbt);
    }

    @Inject(method = "addChildren", at = @At("HEAD"), cancellable = true)
    public void beforeAddChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "postProcess", at = @At("HEAD"), cancellable = true)
    public void beforePostProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos, CallbackInfo ci) {
        BlockState state = TFFBBlocks.VIOLET_FRAGILE_FIELD.get().defaultBlockState();

        for (Rotation rotation : RotationUtil.ROTATIONS) {
            this.fillBlocksRotated(level, sbb, 0, 0, 0, 0, 10, 20, state, rotation);
        }

        this.generateBox(level, sbb, 1, 11, 0, 19, 11, 20, state, state, false);
        this.generateBox(level, sbb, 0, 11, 0, 0, 11, 20, state, state, false);
        this.generateBox(level, sbb, 20, 11, 0, 20, 11, 20, state, state, false);

        placeBlock(level, TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get().defaultBlockState(), 10, 1, 10, sbb);
        ci.cancel();
    }
}
