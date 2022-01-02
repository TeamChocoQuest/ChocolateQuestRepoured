package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.DoubleNBT;

public class DataEntryDouble extends DataEntry<Double> {

	private double value;

	public DataEntryDouble(String name, double defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public INBT write() {
		return new DoubleNBT(this.value);
	}

	@Override
	protected void readInternal(INBT nbt) {
		if (nbt instanceof DoubleNBT) {
			this.value = ((DoubleNBT) nbt).getDouble();
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
		this.set(value);
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
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryDouble#isSavedValueEqualTo(double)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Double value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryDouble#getDouble()} instead.
	 */
	@Deprecated
	@Override
	public Double get() {
		return this.getDouble();
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
