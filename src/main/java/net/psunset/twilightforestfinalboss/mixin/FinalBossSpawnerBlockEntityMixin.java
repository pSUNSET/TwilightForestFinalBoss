package net.psunset.twilightforestfinalboss.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.EventHooks;
import net.psunset.twilightforestfinalboss.entity.boss.CastleKeeper;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import net.psunset.twilightforestfinalboss.init.TFFBEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.block.entity.spawner.BossSpawnerBlockEntity;
import twilightforest.block.entity.spawner.FinalBossSpawnerBlockEntity;
import twilightforest.entity.boss.PlateauBoss;

@Mixin(FinalBossSpawnerBlockEntity.class)
public abstract class FinalBossSpawnerBlockEntityMixin extends BossSpawnerBlockEntity<PlateauBoss> {
    protected FinalBossSpawnerBlockEntityMixin(BlockEntityType<?> type, EntityType<PlateauBoss> entityType, BlockPos pos, BlockState state) {
        super(type, entityType, pos, state);
    }

    @Inject(method = "spawnMyBoss", at = @At("HEAD"), cancellable = true)
    public void beforeSpawnMyBoss(ServerLevelAccessor accessor, CallbackInfoReturnable<Boolean> cir) {
        BlockPos.betweenClosed(this.getBlockPos().offset(-10, -1, -10), getBlockPos().offset(-10, 11, 10)).forEach(it -> {
            if (level.getBlockState(it).is(TFFBBlocks.VIOLET_FRAGILE_FIELD.get())) {
                level.removeBlock(it, false);
            }
        });
        BlockPos.betweenClosed(this.getBlockPos().offset(-10, 10, 10), getBlockPos().offset(10, -1, 10)).forEach(it -> {
            if (level.getBlockState(it).is(TFFBBlocks.VIOLET_FRAGILE_FIELD.get())) {
                level.removeBlock(it, false);
            }
        });
        BlockPos.betweenClosed(this.getBlockPos().offset(10, -1, 10), getBlockPos().offset(10, 10, -10)).forEach(it -> {
            if (level.getBlockState(it).is(TFFBBlocks.VIOLET_FRAGILE_FIELD.get())) {
                level.removeBlock(it, false);
            }
        });
        BlockPos.betweenClosed(this.getBlockPos().offset(10, 11, -10), getBlockPos().offset(-10, -1, -10)).forEach(it -> {
            if (level.getBlockState(it).is(TFFBBlocks.VIOLET_FRAGILE_FIELD.get())) {
                level.removeBlock(it, false);
            }
        });
        BlockPos.betweenClosed(this.getBlockPos().offset(-10, 10, -10), getBlockPos().offset(10, 10, 10)).forEach(it -> {
            if (level.getBlockState(it).is(TFFBBlocks.VIOLET_FRAGILE_FIELD.get())) {
                level.destroyBlock(it, false);
            }
        });

        CastleKeeper entity = TFFBEntities.CASTLE_KEEPER.get().create(accessor.getLevel());

        BlockPos spawnPos = accessor.getBlockState(this.getBlockPos().below()).getCollisionShape(accessor, this.getBlockPos().below()).isEmpty() ? this.getBlockPos().below() : this.getBlockPos();
        entity.moveTo(spawnPos, accessor.getLevel().getRandom().nextFloat() * 360F, 0.0F);
        EventHooks.finalizeMobSpawn(entity, accessor, accessor.getCurrentDifficultyAt(spawnPos), MobSpawnType.SPAWNER, null);

        // set creature's home to this
//        this.initializeCreature(entity);
        entity.setRestrictionPoint(GlobalPos.of(entity.level().dimension(), this.getBlockPos()));

        // spawn it
        cir.setReturnValue(accessor.addFreshEntity(entity));
    }
}
