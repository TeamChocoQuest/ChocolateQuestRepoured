package team.cqr.cqrepoured.objects.blocks;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public ResourceLocation getLootTable(World world, BlockPos pos) {
		return this.lootTable;
	}

}
