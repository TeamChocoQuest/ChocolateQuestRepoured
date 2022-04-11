package team.cqr.cqrepoured.config;

public class ExtendedItemTierConfig
{
    public int fixedAttackDamageBonus;
    public float attackSpeedBonus;
    public double movementSpeedBonus;

    //Skip tier
    public ExtendedItemTierConfig(int fixedAttackDamageBonus, float attackSpeedBonus, double movementSpeedBonus)
    {
        this.fixedAttackDamageBonus = fixedAttackDamageBonus;
        this.attackSpeedBonus = attackSpeedBonus;
        this.movementSpeedBonus = movementSpeedBonus;
    }

    public int getFixedAttackDamageBonus() {
        return this.fixedAttackDamageBonus;
    }

    public float getAttackSpeedBonus() {
        return this.attackSpeedBonus;
    }

    public double getMovementSpeedBonus() {
        return this.movementSpeedBonus;
    }
}