package team.cqr.cqrepoured.entity.mount;

import net.minecraft.world.World;
import net.minecraft.world.entity.EntityType;
import team.cqr.cqrepoured.entity.bases.EntityCQRGiantSilverfishBase;

public class EntityGiantSilverfishNormal extends EntityCQRGiantSilverfishBase {

	public EntityGiantSilverfishNormal(EntityType<? extends EntityGiantSilverfishNormal> type, World worldIn) {
		super(type, worldIn);
	}

	/*@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GIANT_SILVERFISH;
	}*/

}
