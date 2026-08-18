package com.example.chocolatequest.entity.ai.goal.boss;

import com.example.chocolatequest.entity.boss.CQBullEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class BullChargeGoal extends Goal {
    private final CQBullEntity bull;
    private int chargeTime;
    private int prepareTime;
    private boolean isCharging;
    private final boolean canBreakBlocks;
    private double chargeDirX;
    private double chargeDirZ;
    private int cooldown;

    public BullChargeGoal(CQBullEntity bull, boolean canBreakBlocks) {
        this.bull = bull;
        this.canBreakBlocks = canBreakBlocks;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        LivingEntity target = this.bull.getTarget();
        if (target != null && target.isAlive()) {
            int chance = this.bull.isEnraged() ? 20 : 40;
            return this.bull.distanceToSqr(target) > 16.0 && this.bull.distanceToSqr(target) < 400.0 && this.bull.getRandom().nextInt(chance) == 0;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.prepareTime > 0 || this.isCharging;
    }

    @Override
    public void start() {
        this.prepareTime = 15;
        this.chargeTime = 0;
        this.isCharging = false;
        this.bull.getNavigation().stop();
        this.bull.triggerAnim("action_controller", "charge_prepare");
    }

    public void tick() {
        LivingEntity target = this.bull.getTarget();
        if (target == null) {
            this.isCharging = false;
            return;
        }

        if (this.prepareTime > 0) {
            this.prepareTime--;
            this.bull.getLookControl().setLookAt(target, 30.0F, 30.0F);
            
            double d0 = target.getX() - this.bull.getX();
            double d1 = target.getZ() - this.bull.getZ();
            float targetRot = (float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.bull.setYRot(targetRot);
            this.bull.yBodyRot = this.bull.getYRot();
            this.bull.yHeadRot = this.bull.getYRot();

            if (this.prepareTime == 0) {
                this.isCharging = true;
                this.chargeTime = 0;
                this.bull.triggerAnim("action_controller", "unstoppable_charge");
                
                double ry = Math.toRadians(this.bull.getYRot() + 90.0f);
                this.chargeDirX = Math.cos(ry);
                this.chargeDirZ = Math.sin(ry);
            }
        } else if (this.isCharging) {
            this.chargeTime++;
            
            // Move forward in the locked direction instantly (no slow ramp-up)
            double speed = 1.5;
            this.bull.setDeltaMovement(this.chargeDirX * speed, this.bull.getDeltaMovement().y, this.chargeDirZ * speed);

            // Break blocks
            if (this.canBreakBlocks) {
                Level level = this.bull.level();
                AABB bb = this.bull.getBoundingBox().inflate(0.2);
                BlockPos minPos = BlockPos.containing(bb.minX, bb.minY, bb.minZ);
                BlockPos maxPos = BlockPos.containing(bb.maxX, bb.maxY, bb.maxZ);
                for (int x = minPos.getX(); x <= maxPos.getX(); x++) {
                    for (int y = minPos.getY(); y <= maxPos.getY(); y++) {
                        for (int z = minPos.getZ(); z <= maxPos.getZ(); z++) {
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockState state = level.getBlockState(pos);
                            if (!state.isAir() && !state.is(BlockTags.WITHER_IMMUNE)) {
                                level.destroyBlock(pos, true, this.bull);
                            }
                        }
                    }
                }
            }

            // Damage entities
            AABB damageBox = this.bull.getBoundingBox().inflate(1.0);
            List<Entity> entities = this.bull.level().getEntities(this.bull, damageBox);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && entity != this.bull) {
                    DamageSource source = this.bull.damageSources().mobAttack(this.bull);
                    entity.hurt(source, (float) this.bull.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE) * 1.5f);
                    
                    double dx = entity.getX() - this.bull.getX();
                    double dz = entity.getZ() - this.bull.getZ();
                    entity.push(dx * 1.5, 0.5, dz * 1.5);
                }
            }

            // Stop charging if hit a wall or max time reached
            if (this.bull.horizontalCollision) {
                this.isCharging = false;
                this.bull.setWantsToComboEarthFury(true); // Trigger combo!
            } else if (this.chargeTime > 60) {
                this.isCharging = false;
            }
        }
    }

    @Override
    public void stop() {
        this.prepareTime = 0;
        this.chargeTime = 0;
        this.isCharging = false;
        this.cooldown = this.bull.isEnraged() ? 40 : 80;
        this.bull.triggerAnim("action_controller", "stop_action");
    }
}
