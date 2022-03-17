package team.cqr.cqrepoured.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public abstract class TileEntityExporterChest extends TileEntity {

	public TileEntityExporterChest(TileEntityType<? extends TileEntityExporterChest> type) {
		super(type);
	}

	public abstract ResourceLocation getLootTable();

}
