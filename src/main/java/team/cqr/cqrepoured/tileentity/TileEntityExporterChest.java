package team.cqr.cqrepoured.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public abstract class TileEntityExporterChest extends ChestTileEntity {

	public TileEntityExporterChest(TileEntityType<? extends TileEntityExporterChest> type) {
		super(type);
	}

	public abstract ResourceLocation getLootTable();

	@Override
	public CompoundNBT save(CompoundNBT pCompound) {
		super.save(pCompound);
		pCompound.remove("Items");
		return pCompound;
	}

	@Override
	public int getContainerSize() {
		return 0;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return NonNullList.withSize(0, ItemStack.EMPTY);
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItems) {

	}

}
