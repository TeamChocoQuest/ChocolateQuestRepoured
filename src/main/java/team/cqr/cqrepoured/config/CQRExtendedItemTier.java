package team.cqr.cqrepoured.config;

import net.minecraft.item.IItemTier;
import team.cqr.cqrepoured.item.IExtendedItemTier;

public class CQRExtendedItemTier extends CQRItemTier implements IExtendedItemTier
{
    public int fixedAttackDamageBonus;
    public float attackSpeedBonus;
    public double movementSpeedBonus;

    public CQRExtendedItemTier(IItemTier tier, int fixedAttackDamageBonus, float attackSpeedBonus, double movementSpeedBonus)
    {
        super(tier.getUses(), tier.getSpeed(), tier.getAttackDamageBonus(), tier.getLevel(), tier.getEnchantmentValue(), tier::getRepairIngredient);

        this.fixedAttackDamageBonus = fixedAttackDamageBonus;
        this.attackSpeedBonus = attackSpeedBonus;
        this.movementSpeedBonus = movementSpeedBonus;
    }

    @Override
    public int getFixedAttackDamageBonus() {
        return this.fixedAttackDamageBonus;
    }

    @Override
    public float getAttackSpeedBonus() {
        return this.attackSpeedBonus;
    }

    @Override
    public double getMovementSpeedBonus() {
        return this.movementSpeedBonus;
    }
}