package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import team.cqr.cqrepoured.block.BlockExporterChestFixed;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class TileEntityExporterChestFixed extends TileEntityExporterChest {

	public TileEntityExporterChestFixed(BlockPos pos, BlockState state) {
		super(CQRBlockEntities.EXPORTER_CHEST_CQR.get(), pos, state);
	}

	@Override
	public ResourceLocation getLootTable() {
		Block block = this.getBlockState().getBlock();
		if (block instanceof BlockExporterChestFixed) {
			return ((BlockExporterChestFixed) block).getLootTable();
		}
		return BuiltInLootTables.EMPTY;
	}

}
