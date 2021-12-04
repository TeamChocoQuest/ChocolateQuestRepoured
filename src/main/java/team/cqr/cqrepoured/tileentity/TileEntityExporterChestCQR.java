package team.cqr.cqrepoured.tileentity;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import team.cqr.cqrepoured.objects.blocks.BlockExporterChestCQR;

public class TileEntityExporterChestCQR extends TileEntityExporterChest {

	@Override
	public ResourceLocation getLootTable() {
		Block block = this.getBlockType();
		if (block instanceof BlockExporterChestCQR) {
			return ((BlockExporterChestCQR) block).getLootTable();
		}
		return LootTableList.EMPTY;
	}

}
