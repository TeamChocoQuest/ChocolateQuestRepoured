package com.example.chocolatequest.entity.ai.goal;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import java.util.EnumSet;

public class CQDrinkPotionGoal extends Goal {
    private final AbstractEntityCQR mob;
    private int drinkingTimer;
    private int navigateTimer;

    public CQDrinkPotionGoal(AbstractEntityCQR mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.canUsePotion()) {
            return false;
        }
        if (this.mob.hasUsedPotion() || this.mob.isDeadOrDying()) {
            return false;
        }
        return this.mob.getHealth() < this.mob.getMaxHealth() * 0.25F;
    }

    @Override
    public void start() {
        this.drinkingTimer = 60; // 3 seconds total
        this.navigateTimer = 0;
        this.mob.setDrinkingPotion(true);
        // Do NOT stop navigation here, we want them to flee!
    }

    @Override
    public void tick() {
        this.drinkingTimer--;

        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

            // Continuously search for and move to flee positions
            if (this.navigateTimer-- <= 0 || this.mob.getNavigation().isDone()) {
                this.navigateTimer = 10;
                // Try to find a position away from target, increased radius
                Vec3 fleePos = DefaultRandomPos.getPosAway(this.mob, 16, 7, target.position());
                if (fleePos != null) {
                    this.mob.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, 1.25D);
                }
            }
        }

        if (this.drinkingTimer % 4 == 0) {
            this.mob.swing(this.mob.isLeftHanded() ? net.minecraft.world.InteractionHand.OFF_HAND : net.minecraft.world.InteractionHand.MAIN_HAND);
        }

        if (this.drinkingTimer % 10 == 0) {
            this.mob.playSound(net.minecraft.sounds.SoundEvents.GENERIC_DRINK, 1.0F, this.mob.getRandom().nextFloat() * 0.1F + 0.9F);
        }

        if (this.drinkingTimer <= 0) {
            // Restore health to 50%
            float targetHealth = this.mob.getMaxHealth() * 0.50F;
            if (this.mob.getHealth() < targetHealth) {
                this.mob.setHealth(targetHealth);
            }
            this.mob.setHasUsedPotion(true);
            this.mob.playSound(net.minecraft.sounds.SoundEvents.PLAYER_BURP, 1.0F, this.mob.getRandom().nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.drinkingTimer > 0 && !this.mob.hasUsedPotion() && this.mob.isAlive();
    }

    @Override
    public void stop() {
        this.mob.setDrinkingPotion(false);
        this.mob.getNavigation().stop();
    }
}
