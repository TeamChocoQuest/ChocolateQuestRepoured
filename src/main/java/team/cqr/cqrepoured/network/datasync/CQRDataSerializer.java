package team.cqr.cqrepoured.network.datasync;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public interface CQRDataSerializer<T> extends DataSerializer<T> {

	@Override
	default DataParameter<T> createKey(int id) {
		return new DataParameter<>(id, this);
	}

	@Override
	default T copyValue(T value) {
		return value;
	}

}
