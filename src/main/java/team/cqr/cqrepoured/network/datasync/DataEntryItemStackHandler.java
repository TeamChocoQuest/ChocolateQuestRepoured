package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class DataEntryItemStackHandler extends DataEntryObject<ItemStackHandler> {

	private boolean canMarkDirty = true;
	private final boolean[] dirtySlots;

	public DataEntryItemStackHandler(String name, CustomItemStackHandler itemStackHandler, boolean isClientModificationAllowed) {
		super(name, itemStackHandler, isClientModificationAllowed);
		((CustomItemStackHandler) this.value).entry = this;
		this.dirtySlots = new boolean[itemStackHandler.getSlots()];
	}

	@Override
	public INBT write() {
		return this.value.serializeNBT();
	}

	@Override
	protected void readInternal(INBT nbt) {
		this.canMarkDirty = false;
		if (nbt instanceof CompoundNBT) {
			this.value.deserializeNBT((CompoundNBT) nbt);
		}
		this.canMarkDirty = true;
	}

	@Override
	public void writeChanges(PacketBuffer buf) {
		int size = 0;
		for (boolean dirtySlot : this.dirtySlots) {
			if (dirtySlot) {
				size++;
			}
		}
		buf.writeInt(size);
		for (int i = 0; i < this.value.getSlots(); i++) {
			if (this.dirtySlots[i]) {
				buf.writeVarInt(i);
				buf.writeItem(this.value.getStackInSlot(i));
			}
		}
	}

	@Override
	protected void readChangesInternal(PacketBuffer buf) {
		this.canMarkDirty = false;
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			this.value.setStackInSlot(buf.readVarInt(), buf.readItem());
		}
		this.canMarkDirty = true;
	}

	@Override
	public void set(ItemStackHandler value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearDirty() {
		super.clearDirty();
		for (int i = 0; i < this.value.getSlots(); i++) {
			this.dirtySlots[i] = false;
		}
	}

	public void markSlotDirty(int slot) {
		if (slot < 0 || slot >= this.dirtySlots.length) {
			return;
		}
		TileEntityDataManager dataManager = this.getDataManager();
		if (dataManager == null) {
			return;
		}
		World world = dataManager.getTileEntity().getLevel();
		if (world == null) {
			return;
		}
		if (world.isClientSide && !this.isClientModificationAllowed()) {
			return;
		}
		this.isDirty = true;
		this.dirtySlots[slot] = true;
		dataManager.markDirty();
	}

	public static class CustomItemStackHandler extends ItemStackHandler {

		private DataEntryItemStackHandler entry;

		public CustomItemStackHandler(int size) {
			super(size);
		}

		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			if (this.entry != null) {
				this.entry.onValueChanged();
				if (this.entry.canMarkDirty) {
					this.entry.markSlotDirty(slot);
				}
			}
		}

	}

}
