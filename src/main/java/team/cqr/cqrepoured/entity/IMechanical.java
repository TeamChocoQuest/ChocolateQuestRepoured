package team.cqr.cqrepoured.entity;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public interface IMechanical {

	default boolean isImmuneToFire() {
		return true;
	}

	// We're an machine, we don't live
	default boolean isPotionApplicable(MobEffect potioneffectIn) {
		return false;
	}

	default boolean canReceiveElectricDamageCurrently() {
		if (this instanceof LivingEntity) {
			return ((LivingEntity) this).isInWater() /*|| ((LivingEntity) this).isWet()*/;
		}
		return false;
	}

}
