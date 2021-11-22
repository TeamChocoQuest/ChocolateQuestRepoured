package team.cqr.cqrepoured.objects.entity;

public interface IDontRenderFire {

	default boolean canRenderOnFire() {
		return false;
	}

}
