package team.cqr.cqrepoured.config;

import net.minecraft.item.crafting.Ingredient;

public class ItemTierConfig extends CQRItemTier
{
    //Skip repair ingredient for config
    public ItemTierConfig(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue)
    {
        super(uses, speed, attackDamageBonus, level, enchantmentValue, () -> Ingredient.EMPTY);
    }
}