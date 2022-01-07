package team.cqr.cqrepoured.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

public abstract class TileEntityExporterChest extends TileEntity {

	public TileEntityExporterChest(TileEntityType<? extends TileEntityExporterChest> p_i48289_1_) {
		super(p_i48289_1_);
		// TODO Auto-generated constructor stub
	}

	public abstract ResourceLocation getLootTable();

}
