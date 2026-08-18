package com.example.chocolatequest.entity.ai.boss.exterminator;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import com.example.chocolatequest.entity.boss.exterminator.EntityCQRExterminator;
import com.example.chocolatequest.entity.projectile.ProjectileCannonBall;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleOptions;

import java.util.EnumSet;

public class BossAINewExterminatorCannon extends Goal {

    private final EntityCQRExterminator mob;
    
    private int cooldown = 0;
    private int attackSequenceTicks = 0;
    private boolean isSalvo = false;
    private int shotsRemaining = 0;
    
    private static final int MAX_COOLDOWN = 100;
    
    public BossAINewExterminatorCannon(EntityCQRExterminator mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive() || this.mob.isStunned() || this.mob.globalAttackCooldown > 0) {
            return false;
        }
        
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        
        double distSq = this.mob.distanceToSqr(this.mob.getTarget());
        return distSq > 16.0D && distSq < 1024.0D;
    }
    
    @Override
    public void start() {
        this.attackSequenceTicks = 0;
		this.isSalvo = this.mob.getRandom().nextFloat() < 0.72F;
		this.shotsRemaining = this.isSalvo ? 3 + this.mob.getRandom().nextInt(2) : 1;
        this.mob.switchCannonArmState(true); 
		this.mob.playSound(net.minecraft.sounds.SoundEvents.PISTON_EXTEND, 1.3F, 0.65F);
    }

    @Override
    public boolean canContinueToUse() {
        return this.shotsRemaining > 0 && this.mob.getTarget() != null && this.mob.getTarget().isAlive() && !this.mob.isStunned();
    }
    
    @Override
    public void stop() {
        this.shotsRemaining = 0;
        this.cooldown = MAX_COOLDOWN;
        this.mob.globalAttackCooldown = 40; // 2 seconds before next attack can start
        if (!this.mob.switchCannonArmState(false)) {
            this.mob.setCannonArmAutoTimeoutForLowering(1); // will retry on tick
        }
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;
        
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        
        // Force the body to align with the target perfectly to prevent shooting backwards
        double d0 = target.getX() - this.mob.getX();
        double d1 = target.getZ() - this.mob.getZ();
        float f = (float)(net.minecraft.util.Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
        this.mob.setYRot(f);
        this.mob.yBodyRot = f;
        this.mob.yHeadRot = f;
        this.mob.yRotO = f;
        this.mob.yBodyRotO = f;
        this.mob.yHeadRotO = f;
        
        if (this.isSalvo) {
            this.mob.getMoveControl().strafe(-0.5F, 0.0F); // properly strafe backwards
        } else {
            this.mob.getNavigation().stop();
        }
        
		// Never fire through the 40-tick arm-raising animation.
		if (!this.mob.isCannonArmReadyToShoot()) {
			this.mob.getNavigation().stop();
			return;
		}
		this.attackSequenceTicks++;
		if (this.attackSequenceTicks == 1) {
			this.mob.playSound(net.minecraft.sounds.SoundEvents.RESPAWN_ANCHOR_CHARGE, 1.2F, 0.72F);
		}
		if (this.attackSequenceTicks < 8) return;
        
		int fireRate = this.isSalvo ? 10 : 20;
        
        if (this.attackSequenceTicks % fireRate == 0 && this.shotsRemaining > 0) {
            this.fireCannon(target);
            this.shotsRemaining--;
        }
    }
    
    private void fireCannon(LivingEntity target) {
        this.mob.startShootingAnimation(this.isSalvo);
        
        Vec3 armPos = this.mob.getCannonFiringLocation();
        this.spawnParticles(armPos);
        
        ProjectileCannonBall cannonBall = new ProjectileCannonBall(this.mob, this.mob.level(), this.isSalvo);
        cannonBall.setPos(armPos.x, armPos.y, armPos.z);
        
        double vx = target.getX() - armPos.x;
        double vy = target.getY() + target.getBbHeight() * 0.5D - armPos.y;
        double vz = target.getZ() - armPos.z;
        
        float inaccuracy = this.isSalvo ? 2.0F : 1.0F;
        
        cannonBall.shoot(vx, vy, vz, 1.2F, inaccuracy);
        this.mob.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(), 3.0F, 0.75F + this.mob.getRandom().nextFloat() * 0.5F);
        
        this.mob.level().addFreshEntity(cannonBall);
    }
    
    private void spawnParticles(Vec3 armPos) {
        if (this.mob.level() instanceof ServerLevel sw) {
            ParticleOptions[] particles = new ParticleOptions[] { ParticleTypes.LARGE_SMOKE, ParticleTypes.CLOUD, ParticleTypes.FLAME };
            for(int i = 0; i < particles.length; i++) {
                int count = i > 0 ? 5 : 10;
                sw.sendParticles(particles[i], armPos.x, armPos.y, armPos.z, count, 0.1D, 0.1D, 0.1D, 0.05D);
            }
        }
    }
}
