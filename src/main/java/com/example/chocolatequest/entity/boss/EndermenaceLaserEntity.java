package com.example.chocolatequest.entity.boss;

import com.example.chocolatequest.entity.projectile.EntityTargetingLaser;
import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EndermenaceLaserEntity extends EntityTargetingLaser {
    public EndermenaceLaserEntity(EntityType<? extends EndermenaceLaserEntity> type, Level level) {
        super(type, level);
    }

    public EndermenaceLaserEntity(LivingEntity caster, LivingEntity target) {
        super(ModEntities.ENDERMENACE_LASER.get(), caster.level(), caster, 56.0F, target);
        this.maxRotationPerTick = 0.55F;
    }

    @Override
    public float getColorR() {
        return 0.55F;
    }

    @Override
    public float getColorG() {
        return 0.05F;
    }

    @Override
    public float getColorB() {
        return 0.95F;
    }

    @Override
    public float getDamage() {
        return 3.0F;
    }

    @Override
    public double laserEffectRadius() {
        return 0.5D;
    }

    @Override
    public Vec3 getOffsetVector() {
        LivingEntity caster = this.caster;
        if (caster == null) return Vec3.ZERO;
        // The inherited 60% height offset was above this unusually tall model.
        // Emit from the front of its upper torso instead.
        return new Vec3(0.0D, caster.getBbHeight() * 0.43D, 0.0D)
                .add(caster.getLookAngle().scale(1.15D));
    }

    @Override
    public boolean canHitBlock(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        return false;
    }
}
