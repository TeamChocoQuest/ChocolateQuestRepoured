package team.cqr.cqrepoured.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityBoss extends TileEntity {

	public TileEntityBoss(TileEntityType<?> p_i48289_1_) {
		super(p_i48289_1_);
	}

	public final ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			if (TileEntityBoss.this.level != null && !TileEntityBoss.this.level.isClientSide) {
				TileEntityBoss.this.setChanged();
			}
		}
	};

	/*@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
	}*/

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		compound = super.save(compound);
		compound.put("inventory", this.inventory.serializeNBT());
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.inventory.deserializeNBT(compound.getCompound("inventory"));
	}

}
