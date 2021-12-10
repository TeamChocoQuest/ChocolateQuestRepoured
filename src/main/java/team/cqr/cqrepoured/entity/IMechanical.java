package team.cqr.cqrepoured.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public interface IMechanical {

	default boolean isImmuneToFire() {
		return true;
	}

	// We're an machine, we don't live
	default boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}

	default boolean canReceiveElectricDamageCurrently() {
		if (this instanceof EntityLivingBase) {
			return ((EntityLivingBase) this).isInWater() || ((EntityLivingBase) this).isWet();
		}
		return false;
	}

}
