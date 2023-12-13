package team.cqr.cqrepoured.world.processor;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import team.cqr.cqrepoured.block.BlockExporterChest;
import team.cqr.cqrepoured.init.CQRStructureProcessors;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

public class ProcessorLootChest extends StructureProcessor {
	
	public static final ProcessorLootChest INSTANCE = new ProcessorLootChest();
	public static final Codec<ProcessorLootChest> CODEC = Codec.unit(() -> INSTANCE);

	@Override
	protected StructureProcessorType<?> getType() {
		return CQRStructureProcessors.PROCESSOR_LOOT_CHEST;
	}
	
	@Override
	public StructureBlockInfo process(LevelReader worldReader, BlockPos jigsawPos, BlockPos jigsawPieceBottomCenterPos, StructureBlockInfo blockInfoLocal, StructureBlockInfo blockInfoGlobal, StructurePlaceSettings structurePlacementData, StructureTemplate template) {
		if(blockInfoGlobal != null && blockInfoGlobal.state() != null && blockInfoGlobal.state().getBlock() != null) {
			BlockState inputState = blockInfoGlobal.state();
			if(inputState.getBlock() instanceof BlockExporterChest) {
				BlockExporterChest bec = (BlockExporterChest)inputState.getBlock();
				if(bec.hasTileEntity(blockInfoGlobal.state())) {
					BlockEntity tile = worldReader.getBlockEntity(blockInfoGlobal.pos());
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
					worldReader.getChunk(blockInfoGlobal.pos()).setBlockState(blockInfoGlobal.pos(), resultState, false);
					if (resultState.getBlock() instanceof EntityBlock eb) {
						BlockEntity chestTile = eb.newBlockEntity(blockInfoGlobal.pos(), resultState);
						if(chestTile instanceof RandomizableContainerBlockEntity tec) {
							long seed = structurePlacementData.getRandom(blockInfoGlobal.pos()).nextLong();
							tec.setLootTable(loottable, seed);
						}
					}
				}
			}
		}
		
		return blockInfoGlobal;
	}

}
