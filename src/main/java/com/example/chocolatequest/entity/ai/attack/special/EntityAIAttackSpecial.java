package com.example.chocolatequest.entity.ai.attack.special;

import net.minecraft.world.entity.LivingEntity;
import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class EntityAIAttackSpecial extends AbstractCQREntityAI<AbstractEntityCQR> {

    public static final List<AbstractEntityAIAttackSpecial> SPECIAL_ATTACKS = new ArrayList<>();
    static {
        SPECIAL_ATTACKS.add(new EntityAIAttackSpecialSpinAttack());
    }
    
    private AbstractEntityAIAttackSpecial activeSpecialAttack;
    private int specialAttackTick;
    private int tick;

    public EntityAIAttackSpecial(AbstractEntityCQR entity) {
        super(entity);
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity attackTarget = this.entity.getTarget();
        if (attackTarget == null) {
            return false;
        }
        if (!this.entity.getSensing().hasLineOfSight(attackTarget)) {
            return false;
        }
        for (AbstractEntityAIAttackSpecial specialAttack : SPECIAL_ATTACKS) {
            if (this.specialAttackTick + specialAttack.getCooldown(this.entity) >= this.entity.tickCount) {
                continue;
            }
            if (!specialAttack.shouldStartAttack(this.entity, attackTarget)) {
                continue;
            }
            if (this.random.nextDouble() > specialAttack.getAttackChance(this.entity, attackTarget)) {
                continue;
            }
            this.activeSpecialAttack = specialAttack;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.tick >= this.activeSpecialAttack.getMaxUseTime()) {
            return false;
        }
        LivingEntity attackTarget = this.entity.getTarget();
        if ((this.activeSpecialAttack.needsTargetToContinue() || this.activeSpecialAttack.needsSightToContinue()) && attackTarget == null) {
            return false;
        }
        if (this.activeSpecialAttack.needsSightToContinue() && !this.entity.getSensing().hasLineOfSight(attackTarget)) {
            return false;
        }
        return this.activeSpecialAttack.shouldContinueAttack(this.entity, attackTarget);
    }

    @Override
    public boolean isInterruptable() {
        return this.activeSpecialAttack.isInterruptible(this.entity);
    }

    @Override
    public void start() {
        this.tick = 0;

        LivingEntity attackTarget = this.entity.getTarget();
        this.activeSpecialAttack.startAttack(this.entity, attackTarget);
    }

    @Override
    public void stop() {
        this.specialAttackTick = this.entity.tickCount;
        this.tick = -1;

        this.activeSpecialAttack.resetAttack(this.entity);
    }

    public void tick() {
        LivingEntity attackTarget = this.entity.getTarget();
        this.activeSpecialAttack.continueAttack(this.entity, attackTarget, this.tick++);

        if (this.tick == this.activeSpecialAttack.getMaxUseTime()) {
            this.activeSpecialAttack.stopAttack(this.entity, attackTarget);
        }
    }

}
