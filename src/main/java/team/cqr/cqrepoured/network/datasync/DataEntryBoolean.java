package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

public class DataEntryBoolean extends DataEntry<Boolean> {

	private boolean value;

	public DataEntryBoolean(String name, boolean defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public NBTBase write() {
		return new NBTTagByte((byte) (this.value ? 1 : 0));
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagByte) {
			this.value = ((NBTTagByte) nbt).getByte() != 0;
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeBoolean(this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
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
		this.set(value.booleanValue());
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
		this.setInternal(value.booleanValue());
	}

	/**
	 * @deprecated Use {@link DataEntryBoolean#isSavedValueEqualTo(boolean)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Boolean value) {
		return this.isSavedValueEqualTo(value.booleanValue());
	}

	/**
	 * @deprecated Use {@link DataEntryBoolean#getBoolean()} instead.
	 */
	@Deprecated
	@Override
	public Boolean get() {
		return Boolean.valueOf(this.getBoolean());
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
