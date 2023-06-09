package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryDouble extends DataEntry<Double> {

	private double value;

	public DataEntryDouble(String name, double defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public Tag write() {
		return DoubleTag.valueOf(this.value);
	}

	@Override
	protected void readInternal(Tag nbt) {
		if (nbt instanceof DoubleTag) {
			this.value = ((DoubleTag) nbt).getAsDouble();
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeDouble(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
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
