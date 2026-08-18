package com.example.chocolatequest.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class FactionMob extends Monster {

    protected FactionMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Prevents friendly fire and faction infighting.
     * Any FactionMob is allied to any other FactionMob.
     */
    @Override
    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        }
        if (entity instanceof FactionMob) {
            return true;
        }
        return false;
    }

    /**
     * Extra safety net to absolutely prevent targeting allies even via revenge goals.
     */
    @Override
    public boolean canAttack(net.minecraft.world.entity.LivingEntity target) {
        if (target instanceof FactionMob) {
            return false;
        }
        return super.canAttack(target);
    }
}
