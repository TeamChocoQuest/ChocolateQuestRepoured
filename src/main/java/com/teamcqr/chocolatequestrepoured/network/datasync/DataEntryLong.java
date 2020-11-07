package com.teamcqr.chocolatequestrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;

public class DataEntryLong extends DataEntry<Long> {

	private long value;

	public DataEntryLong(String name, long defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public NBTBase write() {
		return new NBTTagLong(this.value);
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagLong) {
			this.value = ((NBTTagLong) nbt).getLong();
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeLong(this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
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
		this.set(value.longValue());
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
		this.setInternal(value.longValue());
	}

	/**
	 * @deprecated Use {@link DataEntryLong#isSavedValueEqualTo(long)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Long value) {
		return this.isSavedValueEqualTo(value.longValue());
	}

	/**
	 * @deprecated Use {@link DataEntryLong#getLong()} instead.
	 */
	@Deprecated
	@Override
	public Long get() {
		return Long.valueOf(this.getLong());
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
