package com.example.chocolatequest.entity.ai.goal;

import com.example.chocolatequest.entity.ai.target.TargetUtil;
import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import com.example.chocolatequest.item.staff.HealStaffItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class HealAlliesGoalCQR extends Goal {
    protected final AbstractEntityCQR entity;
    protected LivingEntity targetAlly;
    protected int attackTime = -1;
    private final double speedModifier = 1.25D;

    public HealAlliesGoalCQR(AbstractEntityCQR entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.entity.getMainHandItem().isEmpty() || !(this.entity.getMainHandItem().getItem() instanceof HealStaffItem)) {
            return false;
        }
        
        AABB aabb = this.entity.getBoundingBox().inflate(16.0D, 8.0D, 16.0D);
        List<LivingEntity> allies = this.entity.level().getEntitiesOfClass(LivingEntity.class, aabb, 
            e -> e != this.entity && e.isAlive() && e.getHealth() < e.getMaxHealth() && TargetUtil.isAllyCheckingLeaders(this.entity, e));

        if (allies.isEmpty()) {
            return false;
        }

        this.targetAlly = TargetUtil.getNearestEntity(this.entity, allies);
        return this.targetAlly != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetAlly != null && this.targetAlly.isAlive() && this.targetAlly.getHealth() < this.targetAlly.getMaxHealth() 
            && !this.entity.getMainHandItem().isEmpty() && this.entity.getMainHandItem().getItem() instanceof HealStaffItem 
            && this.entity.distanceToSqr(this.targetAlly) < 256.0D;
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.targetAlly, this.speedModifier);
    }

    @Override
    public void stop() {
        this.targetAlly = null;
        this.entity.getNavigation().stop();
        this.entity.setAggressive(false);
    }

    public void tick() {
        this.entity.getLookControl().setLookAt(this.targetAlly, 30.0F, 30.0F);
        
        double distSq = this.entity.distanceToSqr(this.targetAlly.getX(), this.targetAlly.getY(), this.targetAlly.getZ());
        
        if (this.attackTime > 0) {
            --this.attackTime;
        }

        if (distSq <= 4.0D) {
            this.entity.getNavigation().stop();
            if (this.attackTime <= 0) {
                this.entity.swing(InteractionHand.MAIN_HAND);
                this.entity.getMainHandItem().getItem().hurtEnemy(this.entity.getMainHandItem(), this.targetAlly, this.entity);
                this.attackTime = 40;
            }
        } else {
            this.entity.getNavigation().moveTo(this.targetAlly, this.speedModifier);
        }
    }
}
