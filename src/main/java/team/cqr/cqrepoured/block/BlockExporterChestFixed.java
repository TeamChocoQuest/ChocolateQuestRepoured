package team.cqr.cqrepoured.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestFixed;

public class BlockExporterChestFixed extends BlockExporterChest {

	private final ResourceLocation lootTable;

	public BlockExporterChestFixed(ResourceLocation lootTable) {
		this.lootTable = lootTable;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TileEntityExporterChestFixed();
	}

	public ResourceLocation getLootTableCQR() {
		return lootTable;
	}

}
