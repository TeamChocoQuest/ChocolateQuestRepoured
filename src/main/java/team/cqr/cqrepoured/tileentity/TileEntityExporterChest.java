package team.cqr.cqrepoured.tileentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.NonNullList;

public abstract class TileEntityExporterChest extends ChestTileEntity {

	public TileEntityExporterChest(BlockEntityType<? extends TileEntityExporterChest> type) {
		super(type);
	}

	public abstract ResourceLocation getLootTable();

	@Override
	public CompoundTag save(CompoundTag pCompound) {
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
