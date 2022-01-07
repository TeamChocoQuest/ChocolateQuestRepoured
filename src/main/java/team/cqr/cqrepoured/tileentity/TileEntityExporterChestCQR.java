package team.cqr.cqrepoured.tileentity;

import net.minecraft.block.Block;
import net.minecraft.loot.LootTables;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import team.cqr.cqrepoured.block.BlockExporterChestCQR;

public class TileEntityExporterChestCQR extends TileEntityExporterChest {

	public TileEntityExporterChestCQR(TileEntityType<? extends TileEntityExporterChestCQR> p_i48289_1_) {
		super(p_i48289_1_);
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
