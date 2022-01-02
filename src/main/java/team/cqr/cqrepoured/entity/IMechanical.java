package team.cqr.cqrepoured.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

public interface IMechanical {

	default boolean isImmuneToFire() {
		return true;
	}

	// We're an machine, we don't live
	default boolean isPotionApplicable(Effect potioneffectIn) {
		return false;
	}

	default boolean canReceiveElectricDamageCurrently() {
		if (this instanceof LivingEntity) {
			return ((LivingEntity) this).isInWater() /*|| ((LivingEntity) this).isWet()*/;
		}
		return false;
	}

}
