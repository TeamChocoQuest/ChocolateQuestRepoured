package com.teamcqr.chocolatequestrepoured.network.datasync;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
	public NBTBase write() {
		return this.value.serializeNBT();
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		this.canMarkDirty = false;
		if (nbt instanceof NBTTagCompound) {
			this.value.deserializeNBT((NBTTagCompound) nbt);
		}
		this.canMarkDirty = true;
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		int size = 0;
		for (boolean dirtySlot : this.dirtySlots) {
			if (dirtySlot) {
				size++;
			}
		}
		buf.writeInt(size);
		for (int i = 0; i < this.value.getSlots(); i++) {
			if (this.dirtySlots[i]) {
				ByteBufUtils.writeVarInt(buf, i, 5);
				ByteBufUtils.writeItemStack(buf, this.value.getStackInSlot(i));
			}
		}
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.canMarkDirty = false;
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			this.value.setStackInSlot(ByteBufUtils.readVarInt(buf, 5), ByteBufUtils.readItemStack(buf));
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
		World world = dataManager.getTileEntity().getWorld();
		if (world == null) {
			return;
		}
		if (world.isRemote && !this.isClientModificationAllowed()) {
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
