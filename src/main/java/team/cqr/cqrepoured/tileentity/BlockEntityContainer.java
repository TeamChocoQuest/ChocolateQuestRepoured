package team.cqr.cqrepoured.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import team.cqr.cqrepoured.inventory.InventoryBlockEntity;

public class BlockEntityContainer extends TileEntity {

	protected final InventoryBlockEntity inventory;

	public BlockEntityContainer(TileEntityType<?> type, int invSize) {
		this(type, invSize, 64);
	}

	public BlockEntityContainer(TileEntityType<?> type, int invSize, int maxStackSize) {
		super(type);
		this.inventory = new InventoryBlockEntity(this, invSize, true) {
			@Override
			public int getMaxStackSize() {
				return maxStackSize;
			}
		};
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		this.inventory.save(compound);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.inventory.load(compound);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return LazyOptional.of(() -> new InvWrapper(this.inventory)).cast();
		}
		return super.getCapability(cap, side);
	}

	public InventoryBlockEntity getInventory() {
		return this.inventory;
	}

}
