package team.cqr.cqrepoured.util.tool;

public class Triple<A, B, C> {

	private final A first;
	private final B second;
	private final C third;

	public Triple(A first, B second, C third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public Triple(Tuple<A, B> tuple, C third) {
		this.first = tuple.getFirst();
		this.second = tuple.getSecond();
		this.third = third;
	}

	public Triple(A first, Tuple<B, C> tuple) {
		this.first = first;
		this.second = tuple.getFirst();
		this.third = tuple.getSecond();
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	public C getThird() {
		return third;
	}

}
