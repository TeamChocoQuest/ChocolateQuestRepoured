package team.cqr.cqrepoured.world.processor;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.init.CQRStructureProcessors;

public class ProcessorExtendLowestBlocksToFloor extends StructureProcessor {
	
	public static final ProcessorExtendLowestBlocksToFloor INSTANCE = new ProcessorExtendLowestBlocksToFloor();
	public static final Codec<ProcessorExtendLowestBlocksToFloor> CODEC = Codec.unit(() -> INSTANCE);

	@Override
	protected StructureProcessorType<?> getType() {
		return CQRStructureProcessors.PROCESSOR_EXTEND_LOWEST_TO_FLOOR;
	}

	public StructureBlockInfo process(LevelReader worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, StructureBlockInfo blockInfoLocal, StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData, StructureTemplate template) {
		if (!(blockInfoGlobal.state().is(Blocks.AIR) || blockInfoGlobal.state().is(Blocks.CAVE_AIR) || blockInfoGlobal.state().is(Blocks.STRUCTURE_VOID) || blockInfoGlobal.state().is(CQRBlocks.NULL_BLOCK.get())) && blockInfoLocal.pos().getY() == 0) {
			ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos());
			ChunkAccess currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);

			// Always replace the glass itself with stone bricks
			final BlockState stateForExtension = blockInfoGlobal.state();
			currentChunk.setBlockState(blockInfoGlobal.pos(), stateForExtension, false);
			blockInfoGlobal = new StructureBlockInfo(blockInfoGlobal.pos(), stateForExtension, blockInfoGlobal.nbt());

			// Straight line down
			MutableBlockPos mutable = blockInfoGlobal.pos().below().mutable();
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
