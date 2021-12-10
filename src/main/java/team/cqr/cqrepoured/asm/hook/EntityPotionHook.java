package team.cqr.cqrepoured.asm.hook;

import net.minecraft.entity.EntityLivingBase;
import team.cqr.cqrepoured.entity.IMechanical;

public class EntityPotionHook {

	public static boolean isWaterSensitiveEntity(EntityLivingBase entity) {
		return entity instanceof IMechanical;
	}

}
