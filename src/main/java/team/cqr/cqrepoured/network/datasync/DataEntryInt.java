package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DataEntryInt extends DataEntry<Integer> {

	private int value;

	public DataEntryInt(String name, int defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		this.value = defaultValue;
	}

	@Override
	public Tag write() {
		return IntTag.valueOf(this.value);
	}

	@Override
	protected void readInternal(Tag nbt) {
		if (nbt instanceof IntTag) {
			this.value = ((IntTag) nbt).getAsInt();
		}
	}

	@Override
	public void writeChanges(FriendlyByteBuf buf) {
		buf.writeInt(this.value);
	}

	@Override
	protected void readChangesInternal(FriendlyByteBuf buf) {
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
		this.set(value);
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
		this.setInternal(value);
	}

	/**
	 * @deprecated Use {@link DataEntryInt#isSavedValueEqualTo(int)} instead.
	 */
	@Deprecated
	@Override
	public boolean isSavedValueEqualTo(Integer value) {
		return this.isSavedValueEqualTo(value);
	}

	/**
	 * @deprecated Use {@link DataEntryInt#getInt()} instead.
	 */
	@Deprecated
	@Override
	public Integer get() {
		return this.getInt();
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
