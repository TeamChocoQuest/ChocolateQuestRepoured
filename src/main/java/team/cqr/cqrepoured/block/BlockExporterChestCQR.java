package team.cqr.cqrepoured.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChestCQR;

public class BlockExporterChestCQR extends BlockExporterChest {

	private final ResourceLocation lootTable;

	public BlockExporterChestCQR(ResourceLocation lootTable, String resourceName) {
		this(lootTable, new ResourceLocation(resourceName));
	}

	public BlockExporterChestCQR(ResourceLocation lootTable, String resourceDomain, String resourcePath) {
		this(lootTable, new ResourceLocation(resourceDomain, resourcePath));
	}

	public BlockExporterChestCQR(ResourceLocation lootTable, ResourceLocation overlayTexture) {
		super(overlayTexture);
		this.lootTable = lootTable;
	}

	@Override
	public TileEntityExporterChest createTileEntity(World world, BlockState state) {
		return new TileEntityExporterChestCQR();
	}

	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

}
