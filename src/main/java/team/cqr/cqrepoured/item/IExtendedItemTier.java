package team.cqr.cqrepoured.item;

import net.minecraft.item.IItemTier;

public interface IExtendedItemTier extends IItemTier {

	public float getFixedAttackDamageBonus();

	public float getAttackSpeedBonus();

	public double getMovementSpeedBonus();

}
