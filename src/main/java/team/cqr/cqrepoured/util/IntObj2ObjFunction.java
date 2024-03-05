package team.cqr.cqrepoured.util;

@FunctionalInterface
public interface IntObj2ObjFunction<T, R> {

	R apply(int x, T t);

}
