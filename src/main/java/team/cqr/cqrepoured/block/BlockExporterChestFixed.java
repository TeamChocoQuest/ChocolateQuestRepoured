package team.cqr.cqrepoured.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.level.block.state.BlockState;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestFixed;

public class BlockExporterChestFixed extends BlockExporterChest {

	private final ResourceLocation lootTable;

	public BlockExporterChestFixed(ResourceLocation lootTable) {
		this.lootTable = lootTable;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityExporterChestFixed();
	}

	public ResourceLocation getLootTableCQR() {
		return lootTable;
	}

}
