package team.cqr.cqrepoured.asm.hook;

import net.minecraft.entity.LivingEntity;
import team.cqr.cqrepoured.entity.IMechanical;

public class EntityPotionHook {

	public static boolean isWaterSensitiveEntity(LivingEntity entity) {
		return entity instanceof IMechanical;
	}

}
