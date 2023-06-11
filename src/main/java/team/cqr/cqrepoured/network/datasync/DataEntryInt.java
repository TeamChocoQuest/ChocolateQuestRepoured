package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.network.PacketBuffer;

public class DataEntryInt extends DataEntry<Integer> {

	private int value;

	public DataEntryInt(String name, int defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public INBT write() {
		return IntNBT.valueOf(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof IntNBT) {
			this.value = ((IntNBT) nbt).getAsInt();
		}
	}

	@Override
	public void writeChanges(PacketBuffer buf) {
		buf.writeInt(this.value);
	}

	@Override
	protected void readChangesInternal(PacketBuffer buf) {
		this.value = buf.readInt();
	}

	/**
	 * @deprecated Use {@link DataEntryInt#set(int)} instead.
	 */
	@Deprecated
	@Override
	public void set(@Nonnull Integer value) {
		if (value == null) {
			return;
		}
		this.set(value);
	}

	/**
	 * @deprecated Use {@link DataEntryInt#setInternal(int)} instead.
	 */
	@Deprecated
	@Override
	protected void setInternal(@Nonnull Integer value) {
		if (value == null) {
			return;
		}
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryInt#isSavedValueEqualTo(int)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Integer value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryInt#getInt()} instead.
	 */
	@Deprecated
	@Override
	public Integer get() {
		return this.getInt();
	}

	public void set(int value) {
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	protected void setInternal(int value) {
		this.value = value;
	}

	public boolean isSavedValueEqualTo(int value) {
		return this.value == value;
	}

	public int getInt() {
		return this.value;
	}

}
