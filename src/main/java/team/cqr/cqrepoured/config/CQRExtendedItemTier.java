package team.cqr.cqrepoured.config;

import net.minecraft.item.crafting.Ingredient;
import team.cqr.cqrepoured.item.IExtendedItemTier;

import java.util.function.Supplier;

public class CQRExtendedItemTier extends CQRItemTier implements IExtendedItemTier
{
    public int fixedAttackDamageBonus;
    public float attackSpeedBonus;
    public double movementSpeedBonus;

    public CQRExtendedItemTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, int fixedAttackDamageBonus, float attackSpeedBonus, double movementSpeedBonus)
    {
        super(uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient);

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