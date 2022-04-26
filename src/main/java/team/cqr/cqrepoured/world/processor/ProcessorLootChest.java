package team.cqr.cqrepoured.world.processor;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.init.CQRStructureProcessors;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

public class ProcessorLootChest extends StructureProcessor {
	
	public static final ProcessorLootChest INSTANCE = new ProcessorLootChest();
	public static final Codec<ProcessorLootChest> CODEC = Codec.unit(() -> INSTANCE);

	@Override
	protected IStructureProcessorType<?> getType() {
		return CQRStructureProcessors.PROCESSOR_LOOT_CHEST;
	}
	
	@Override
	public BlockInfo process(IWorldReader worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, BlockInfo blockInfoLocal, BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, Template template) {
		if(blockInfoGlobal != null && blockInfoGlobal.state != null && blockInfoGlobal.state.getBlock() != null) {
			BlockState inputState = blockInfoGlobal.state;
			if(inputState.getBlock() instanceof BlockExporterChest) {
				BlockExporterChest bec = (BlockExporterChest)inputState.getBlock();
				if(bec.hasTileEntity(blockInfoGlobal.state)) {
					TileEntity tile = worldReader.getBlockEntity(blockInfoGlobal.pos);
					if(tile == null) {
						return blockInfoGlobal;
					}
					if(!(tile instanceof TileEntityExporterChest)) {
						return blockInfoGlobal;
					}
					TileEntityExporterChest teec = (TileEntityExporterChest)tile;
					ResourceLocation loottable = teec.getLootTable();
					BlockState resultState = Blocks.CHEST.defaultBlockState()
							.setValue(ChestBlock.FACING, inputState.getValue(ChestBlock.FACING))
							.setValue(ChestBlock.WATERLOGGED, inputState.getValue(ChestBlock.WATERLOGGED));
					worldReader.getChunk(blockInfoGlobal.pos).setBlockState(blockInfoGlobal.pos, resultState, false);
					TileEntity chestTile = resultState.createTileEntity(worldReader);
					if(chestTile instanceof LockableLootTileEntity) {
						LockableLootTileEntity tec = (LockableLootTileEntity)chestTile;
						long seed = structurePlacementData.getRandom(blockInfoGlobal.pos).nextLong();
						tec.setLootTable(loottable, seed);
					}
				}
			}
		}
		
		return blockInfoGlobal;
	}

}
