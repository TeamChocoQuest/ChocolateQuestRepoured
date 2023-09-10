package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileEntityExporterChest extends ChestBlockEntity {

	public TileEntityExporterChest(BlockEntityType<? extends TileEntityExporterChest> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public abstract ResourceLocation getLootTable();

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.remove("Items");
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
