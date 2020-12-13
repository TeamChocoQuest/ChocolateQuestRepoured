package team.cqr.cqrepoured.network.datasync;

import javax.annotation.Nonnull;

public abstract class DataEntryObject<T> extends DataEntry<T> {

	protected T value;

	public DataEntryObject(String name, @Nonnull T defaultValue, boolean isClientModificationAllowed) {
		super(name, isClientModificationAllowed);
		if (defaultValue == null) {
			throw new NullPointerException();
		}
		this.value = defaultValue;
	}

	@Override
	protected void setInternal(@Nonnull T value) {
		if (value == null) {
			return;
		}
		this.value = value;
	}

	@Override
	public boolean isSavedValueEqualTo(@Nonnull T value) {
		return this.value.equals(value);
	}

	@Override
	public T get() {
		return this.value;
	}

}
