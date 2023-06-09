package team.cqr.cqrepoured.data;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.block.*;
import team.cqr.cqrepoured.init.CQRBlocks;

import java.util.function.BiFunction;
import java.util.function.Function;

import static team.cqr.cqrepoured.data.CQRDataGenerators.*;

public class CQRBlockStateProvider extends BlockStateProvider {

	public CQRBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, CQRMain.MODID, existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		valuesOfClass(CQRBlocks.BLOCKS, Block.class).forEach(this::simpleBlock);
		valuesOfClass(CQRBlocks.BLOCKS, RotatedPillarBlock.class).forEach(this::axisBlock);
		valuesOfClass(CQRBlocks.BLOCKS, BlockTable.class).forEach(this::table);
		simpleBlock(CQRBlocks.UNLIT_TORCH.get(), models().torch(CQRBlocks.UNLIT_TORCH.getId().getPath(), blockTexture(CQRBlocks.UNLIT_TORCH.get())));
		horizontalBlock(CQRBlocks.UNLIT_TORCH_WALL.get(), models().torchWall(CQRBlocks.UNLIT_TORCH_WALL.getId().getPath(), blockTexture(CQRBlocks.UNLIT_TORCH.get())), 90);
		simpleBlock(CQRBlocks.EXPORTER.get());
		getVariantBuilder(CQRBlocks.NULL_BLOCK.get()).forAllStates(booleanPropertyMapper(BlockNull.PASSABLE, models()::cubeAll));
		simpleBlock(CQRBlocks.SPAWNER.get());
		simpleBlock(CQRBlocks.BOSS_BLOCK.get());
		simpleBlock(CQRBlocks.FORCE_FIELD_NEXUS.get(), models().getExistingFile(blockTexture(CQRBlocks.FORCE_FIELD_NEXUS.get())));
		getVariantBuilder(CQRBlocks.MAP_PLACEHOLDER.get()).forAllStates(state -> {
			Direction dir = state.getValue(BlockStateProperties.FACING);
			return ConfiguredModel.builder()
					.modelFile(models().withExistingParent(state.getBlock().getRegistryName().getPath(), blockLoc(mcLoc("item_frame_map"))))
					.rotationX(dir == Direction.DOWN ? 90 : dir.getAxis().isHorizontal() ? 0 : 270)
					.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + 180) % 360)
					.build();
		});
		getVariantBuilder(CQRBlocks.TNT.get()).forAllStates(singlePropertyMapper(BlockTNTCQR.HIDDEN, (block, hidden) -> {
			if (hidden)
				return models().getExistingFile(blockTexture(Blocks.TNT));
			ResourceLocation tex = extend(blockTexture(CQRBlocks.TNT.get()), "_hidden");
			return models().cubeBottomTop(block.getRegistryName().getPath() + "_hidden", extend(tex, "_side"), extend(tex, "_bottom"), extend(tex, "_top"));
		}));
		valuesOfType(CQRBlocks.BLOCKS, BlockExporterChest.class).forEach(block -> simpleBlock(block, models().getBuilder(block.getRegistryName().getPath()).texture("particle", blockLoc(mcLoc("oak_planks")))));
		simpleBlock(CQRBlocks.PHYLACTERY.get(), models().getExistingFile(blockTexture(CQRBlocks.PHYLACTERY.get())));
		getVariantBuilder(CQRBlocks.POISONOUS_WEB.get()).forAllStates(intPropertyMapper(BlockPoisonousWeb.AGE, models()::cross));
	}

	public <T extends Comparable<T>> void test(Block block, Property<T> property, BiFunction<Block, T, ModelFile> modelFileFunc) {
		VariantBlockStateBuilder v = getVariantBuilder(block);
		property.getAllValues().forEach(pair -> {
			v.partialState().with(pair.getProperty(), pair.value()).modelForState().modelFile(modelFileFunc.apply(block, pair.value())).addModel();
		});
	}

	public <T extends Comparable<T>> void test(Block block, Property<T> property, Function<T, String> suffixFunc, BiFunction<String, ResourceLocation, ModelFile> modelFileFunc) {
		test(block, property, (b, t) -> {
			ResourceLocation registryName = blockTexture(b);
			String suffix = suffixFunc.apply(t);
			return modelFileFunc.apply(registryName.getPath() + suffix, extend(registryName, suffix));
		});
	}

	public void test(Block block, BooleanProperty property, BiFunction<String, ResourceLocation, ModelFile> modelFileFunc) {
		test(block, property, flag -> flag ? "_" + property.getName() : "", modelFileFunc);
	}

	public void test(Block block, IntegerProperty property, BiFunction<String, ResourceLocation, ModelFile> modelFileFunc) {
		test(block, property, i -> "_" + i, modelFileFunc);
	}

	public <T extends Comparable<T>> Function<BlockState, ConfiguredModel[]> singlePropertyMapper(Property<T> property, BiFunction<Block, T, ModelFile> modelFileFunc) {
		return state -> {
			T t = state.getValue(property);
			return ConfiguredModel.builder()
					.modelFile(modelFileFunc.apply(state.getBlock(), t))
					.build();
		};
	}

	public <T extends Comparable<T>> Function<BlockState, ConfiguredModel[]> simplePropertyMapper(Property<T> property, Function<T, String> suffixFunc, BiFunction<String, ResourceLocation, ModelFile> modelFileFunc) {
		return singlePropertyMapper(property, (block, t) -> {
			ResourceLocation registryName = blockTexture(block);
			String suffix = suffixFunc.apply(t);
			return modelFileFunc.apply(registryName.getPath() + suffix, extend(registryName, suffix));
		});
	}

	public Function<BlockState, ConfiguredModel[]> booleanPropertyMapper(Property<Boolean> property, BiFunction<String, ResourceLocation, ModelFile> modelFileFunc) {
		return simplePropertyMapper(property, flag -> flag ? "_" + property.getName() : "", modelFileFunc);
	}

	public Function<BlockState, ConfiguredModel[]> intPropertyMapper(Property<Integer> property, BiFunction<String, ResourceLocation, ModelFile> modelFileFunc) {
		return simplePropertyMapper(property, i -> "_" + i, modelFileFunc);
	}

	public void table(BlockTable block) {
		String s = block.getRegistryName().getPath();
		s = s.substring(0, s.lastIndexOf('_'));
		getVariantBuilder(block)
				.partialState().with(BlockTable.TOP, false).modelForState().modelFile(table(s)).addModel()
				.partialState().with(BlockTable.TOP, true).modelForState().modelFile(tableTop(s)).addModel();
	}

	public ModelFile table(String woodType) {
		return models().withExistingParent(woodType + "_table", modLoc("template_table")).texture("wood", blockLoc(mcLoc(woodType + "_planks")));
	}

	public ModelFile tableTop(String woodType) {
		return models().withExistingParent(woodType + "_table_top", modLoc("template_table_top")).texture("wood", blockLoc(mcLoc(woodType + "_planks")));
	}

}
