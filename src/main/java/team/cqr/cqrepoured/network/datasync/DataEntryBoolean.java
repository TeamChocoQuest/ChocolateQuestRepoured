package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryBoolean extends DataEntry<Boolean> {

	private boolean value;

	public DataEntryBoolean(String name, boolean defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public Tag write() {
		return ByteTag.valueOf((byte) (this.value ? 1 : 0));
	}

	@Override
	protected void readInternal(Tag nbt) {
		if (nbt instanceof ByteTag) {
			this.value = ((ByteTag) nbt).getAsByte() != 0;
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeBoolean(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
		this.value = buf.readBoolean();
	}

	/**
	 * @deprecated Use {@link DataEntryBoolean#set(boolean)} instead.
	 */
	@Deprecated
	@Override
	public void set(@Nonnull Boolean value) {
		if (value == null) {
			return;
		}
		this.set(value);
	}

	/**
	 * @deprecated Use {@link DataEntryBoolean#setInternal(boolean)} instead.
	 */
	@Deprecated
	@Override
	protected void setInternal(@Nonnull Boolean value) {
		if (value == null) {
			return;
		}
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryBoolean#isSavedValueEqualTo(boolean)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Boolean value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryBoolean#getBoolean()} instead.
	 */
	@Deprecated
	@Override
	public Boolean get() {
		return this.getBoolean();
	}

	public void set(boolean value) {
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	protected void setInternal(boolean value) {
		this.value = value;
	}

	public boolean isSavedValueEqualTo(boolean value) {
		return this.value == value;
	}

	public boolean getBoolean() {
		return this.value;
	}

}
