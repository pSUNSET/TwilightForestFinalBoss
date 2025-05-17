package net.psunset.twilightforestfinalboss.data.blockstates;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.psunset.twilightforestfinalboss.TwilightForestFinalBoss;
import net.psunset.twilightforestfinalboss.init.TFFBBlocks;
import net.psunset.twilightforestfinalboss.tool.RLUtl;
import twilightforest.block.ForceFieldBlock;
import twilightforest.client.model.block.forcefield.ForceFieldModel;
import twilightforest.client.model.block.forcefield.ForceFieldModelBuilder;

public class TFFBBlockStateProvider extends BlockStateProvider {

    protected static final ResourceLocation SOLID = ResourceLocation.withDefaultNamespace("solid");
    protected static final ResourceLocation CUTOUT = ResourceLocation.withDefaultNamespace("cutout");
    protected static final ResourceLocation CUTOUT_MIPPED = ResourceLocation.withDefaultNamespace("cutout_mipped");
    protected static final ResourceLocation TRANSLUCENT = ResourceLocation.withDefaultNamespace("translucent");

    public TFFBBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TwilightForestFinalBoss.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        forceFieldLike(TFFBBlocks.VIOLET_FRAGILE_FIELD);
    }

    public void forceFieldLike(DeferredBlock<ForceFieldBlock> block) {
        ResourceLocation textureLocation = RLUtl.of("block/" + block.getId().getPath());
        simpleBlock(block.get(), models().withExistingParent(block.getId().getPath(), ResourceLocation.withDefaultNamespace("block/block"))
				.texture("particle", textureLocation)
				.texture("pane", textureLocation)
				.ao(false)
				.renderType(TRANSLUCENT)
				.customLoader(ForceFieldModelBuilder::begin)

				//WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.WEST, true).from(0, 7, 7).to(7, 9, 9).shade(false).face(Direction.WEST).cullface(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.WEST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.EAST, true).from(9, 7, 7).to(16, 9, 9).shade(false).face(Direction.EAST).cullface(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.EAST).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN, true).from(7, 0, 7).to(9, 7, 9).shade(false).face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.DOWN).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//UP
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP, true).from(7, 9, 7).to(9, 16, 9).shade(false).face(Direction.UP).cullface(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.UP).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH, true).from(7, 7, 0).to(9, 9, 7).shade(false).face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.NORTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH, true).from(7, 7, 9).to(9, 9, 16).shade(false).face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end()
				.ifElse().from(7, 7, 7).to(9, 9, 9).shade(false).face(Direction.SOUTH).uvs(7, 7, 9, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_WEST, true).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.WEST).from(0, 0, 7).to(7, 7, 9).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).uvs(0, 0, 7, 7).end()
					.face(Direction.SOUTH).uvs(9, 0, 16, 7).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.WEST).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.DOWN).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_EAST, true).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.EAST).from(9, 0, 7).to(16, 7, 9).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).uvs(9, 0, 16, 7).end()
					.face(Direction.SOUTH).uvs(0, 0, 7, 7).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.EAST).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.DOWN).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().end()

				//DOWN NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_NORTH, true).from(7, 0, 0).to(9, 7, 7).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.NORTH).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 0, 9, 7).end()
					.face(Direction.WEST).uvs(0, 0, 7, 7).end()
					.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.NORTH).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.DOWN).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().end()

				//DOWN SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.DOWN_SOUTH, true).from(7, 0, 9).to(9, 7, 16).parents(ForceFieldModel.ExtraDirection.DOWN, ForceFieldModel.ExtraDirection.SOUTH).shade(false)
					.face(Direction.DOWN).cullface(Direction.DOWN).uvs(7, 9, 9, 16).end()
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 0, 9, 7).end()
					.face(Direction.WEST).uvs(9, 0, 16, 7).end()
					.face(Direction.EAST).uvs(0, 0, 7, 7).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 0, 7).to(9, 7, 9).parents(ForceFieldModel.ExtraDirection.DOWN).shade(false).face(Direction.SOUTH).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.DOWN).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().end()

				//UP WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_WEST, true).from(0, 9, 7).to(7, 16, 9).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.WEST).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(7, 9, 9, 16).end()
					.face(Direction.NORTH).uvs(0, 9, 7, 16).end()
					.face(Direction.SOUTH).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.WEST).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.UP).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//UP EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_EAST, true).from(9, 9, 7).to(16, 16, 9).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.EAST).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(7, 9, 9, 16).end()
					.face(Direction.NORTH).uvs(9, 9, 16, 16).end()
					.face(Direction.SOUTH).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.EAST).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.UP).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().end()

				//UP NORTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_NORTH, true).from(7, 9, 0).to(9, 16, 7).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.NORTH).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(7, 0, 9, 7).end()
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(7, 9, 9, 16).end()
					.face(Direction.WEST).uvs(0, 9, 7, 16).end()
					.face(Direction.EAST).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.NORTH).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.UP).uvs(7, 0, 9, 7).texture("#pane").emissivity(15, 15).end().end()

				//UP SOUTH
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.UP_SOUTH, true).from(7, 9, 9).to(9, 16, 16).parents(ForceFieldModel.ExtraDirection.UP, ForceFieldModel.ExtraDirection.SOUTH).shade(false)
					.face(Direction.UP).cullface(Direction.UP).uvs(7, 9, 9, 16).end()
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(7, 9, 9, 16).end()
					.face(Direction.WEST).uvs(9, 9, 16, 16).end()
					.face(Direction.EAST).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 9, 7).to(9, 16, 9).parents(ForceFieldModel.ExtraDirection.UP).shade(false).face(Direction.SOUTH).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.UP).uvs(7, 9, 9, 16).texture("#pane").emissivity(15, 15).end().end()

				//NORTH WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH_WEST, true).from(0, 7, 0).to(7, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH, ForceFieldModel.ExtraDirection.WEST).shade(false)
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
					.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.NORTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//NORTH EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.NORTH_EAST, true).from(9, 7, 0).to(16, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH, ForceFieldModel.ExtraDirection.EAST).shade(false)
					.face(Direction.NORTH).cullface(Direction.NORTH).uvs(9, 7, 16, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(0, 7, 7, 9).end()
					.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
					.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 0).to(9, 9, 7).parents(ForceFieldModel.ExtraDirection.NORTH).shade(false).face(Direction.EAST).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.NORTH).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end().end()

				//SOUTH WEST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH_WEST, true).from(0, 7, 9).to(7, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH, ForceFieldModel.ExtraDirection.WEST).shade(false)
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
					.face(Direction.WEST).cullface(Direction.WEST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(0, 9, 7, 16).end()
					.face(Direction.UP).uvs(9, 9, 16, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.WEST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(0, 7, 7).to(7, 9, 9).parents(ForceFieldModel.ExtraDirection.WEST).shade(false).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

				//SOUTH EAST
				.forceFieldElement().ifState(ForceFieldModel.ExtraDirection.SOUTH_EAST, true).from(9, 7, 9).to(16, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH, ForceFieldModel.ExtraDirection.EAST).shade(false)
					.face(Direction.SOUTH).cullface(Direction.SOUTH).uvs(0, 7, 7, 9).end()
					.face(Direction.EAST).cullface(Direction.EAST).uvs(9, 7, 16, 9).end()
					.face(Direction.DOWN).uvs(9, 9, 16, 16).end()
					.face(Direction.UP).uvs(0, 9, 7, 16).end().faces((direction, builder) -> builder.texture("#pane").emissivity(15, 15))
				.ifElse().from(7, 7, 9).to(9, 9, 16).parents(ForceFieldModel.ExtraDirection.SOUTH).shade(false).face(Direction.EAST).uvs(9, 7, 16, 9).texture("#pane").emissivity(15, 15).end()
				.ifSame().from(9, 7, 7).to(16, 9, 9).parents(ForceFieldModel.ExtraDirection.EAST).shade(false).face(Direction.SOUTH).uvs(0, 7, 7, 9).texture("#pane").emissivity(15, 15).end().end()

			.end());
    }
}
