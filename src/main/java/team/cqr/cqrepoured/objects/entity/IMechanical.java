package team.cqr.cqrepoured.objects.entity;

import net.minecraft.potion.PotionEffect;

public interface IMechanical {
	
	public default boolean isImmuneToFire() {
		return true;
	}

	//We're an machine, we don't live
	public default boolean isPotionApplicable(PotionEffect potioneffectIn) {
		return false;
	}
	
}
