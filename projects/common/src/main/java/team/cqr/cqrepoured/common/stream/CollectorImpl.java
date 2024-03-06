package team.cqr.cqrepoured.common.stream;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CollectorImpl<T, A, R> implements Collector<T, A, R> {

	public static final Set<Characteristics> CH_CONCURRENT_ID = Collections
			.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH));
	public static final Set<Characteristics> CH_CONCURRENT_NOID = Collections
			.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED));
	public static final Set<Characteristics> CH_ID = Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
	public static final Set<Characteristics> CH_UNORDERED_ID = Collections
			.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH));
	public static final Set<Characteristics> CH_NOID = Collections.emptySet();
	public static final Set<Characteristics> CH_UNORDERED_NOID = Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));

	private final Supplier<A> supplier;
	private final BiConsumer<A, T> accumulator;
	private final BinaryOperator<A> combiner;
	private final Function<A, R> finisher;
	private final Set<Characteristics> characteristics;

	public CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher,
			Set<Characteristics> characteristics) {
		this.supplier = supplier;
		this.accumulator = accumulator;
		this.combiner = combiner;
		this.finisher = finisher;
		this.characteristics = characteristics;
	}

	@SuppressWarnings("unchecked")
	public CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Set<Characteristics> characteristics) {
		this(supplier, accumulator, combiner, a -> (R) a, characteristics);
	}

	@Override
	public BiConsumer<A, T> accumulator() {
		return accumulator;
	}

	@Override
	public Supplier<A> supplier() {
		return supplier;
	}

	@Override
	public BinaryOperator<A> combiner() {
		return combiner;
	}

	@Override
	public Function<A, R> finisher() {
		return finisher;
	}

	@Override
	public Set<Characteristics> characteristics() {
		return characteristics;
	}

}
