package team.cqr.cqrepoured.client.occlusion;

@FunctionalInterface
interface ObjIntIntInt2ObjFunction<T, R> {

	R apply(T t, int x, int y, int z);

}
