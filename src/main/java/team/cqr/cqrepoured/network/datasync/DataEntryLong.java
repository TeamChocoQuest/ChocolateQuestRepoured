package team.cqr.cqrepoured.network.datasync;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public class DataEntryLong extends DataEntry<Long> {

	private long value;

	public DataEntryLong(String name, long defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public INBT write() {
		return LongNBT.valueOf(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof LongNBT) {
			this.value = ((LongNBT) nbt).getAsLong();
		}
	}

	@Override
	public void writeChanges(PacketBuffer buf) {
		buf.writeLong(this.value);
	}

	@Override
	protected void readChangesInternal(PacketBuffer buf) {
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
