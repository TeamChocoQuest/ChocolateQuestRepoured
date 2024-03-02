package team.cqr.cqrepoured.common.io;

import java.io.IOException;

public interface IOConsumer<T> {

	void accept(T t) throws IOException;

	default <V> IOConsumer<V> compose(IOFunction<V, T> before) {
		return v -> this.accept(before.apply(v));
	}

}
