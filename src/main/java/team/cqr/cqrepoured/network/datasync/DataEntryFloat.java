package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;

public class DataEntryFloat extends DataEntry<Float> {

	private float value;

	public DataEntryFloat(String name, float defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public NBTBase write() {
		return new NBTTagFloat(this.value);
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagFloat) {
			this.value = ((NBTTagFloat) nbt).getFloat();
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeFloat(this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = buf.readFloat();
	}

	/**
	 * @deprecated Use {@link DataEntryFloat#set(float)} instead.
	 */
	@Deprecated
	@Override
	public void set(@Nonnull Float value) {
		if (value == null) {
			return;
		}
		this.set(value.floatValue());
	}

	/**
	 * @deprecated Use {@link DataEntryFloat#setInternal(float)} instead.
	 */
	@Deprecated
	@Override
	protected void setInternal(@Nonnull Float value) {
		if (value == null) {
			return;
		}
		this.setInternal(value.floatValue());
	}

	/**
	 * @deprecated Use {@link DataEntryFloat#isSavedValueEqualTo(float)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Float value) {
		return this.isSavedValueEqualTo(value.floatValue());
	}

	/**
	 * @deprecated Use {@link DataEntryFloat#getFloat()} instead.
	 */
	@Deprecated
	@Override
	public Float get() {
		return Float.valueOf(this.getFloat());
	}

	public void set(float value) {
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	protected void setInternal(float value) {
		this.value = value;
	}

	public boolean isSavedValueEqualTo(float value) {
		return this.value == value;
	}

	public float getFloat() {
		return this.value;
	}

}
