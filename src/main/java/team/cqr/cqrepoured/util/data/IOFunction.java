package team.cqr.cqrepoured.util.data;

import java.io.IOException;

public interface IOFunction<T, R> {

	R apply(T t) throws IOException;

	default <V> IOFunction<V, R> compose(IOFunction<V, T> before) {
		return v -> this.apply(before.apply(v));
	}

	default <V> IOFunction<T, V> andThen(IOFunction<R, V> before) {
		return t -> before.apply(this.apply(t));
	}

	static <T> IOFunction<T, T> identity() {
		return t -> t;
	}

}
