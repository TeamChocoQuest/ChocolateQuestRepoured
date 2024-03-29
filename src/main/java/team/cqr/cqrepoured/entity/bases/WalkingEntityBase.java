package team.cqr.cqrepoured.entity.bases;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.common.entity.VariantEntity;
import team.cqr.cqrepoured.faction.entity.FactionEntity;

public class WalkingEntityBase<T extends WalkingEntityBase<T>> extends FactionEntity<T> {

	public WalkingEntityBase(EntityType<? extends VariantEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	

}
