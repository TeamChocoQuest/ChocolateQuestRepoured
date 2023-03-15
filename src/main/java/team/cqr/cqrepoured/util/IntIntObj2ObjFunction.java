package team.cqr.cqrepoured.util;

@FunctionalInterface
public interface IntIntObj2ObjFunction<T, R> {

	R apply(int x, int y, T t);

}
