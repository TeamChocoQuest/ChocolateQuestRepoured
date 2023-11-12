package team.cqr.cqrepoured.entity.bases;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class WalkingEntityBase<T extends WalkingEntityBase<T>> extends FactionEntity<T> {

	public WalkingEntityBase(EntityType<? extends VariantEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	

}
