package team.cqr.cqrepoured.tileentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import team.cqr.cqrepoured.inventory.InventoryBlockEntity;

public class BlockEntityContainer extends BlockEntity {

	protected final InventoryBlockEntity inventory;

	public BlockEntityContainer(BlockEntityType<?> type, int invSize) {
		this(type, invSize, 64);
	}

	public BlockEntityContainer(BlockEntityType<?> type, int invSize, int maxStackSize) {
		super(type);
		this.inventory = new InventoryBlockEntity(this, invSize, true) {
			@Override
			public int getMaxStackSize() {
				return maxStackSize;
			}
		};
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		this.inventory.save(compound);
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
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
