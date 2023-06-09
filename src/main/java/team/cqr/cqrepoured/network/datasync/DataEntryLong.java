package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryLong extends DataEntry<Long> {

	private long value;

	public DataEntryLong(String name, long defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public Tag write() {
		return LongTag.valueOf(this.value);
	}

	@Override
	protected void readInternal(Tag nbt) {
		if (nbt instanceof LongTag) {
			this.value = ((LongTag) nbt).getAsLong();
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeLong(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
		this.value = buf.readLong();
	}

	/**
	 * @deprecated Use {@link DataEntryLong#set(long)} instead.
	 */
	@Deprecated
	@Override
	public void set(@Nonnull Long value) {
		if (value == null) {
			return;
		}
		this.set(value);
	}

	/**
	 * @deprecated Use {@link DataEntryLong#setInternal(long)} instead.
	 */
	@Deprecated
	@Override
	protected void setInternal(@Nonnull Long value) {
		if (value == null) {
			return;
		}
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryLong#isSavedValueEqualTo(long)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Long value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryLong#getLong()} instead.
	 */
	@Deprecated
	@Override
	public Long get() {
		return this.getLong();
	}

	public void set(long value) {
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	protected void setInternal(long value) {
		this.value = value;
	}

	public boolean isSavedValueEqualTo(long value) {
		return this.value == value;
	}

	public long getLong() {
		return this.value;
	}

}
