package team.cqr.cqrepoured.tileentity;

import net.minecraft.loot.LootTables;
import net.minecraft.resources.ResourceLocation;
import team.cqr.cqrepoured.block.BlockExporterChestFixed;
import team.cqr.cqrepoured.init.CQRBlockEntities;

public class TileEntityExporterChestFixed extends TileEntityExporterChest {

	public TileEntityExporterChestFixed() {
		super(CQRBlockEntities.EXPORTER_CHEST_CQR.get());
	}

	@Override
	public ResourceLocation getLootTable() {
		Block block = this.getBlockState().getBlock();
		if (block instanceof BlockExporterChestFixed) {
			return ((BlockExporterChestFixed) block).getLootTable();
		}
		return LootTables.EMPTY;
	}

}
