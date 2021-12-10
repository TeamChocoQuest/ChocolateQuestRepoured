package team.cqr.cqrepoured.entity;

public interface IDontRenderFire {

	default boolean canRenderOnFire() {
		return false;
	}

}
