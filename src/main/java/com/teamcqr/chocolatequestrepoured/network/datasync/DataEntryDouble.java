package com.teamcqr.chocolatequestrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;

public class DataEntryDouble extends DataEntry<Double> {

	private double value;

	public DataEntryDouble(String name, double defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public NBTBase write() {
		return new NBTTagDouble(this.value);
	}

	@Override
	protected void readInternal(NBTBase nbt) {
		if (nbt instanceof NBTTagDouble) {
			this.value = ((NBTTagDouble) nbt).getDouble();
		}
	}

	@Override
	public void writeChanges(ByteBuf buf) {
		buf.writeDouble(this.value);
	}

	@Override
	protected void readChangesInternal(ByteBuf buf) {
		this.value = buf.readDouble();
	}

	/**
	 * @deprecated Use {@link DataEntryDouble#set(double)} instead.
	 */
	@Deprecated
	@Override
	public void set(@Nonnull Double value) {
		if (value == null) {
			return;
		}
		this.set(value.doubleValue());
	}

	/**
	 * @deprecated Use {@link DataEntryDouble#setInternal(double)} instead.
	 */
	@Deprecated
	@Override
	protected void setInternal(@Nonnull Double value) {
		if (value == null) {
			return;
		}
		this.setInternal(value.doubleValue());
	}

	/**
	 * @deprecated Use {@link DataEntryDouble#isSavedValueEqualTo(double)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Double value) {
		return this.isSavedValueEqualTo(value.doubleValue());
	}

	/**
	 * @deprecated Use {@link DataEntryDouble#getDouble()} instead.
	 */
	@Deprecated
	@Override
	public Double get() {
		return Double.valueOf(this.getDouble());
	}

	public void set(double value) {
		if (!this.isSavedValueEqualTo(value)) {
			this.setInternal(value);
			this.onValueChanged();
			this.markDirty();
		}
	}

	protected void setInternal(double value) {
		this.value = value;
	}

	public boolean isSavedValueEqualTo(double value) {
		return this.value == value;
	}

	public double getDouble() {
		return this.value;
	}

}
