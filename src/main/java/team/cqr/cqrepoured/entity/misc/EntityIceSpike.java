package team.cqr.cqrepoured.entity.misc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.world.level.Level;
import team.cqr.cqrepoured.entity.IDontRenderFire;
import team.cqr.cqrepoured.init.CQREntityTypes;

public class EntityIceSpike extends EvokerFangsEntity implements IDontRenderFire {

	public EntityIceSpike(EntityType<? extends EntityIceSpike> type, Level p_i47275_1_) {
		super(CQREntityTypes.ICE_SPIKE.get(), p_i47275_1_);
	}

	public EntityIceSpike(Level p_i47276_1_, double p_i47276_2_, double p_i47276_4_, double p_i47276_6_, float p_i47276_8_, int p_i47276_9_, LivingEntity p_i47276_10_) {
		super(p_i47276_1_, p_i47276_2_, p_i47276_4_, p_i47276_6_, p_i47276_8_, p_i47276_9_, p_i47276_10_);
	}

}
