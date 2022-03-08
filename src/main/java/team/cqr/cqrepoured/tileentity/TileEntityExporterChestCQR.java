package team.cqr.cqrepoured.tileentity;

import net.minecraft.block.Block;
import net.minecraft.loot.LootTables;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.block.BlockExporterChestCQR;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class TileEntityExporterChestCQR extends TileEntityExporterChest {

	public TileEntityExporterChestCQR() {
		super(CQRBlockEntities.EXPORTER_CHEST_CQR.get());
	}

	@Override
	public ResourceLocation getLootTable() {
		Block block = this.getBlockState().getBlock();
		if (block instanceof BlockExporterChestCQR) {
			return ((BlockExporterChestCQR) block).getLootTable();
		}
		return LootTables.EMPTY;
	}

}
