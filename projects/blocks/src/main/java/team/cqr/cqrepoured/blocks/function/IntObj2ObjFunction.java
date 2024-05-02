package team.cqr.cqrepoured.common.function;

@FunctionalInterface
public interface IntObj2ObjFunction<T, R> {

	R apply(int x, T t);

}
