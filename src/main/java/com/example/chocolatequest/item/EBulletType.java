package com.example.chocolatequest.item;

public enum EBulletType {
    IRON(2.5F, false),
    GOLD(2.0F, true),
    DIAMOND(4.0F, false),
    FIRE(2.5F, true);

    private final float additionalDamage;
    private final boolean fireDamage;

    EBulletType(float additionalDamage, boolean fireDamage) {
        this.additionalDamage = additionalDamage;
        this.fireDamage = fireDamage;
    }

    public float getAdditionalDamage() {
        return additionalDamage;
    }

    public boolean fireDamage() {
        return fireDamage;
    }
}
