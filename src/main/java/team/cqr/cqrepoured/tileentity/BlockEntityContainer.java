package team.cqr.cqrepoured.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.InvWrapper;
import team.cqr.cqrepoured.inventory.InventoryBlockEntity;

public abstract class BlockEntityContainer extends BaseContainerBlockEntity {

	protected final InventoryBlockEntity inventory;

	public BlockEntityContainer(BlockEntityType<?> type, int invSize, BlockPos pos, BlockState state) {
		this(type, invSize, 64, pos, state);
	}

	public BlockEntityContainer(BlockEntityType<?> type, int invSize, int maxStackSize, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.inventory = new InventoryBlockEntity(this, invSize, true) {
			@Override
			public int getMaxStackSize() {
				return maxStackSize;
			}
		};
	}
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag compound = super.serializeNBT();
		this.inventory.save(compound);
		return compound;
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		this.inventory.load(nbt);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return LazyOptional.of(() -> new InvWrapper(this.inventory)).cast();
		}
		return super.getCapability(cap, side);
	}

	public InventoryBlockEntity getInventory() {
		return this.inventory;
	}

	@Override
	public int getContainerSize() {
		return this.inventory.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return this.inventory.isEmpty();
	}

	@Override
	public ItemStack getItem(int pSlot) {
		return this.inventory.getItem(pSlot);
	}

	@Override
	public ItemStack removeItem(int pSlot, int pAmount) {
		return this.inventory.removeItem(pSlot, pAmount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int pSlot) {
		return this.inventory.removeItemNoUpdate(pSlot);
	}

	@Override
	public void setItem(int pSlot, ItemStack pStack) {
		this.inventory.setItem(pSlot, pStack);
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return this.inventory.stillValid(pPlayer);
	}

	@Override
	public void clearContent() {
		this.inventory.clearContent();
	}

}
