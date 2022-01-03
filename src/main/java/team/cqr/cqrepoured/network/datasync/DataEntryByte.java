package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;

public class DataEntryByte extends DataEntry<Byte> {

	private byte value;

	public DataEntryByte(String name, byte defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public INBT write() {
		return ByteNBT.valueOf(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof ByteNBT) {
			this.value = ((ByteNBT) nbt).getAsByte();
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeByte(this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = buf.readByte();
	}

	/**
	 * @deprecated Use {@link DataEntryByte#set(byte)} instead.
	 */
	@Deprecated
	@Override
	public void set(@Nonnull Byte value) {
		if (value == null) {
			return;
		}
		this.set(value);
	}

	/**
	 * @deprecated Use {@link DataEntryByte#setInternal(byte)} instead.
	 */
	@Deprecated
	@Override
	protected void setInternal(@Nonnull Byte value) {
		if (value == null) {
			return;
		}
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryByte#isSavedValueEqualTo(byte)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Byte value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryByte#getByte()} instead.
	 */
	@Deprecated
	@Override
	public Byte get() {
		return this.getByte();
	}

	public void set(byte value) {
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	protected void setInternal(byte value) {
		this.value = value;
	}

	public boolean isSavedValueEqualTo(byte value) {
		return this.value == value;
	}

	public byte getByte() {
		return this.value;
	}

}
