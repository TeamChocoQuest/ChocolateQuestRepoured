package com.teamcqr.chocolatequestrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

public class DataEntryInt extends DataEntry<Integer> {

	private int value;

	public DataEntryInt(String name, int defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public NBTBase write() {
		return new NBTTagInt(this.value);
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagInt) {
			this.value = ((NBTTagInt) nbt).getInt();
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeInt(this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
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
		this.set(value.intValue());
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
		this.setInternal(value.intValue());
	}

	/**
	 * @deprecated Use {@link DataEntryInt#isSavedValueEqualTo(int)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Integer value) {
		return this.isSavedValueEqualTo(value.intValue());
	}

	/**
	 * @deprecated Use {@link DataEntryInt#getInt()} instead.
	 */
	@Deprecated
	@Override
	public Integer get() {
		return Integer.valueOf(this.getInt());
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
