package team.cqr.cqrepoured.objects.entity;

public interface IDontRenderFire {

	public default boolean canRenderOnFire() {
		return false;
	}
	
}
