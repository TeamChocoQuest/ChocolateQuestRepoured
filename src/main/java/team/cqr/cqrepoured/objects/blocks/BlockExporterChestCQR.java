package team.cqr.cqrepoured.objects.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import team.cqr.cqrepoured.tileentity.TileEntityExporterChest;

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
	public TileEntityExporterChest createTileEntity(World world, IBlockState state) {
		return new TileEntityExporterChest() {

			@Override
			public ResourceLocation getLootTable() {
				return lootTable;
			}

		};
	}

	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

}
