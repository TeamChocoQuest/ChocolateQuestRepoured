package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ShortNBT;

public class DataEntryShort extends DataEntry<Short> {

	private short value;

	public DataEntryShort(String name, short defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public INBT write() {
		return ShortNBT.valueOf(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof ShortNBT) {
			this.value = ((ShortNBT) nbt).getAsShort();
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeShort(this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = buf.readShort();
	}

	/**
	 * @deprecated Use {@link DataEntryShort#set(short)} instead.
	 */
	@Deprecated
	@Override
	public void set(@Nonnull Short value) {
		if (value == null) {
			return;
		}
		this.set(value);
	}

	/**
	 * @deprecated Use {@link DataEntryShort#setInternal(short)} instead.
	 */
	@Deprecated
	@Override
	protected void setInternal(@Nonnull Short value) {
		if (value == null) {
			return;
		}
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryShort#isSavedValueEqualTo(short)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Short value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryShort#getShort()} instead.
	 */
	@Deprecated
	@Override
	public Short get() {
		return this.getShort();
	}

	public void set(short value) {
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	protected void setInternal(short value) {
		this.value = value;
	}

	public boolean isSavedValueEqualTo(short value) {
		return this.value == value;
	}

	public short getShort() {
		return this.value;
	}

}
