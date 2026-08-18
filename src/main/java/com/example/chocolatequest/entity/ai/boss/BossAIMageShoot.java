package com.example.chocolatequest.entity.ai.boss;

import com.example.chocolatequest.entity.boss.AbstractEntityCQRMageBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BossAIMageShoot extends Goal {
    private final AbstractEntityCQRMageBase mage;
    private int attackTime = -1;
    private final int attackInterval;
    private final float attackRadius;
    private final float attackRadiusSqr;
    private final boolean canShootWitherSkulls;
    private int windupTicks;

    public BossAIMageShoot(AbstractEntityCQRMageBase mage, int attackInterval, float attackRadius, boolean canShootWitherSkulls) {
        this.mage = mage;
        this.attackInterval = attackInterval;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.canShootWitherSkulls = canShootWitherSkulls;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mage.getTarget();
        if (target != null && target.isAlive()) {
            return this.mage.distanceToSqr(target) <= (double)this.attackRadiusSqr;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.attackTime = this.attackInterval;
    }

    @Override
    public void stop() {
        this.attackTime = -1;
        this.windupTicks = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mage.getTarget();
        if (target != null) {
            if (this.windupTicks > 0) {
                this.mage.getNavigation().stop();
                this.mage.getLookControl().setLookAt(target, 30.0F, 30.0F);
                if (--this.windupTicks == 0) this.shootProjectile(target);
                return;
            }
            double distanceToTarget = this.mage.distanceToSqr(target);
            boolean canSee = this.mage.getSensing().hasLineOfSight(target);

            if (distanceToTarget < 64.0D) {
                // If closer than 8 blocks, run away
                if (this.mage.getNavigation().isDone()) {
                    net.minecraft.world.phys.Vec3 fleePos = net.minecraft.world.entity.ai.util.DefaultRandomPos.getPosAway(this.mage, 16, 7, target.position());
                    if (fleePos != null) {
                        this.mage.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, 1.2D);
                    }
                }
            } else if (distanceToTarget > 144.0D) {
                // If farther than 12 blocks, move closer
                if (this.mage.getNavigation().isDone() || this.mage.getRandom().nextInt(10) == 0) {
                    this.mage.getNavigation().moveTo(target, 1.0D);
                }
            } else {
                // Stop and shoot
                this.mage.getNavigation().stop();
            }

            if (distanceToTarget < (double)this.attackRadiusSqr && canSee) {
                this.mage.getLookControl().setLookAt(target, 30.0F, 30.0F);

                if (--this.attackTime == 0) {
                    this.mage.startCastingSpell(14);
                    this.windupTicks = 10;
                    this.attackTime = this.attackInterval + this.windupTicks;
                } else if (this.attackTime < 0) {
                    this.attackTime = this.attackInterval;
                }
            } else if (this.attackTime > 0) {
                --this.attackTime;
            }
        }
    }

    private void shootProjectile(LivingEntity target) {
        if (!this.mage.level().isClientSide) {
            net.minecraft.world.item.ItemStack mainHand = this.mage.getMainHandItem();
            if (mainHand.getItem() instanceof com.example.chocolatequest.item.IRangedWeapon ranged) {
                // If they hold a staff, 50% chance to shoot the staff, 50% chance to shoot default spells
                if (this.mage.getRandom().nextBoolean()) {
                    ranged.shoot(this.mage.level(), this.mage, target, net.minecraft.world.InteractionHand.MAIN_HAND);
                    return;
                }
            }

            this.mage.playSound(net.minecraft.sounds.SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.0F);
            double d0 = target.getX() - this.mage.getX();
            double d1 = target.getY(0.5D) - this.mage.getY(0.5D);
            double d2 = target.getZ() - this.mage.getZ();

            // Randomly pick Small Fireball or Wither Skull (if allowed)
            if (!this.canShootWitherSkulls || this.mage.getRandom().nextBoolean()) {
                SmallFireball fireball = new SmallFireball(this.mage.level(), this.mage, new Vec3(d0, d1, d2).normalize());
                fireball.setPos(this.mage.getX(), this.mage.getY() + this.mage.getEyeHeight(), this.mage.getZ());
                this.mage.level().addFreshEntity(fireball);
            } else {
                WitherSkull skull = new WitherSkull(this.mage.level(), this.mage, new Vec3(d0, d1, d2).normalize());
                // Non-destructive wither skull
                skull.setDangerous(false); 
                skull.setPos(this.mage.getX(), this.mage.getY() + this.mage.getEyeHeight(), this.mage.getZ());
                this.mage.level().addFreshEntity(skull);
            }
        }
    }
}
