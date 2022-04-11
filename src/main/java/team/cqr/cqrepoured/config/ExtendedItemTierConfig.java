package team.cqr.cqrepoured.config;

import net.minecraft.item.crafting.Ingredient;

public class ExtendedItemTierConfig extends CQRExtendedItemTier
{
    //Skip tier
    public ExtendedItemTierConfig(int fixedAttackDamageBonus, float attackSpeedBonus, double movementSpeedBonus) {
        super(fixedAttackDamageBonus, attackSpeedBonus, movementSpeedBonus);
    }
}