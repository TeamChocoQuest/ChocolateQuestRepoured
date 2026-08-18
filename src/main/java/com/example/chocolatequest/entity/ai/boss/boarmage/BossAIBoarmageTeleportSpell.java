package com.example.chocolatequest.entity.ai.boss.boarmage;

import com.example.chocolatequest.entity.boss.EntityCQRBoarmage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BossAIBoarmageTeleportSpell extends Goal {
    private final EntityCQRBoarmage entity;
    private int cooldown = 70;
    private int timer;

    public BossAIBoarmageTeleportSpell(EntityCQRBoarmage entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) return false;
        double distance = this.entity.distanceToSqr(target);
        return distance <= 9.0D || distance >= 196.0D;
    }

    @Override
    public void start() {
        this.timer = 0;
        this.entity.getNavigation().stop();
        this.entity.startCastingSpell(24);
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer < 24 && this.entity.getTarget() != null;
    }

    @Override
    public void tick() {
        this.timer++;
        LivingEntity target = this.entity.getTarget();
        if (target == null) return;
        this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (this.entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.entity.getX(), this.entity.getY() + 1.0D,
                    this.entity.getZ(), 4, 0.55D, 0.8D, 0.55D, 0.03D);
        }
        if (this.timer == 16) this.teleport(target);
    }

    private void teleport(LivingEntity target) {
        double oldX = this.entity.getX();
        double oldY = this.entity.getY() + 1.0D;
        double oldZ = this.entity.getZ();
        Vec3 away = this.entity.position().subtract(target.position()).normalize();
        Vec3 destination = target.position().add(away.scale(7.0D));
        BlockPos pos = BlockPos.containing(destination);
        boolean moved = this.entity.level().getBlockState(pos).isAir()
                && this.entity.randomTeleport(destination.x, destination.y, destination.z, true);
        for (int attempt = 0; !moved && attempt < 8; attempt++) {
            double x = target.getX() + (this.entity.getRandom().nextDouble() - 0.5D) * 16.0D;
            double y = target.getY() + this.entity.getRandom().nextInt(7) - 3;
            double z = target.getZ() + (this.entity.getRandom().nextDouble() - 0.5D) * 16.0D;
            moved = this.entity.randomTeleport(x, y, z, true);
        }
        if (moved) {
            this.entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 0.75F);
            if (this.entity.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL, oldX, oldY, oldZ,
                        28, 0.6D, 0.8D, 0.6D, 0.08D);
            }
        }
    }

    @Override
    public void stop() {
        this.cooldown = 110;
    }
}
