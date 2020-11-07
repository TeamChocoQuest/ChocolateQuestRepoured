package com.teamcqr.chocolatequestrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;

public class DataEntryShort extends DataEntry<Short> {

	private short value;

	public DataEntryShort(String name, short defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public NBTBase write() {
		return new NBTTagShort(this.value);
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagShort) {
			this.value = ((NBTTagShort) nbt).getShort();
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
		this.set(value.shortValue());
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
		this.setInternal(value.shortValue());
	}

	/**
	 * @deprecated Use {@link DataEntryShort#isSavedValueEqualTo(short)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Short value) {
		return this.isSavedValueEqualTo(value.shortValue());
	}

	/**
	 * @deprecated Use {@link DataEntryShort#getShort()} instead.
	 */
	@Deprecated
	@Override
	public Short get() {
		return Short.valueOf(this.getShort());
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
