package team.cqr.cqrepoured.serialization;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.util.TriConsumer;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;

public record ExtendedMapCodec<K, V, A, M extends Map<K, V>>(Codec<K> keyCodec, Codec<V> elementCodec, Supplier<A> supplier, TriConsumer<A, K, V> accumulator, Function<A, M> finisher) implements Codec<M> {

	@Override
	public <T> DataResult<Pair<M, T>> decode(final DynamicOps<T> ops, final T input) {
		return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
	}

	@Override
	public <T> DataResult<T> encode(final M input, final DynamicOps<T> ops, final T prefix) {
		return encode(input, ops, ops.mapBuilder()).build(prefix);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final ExtendedMapCodec<?, ?, ?, ?> that = (ExtendedMapCodec<?, ?, ?, ?>) o;
		return Objects.equals(keyCodec, that.keyCodec) && Objects.equals(elementCodec, that.elementCodec) && Objects.equals(supplier, that.supplier) && Objects.equals(accumulator, that.accumulator) && Objects.equals(finisher, that.finisher);
	}

	@Override
	public int hashCode() {
		return Objects.hash(keyCodec, elementCodec, supplier, accumulator, finisher);
	}

	@Override
	public String toString() {
		return "ExtendedMapCodec[" + keyCodec + " -> " + elementCodec + ", " + supplier + ", " + accumulator + ", " + finisher + ']';
	}

	private <T> DataResult<M> decode(final DynamicOps<T> ops, final MapLike<T> input) {
		final A read = supplier.get();
		final ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.builder();

		final DataResult<Unit> result = input.entries().reduce(
			DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
			(r, pair) -> {
				final DataResult<K> k = keyCodec().parse(ops, pair.getFirst());
				final DataResult<V> v = elementCodec().parse(ops, pair.getSecond());

				final DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
				entry.error().ifPresent(e -> failed.add(pair));

				return r.apply2stable((u, p) -> {
					accumulator.accept(read, p.getFirst(), p.getSecond());
					return u;
				}, entry);
			},
			(r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
		);

		final M elements = finisher.apply(read);
		final T errors = ops.createMap(failed.build().stream());

		return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
	}

	private <T> RecordBuilder<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
		for (final Map.Entry<K, V> entry : input.entrySet()) {
			prefix.add(keyCodec().encodeStart(ops, entry.getKey()), elementCodec().encodeStart(ops, entry.getValue()));
		}
		return prefix;
	}

}
