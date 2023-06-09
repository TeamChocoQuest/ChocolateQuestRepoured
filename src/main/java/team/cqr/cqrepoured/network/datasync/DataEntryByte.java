package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryByte extends DataEntry<Byte> {

	private byte value;

	public DataEntryByte(String name, byte defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public Tag write() {
		return ByteTag.valueOf(this.value);
	}

	@Override
	protected void readInternal(Tag nbt) {
		if (nbt instanceof ByteTag) {
			this.value = ((ByteTag) nbt).getAsByte();
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeByte(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
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
