package com.example.chocolatequest.entity.ai.boss.boarmage;

import com.example.chocolatequest.entity.ai.target.TargetUtil;
import com.example.chocolatequest.entity.boss.EntityCQRBoarmage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/** A visible rune pattern that replaces the old stack of up to six instant explosions. */
public class BossAIBoarmageExplodeAreaAttack extends Goal {
    private final EntityCQRBoarmage entity;
    private final List<Vec3> runes = new ArrayList<>();
    private int cooldown = 55;
    private int timer;

    public BossAIBoarmageExplodeAreaAttack(EntityCQRBoarmage entity) {
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
        return target != null && target.isAlive() && this.entity.distanceToSqr(target) < 400.0D;
    }

    @Override
    public void start() {
        this.timer = 0;
        this.runes.clear();
        LivingEntity target = this.entity.getTarget();
        if (target != null) {
            Vec3 center = target.position();
            this.runes.add(center);
            this.runes.add(center.add(3.0D, 0.0D, 0.0D));
            this.runes.add(center.add(-3.0D, 0.0D, 0.0D));
            this.runes.add(center.add(0.0D, 0.0D, 3.0D));
            this.runes.add(center.add(0.0D, 0.0D, -3.0D));
        }
        this.entity.getNavigation().stop();
        this.entity.startCastingSpell(42);
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer < 46 && !this.runes.isEmpty() && this.entity.getTarget() != null;
    }

    @Override
    public void tick() {
        this.timer++;
        LivingEntity target = this.entity.getTarget();
        if (target != null) this.entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (this.entity.level() instanceof ServerLevel serverLevel) {
            for (Vec3 rune : this.runes) {
                serverLevel.sendParticles(this.timer < 30 ? ParticleTypes.SMALL_FLAME : ParticleTypes.SOUL_FIRE_FLAME,
                        rune.x, rune.y + 0.15D, rune.z, this.timer < 30 ? 2 : 5,
                        0.65D, 0.05D, 0.65D, 0.015D);
            }
        }
        if (this.timer == 34) this.detonateRunes();
    }

    private void detonateRunes() {
        if (this.entity.level() instanceof ServerLevel serverLevel) {
            for (Vec3 rune : this.runes) {
                serverLevel.sendParticles(ParticleTypes.FLAME, rune.x, rune.y + 0.8D, rune.z,
                        24, 0.8D, 0.8D, 0.8D, 0.06D);
            }
        }
        this.entity.playSound(SoundEvents.BLAZE_SHOOT, 1.3F, 0.65F);
        for (LivingEntity victim : this.entity.level().getEntitiesOfClass(LivingEntity.class,
                this.entity.getBoundingBox().inflate(24.0D), living -> living.isAlive()
                        && living != this.entity && !TargetUtil.isAllyCheckingLeaders(this.entity, living))) {
            boolean onRune = this.runes.stream().anyMatch(rune -> {
                double dx = victim.getX() - rune.x;
                double dz = victim.getZ() - rune.z;
                return dx * dx + dz * dz <= 2.25D && Math.abs(victim.getY() - rune.y) <= 3.0D;
            });
            if (onRune) {
                victim.hurt(this.entity.damageSources().magic(), 8.0F);
                victim.igniteForSeconds(3.0F);
            }
        }
    }

    @Override
    public void stop() {
        this.runes.clear();
        this.timer = 0;
        this.cooldown = 115 + this.entity.getRandom().nextInt(35);
    }
}
