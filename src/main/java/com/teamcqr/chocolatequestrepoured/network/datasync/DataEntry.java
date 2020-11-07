package com.teamcqr.chocolatequestrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.world.World;

public abstract class DataEntry<T> {

	private TileEntityDataManager dataManager;
	private int id = -1;
	private final String name;
	private final boolean isClientModificationAllowed;
	protected boolean isDirty = false;

	public DataEntry(String name, boolean isClientModificationAllowed) {
		this.name = name;
		this.isClientModificationAllowed = isClientModificationAllowed;
	}

	public abstract NBTBase write();

	public void read(NBTBase nbt) {
		this.read(nbt);
		this.onValueChanged();
	}

	protected abstract void readInternal(NBTBase nbt);

	public abstract void writeChanges(ByteBuf buf);

	public void readChanges(ByteBuf buf) {
		this.readChanges(buf);
		this.onValueChanged();
	}

	protected abstract void readChangesInternal(ByteBuf buf);

	public void setDataManagerAndId(TileEntityDataManager dataManager, int id) {
		if (this.dataManager == null && dataManager != null) {
			this.dataManager = dataManager;
			this.id = id;
		}
	}

	public TileEntityDataManager getDataManager() {
		return this.dataManager;
	}

	public int getId() {
		return this.id;
	}

	public boolean isRegistered() {
		return this.dataManager != null;
	}

	public void set(@Nonnull T value) {
		if (value == null) {
			return;
		}
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	public abstract boolean isSavedValueEqualTo(T value);

	protected abstract void setInternal(@Nonnull T value);

	protected void onValueChanged() {
		if (this.dataManager == null) {
			return;
		}
		this.dataManager.onDataEntryChanged(this);
	}

	public abstract T get();

	public String getName() {
		return this.name;
	}

	public boolean isClientModificationAllowed() {
		return this.isClientModificationAllowed;
	}

	public void markDirty() {
		if (this.dataManager == null) {
			return;
		}
		World world = this.dataManager.getTileEntity().getWorld();
		if (world == null) {
			return;
		}
		if (world.isRemote && !this.isClientModificationAllowed) {
			return;
		}
		this.isDirty = true;
		this.dataManager.markDirty();
	}

	public void clearDirty() {
		this.isDirty = false;
	}

	public boolean isDirty() {
		return this.isDirty;
	}

}
