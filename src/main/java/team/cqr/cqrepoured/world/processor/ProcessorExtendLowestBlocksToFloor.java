package team.cqr.cqrepoured.world.processor;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRStructureProcessors;
import team.cqr.cqrepoured.world.structure.generation.dungeons.PlacementSettings;

public class ProcessorExtendLowestBlocksToFloor extends StructureProcessor {
	
	public static final ProcessorExtendLowestBlocksToFloor INSTANCE = new ProcessorExtendLowestBlocksToFloor();
	public static final Codec<ProcessorExtendLowestBlocksToFloor> CODEC = Codec.unit(() -> INSTANCE);

	@Override
	protected StructureProcessorType<?> getType() {
		return CQRStructureProcessors.PROCESSOR_EXTEND_LOWEST_TO_FLOOR;
	}

	@Override
	public BlockInfo process(BlockGetter worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, BlockInfo blockInfoLocal, BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, Template template) {
		if (!(blockInfoGlobal.state.is(Blocks.AIR) || blockInfoGlobal.state.is(Blocks.CAVE_AIR) || blockInfoGlobal.state.is(Blocks.STRUCTURE_VOID) || blockInfoGlobal.state.is(CQRBlocks.NULL_BLOCK.get())) && blockInfoLocal.pos.getY() == 0) {
			ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
			IChunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);

			// Always replace the glass itself with stone bricks
			final BlockState stateForExtension = blockInfoGlobal.state;
			currentChunk.setBlockState(blockInfoGlobal.pos, stateForExtension, false);
			blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, stateForExtension, blockInfoGlobal.nbt);

			// Straight line down
			BlockPos.Mutable mutable = blockInfoGlobal.pos.below().mutable();
			BlockState currBlock = worldReader.getBlockState(mutable);

			while (mutable.getY() > 0 && (!currBlock.is(BlockTags.BASE_STONE_OVERWORLD))) {
				worldReader.getChunk(mutable).setBlockState(mutable, stateForExtension, false);

				mutable.move(Direction.DOWN);
				currBlock = worldReader.getBlockState(mutable);
			}
		}

		return blockInfoGlobal;
	}
	
}
