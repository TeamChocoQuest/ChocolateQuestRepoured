package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryFloat extends DataEntry<Float> {

	private float value;

	public DataEntryFloat(String name, float defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public Tag write() {
		return FloatTag.valueOf(this.value);
	}

	@Override
	protected void readInternal(Tag nbt) {
		if (nbt instanceof FloatTag) {
			this.value = ((FloatTag) nbt).getAsFloat();
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeFloat(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
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
		this.set(value);
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
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryFloat#isSavedValueEqualTo(float)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Float value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryFloat#getFloat()} instead.
	 */
	@Deprecated
	@Override
	public Float get() {
		return this.getFloat();
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
