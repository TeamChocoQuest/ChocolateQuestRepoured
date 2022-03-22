package team.cqr.cqrepoured.world.processor.wall;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StructureBlock;
import net.minecraft.state.properties.StructureMode;
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

public class ProcessorWallOuterTower extends StructureProcessor {

	public static final ProcessorWallOuterTower INSTANCE = new ProcessorWallOuterTower();
	public static final Codec<ProcessorWallOuterTower> CODEC = Codec.unit(() -> INSTANCE);
	
	@Override
	protected IStructureProcessorType<?> getType() {
		return CQRStructureProcessors.PROCESSOR_WALL_OUTER_TOWER;
	}
	
	@Override
	public BlockInfo process(IWorldReader worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, BlockInfo blockInfoLocal, BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, Template template) {
		if (blockInfoGlobal.state.is(Blocks.STRUCTURE_BLOCK)) {
			if (blockInfoGlobal.state.getValue(StructureBlock.MODE) == StructureMode.DATA) {
				String dataEntry = blockInfoGlobal.nbt.getString("metadata");
				if (dataEntry != null && !dataEntry.isEmpty()) {
					if (dataEntry.equalsIgnoreCase("cqrepoured:wall_outer")) {
						ChunkPos currentChunkPos = new ChunkPos(blockInfoGlobal.pos);
						IChunk currentChunk = worldReader.getChunk(currentChunkPos.x, currentChunkPos.z);
						BlockState randomBlock;

						// Always replace the glass itself with stone bricks
						randomBlock = Blocks.POLISHED_ANDESITE.defaultBlockState();
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
				}
			}
		}

		return blockInfoGlobal;
	}

}
