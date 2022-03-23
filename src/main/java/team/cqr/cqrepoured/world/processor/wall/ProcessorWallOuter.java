package team.cqr.cqrepoured.world.processor.wall;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import team.cqr.cqrepoured.init.CQRStructureProcessors;

public class ProcessorWallOuter extends StructureProcessor {

	public static final ProcessorWallOuter INSTANCE = new ProcessorWallOuter();
	public static final Codec<ProcessorWallOuter> CODEC = Codec.unit(() -> INSTANCE);

	@Override
	protected IStructureProcessorType<?> getType() {
		return CQRStructureProcessors.PROCESSOR_WALL_OUTER;
	}

	@Override
	public BlockInfo process(IWorldReader worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, BlockInfo blockInfoLocal, BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, Template template) {
		if (blockInfoGlobal.state.is(Blocks.LIGHT_GRAY_STAINED_GLASS) && blockInfoLocal.pos.getY() == 0) {
			ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
			IChunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
			BlockState randomBlock;

			// Always replace the glass itself with stone bricks
			randomBlock = Blocks.STONE_BRICKS.defaultBlockState();
			currentChunk.setBlockState(blockInfoGlobal.pos, randomBlock, false);
			blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, randomBlock, blockInfoGlobal.nbt);

			// Straight line down
			BlockPos.Mutable mutable = blockInfoGlobal.pos.below().mutable();
			BlockState currBlock = worldReader.getBlockState(mutable);

			while (mutable.getY() > 0 && (!currBlock.is(BlockTags.BASE_STONE_OVERWORLD))) {
				worldReader.getChunk(mutable).setBlockState(mutable, randomBlock, false);

				mutable.move(Direction.DOWN);
				currBlock = worldReader.getBlockState(mutable);
			}
		}

		return blockInfoGlobal;
	}

}
