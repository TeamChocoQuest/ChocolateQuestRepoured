package team.cqr.cqrepoured.entity.mount;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.bases.EntityCQRGiantSilverfishBase;

public class EntityGiantSilverfishRed extends EntityCQRGiantSilverfishBase {

	public EntityGiantSilverfishRed(EntityType<? extends EntityGiantSilverfishRed> type, Level worldIn) {
		super(type, worldIn);
	}

	/*@Override
	protected ResourceLocation getLootTable() {
		return CQRLoottables.ENTITIES_GIANT_SILVERFISH_RED;
	}*/
	
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if(pSource.isFire()) {
			return false;
		}
		return super.hurt(pSource, pAmount);
	}

}
