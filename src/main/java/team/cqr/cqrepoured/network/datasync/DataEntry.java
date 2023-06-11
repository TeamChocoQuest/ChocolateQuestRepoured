package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public abstract class DataEntry<T> {

	private TileEntityDataManager dataManager;
	private int id = -1;
	private final String name;
	private final boolean isClientModificationAllowed;
	protected boolean isDirty = false;

	protected DataEntry(String name, boolean isClientModificationAllowed) {
		this.name = name;
		this.isClientModificationAllowed = isClientModificationAllowed;
	}
	
	public abstract Tag write();

	public void read(Tag nbt) {
		this.readInternal(nbt);
	}

	protected abstract void readInternal(Tag nbt);

	public abstract void writeChanges(FriendlyByteBuf buf);

	public void readChanges(FriendlyByteBuf buf) {
		this.readChangesInternal(buf);
		this.onValueChanged();
	}

	protected abstract void readChangesInternal(FriendlyByteBuf buf);

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
			throw new NullPointerException();
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
		Level world = this.dataManager.getTileEntity().getLevel();
		if (world == null) {
			return;
		}
		if (world.isClientSide && !this.isClientModificationAllowed) {
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
