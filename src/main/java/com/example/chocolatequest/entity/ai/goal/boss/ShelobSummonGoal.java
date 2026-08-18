package com.example.chocolatequest.entity.ai.goal.boss;

import com.example.chocolatequest.entity.boss.ShelobEntity;
import com.example.chocolatequest.entity.mob.SpiderMinionEntity;
import com.example.chocolatequest.registry.ModEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ShelobSummonGoal extends Goal {
    private final ShelobEntity entity;
    private static final int MAX_MINIONS = 6;
    private static final int MAX_MINIONS_AT_A_TIME = 3;
    private static final int MAX_COOLDOWN = 150;
    private static final int MIN_COOLDOWN = 50;
    private int cooldown = 0;

    public ShelobSummonGoal(ShelobEntity entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
        }
        if (this.entity.getTarget() == null) {
            return false;
        }
        if (this.getAliveMinionCount() < MAX_MINIONS) {
            if (this.entity.getHealth() / this.entity.getMaxHealth() <= 0.75F) {
                return this.cooldown <= 0;
            }
        }
        return false;
    }

    private int getAliveMinionCount() {
        int aliveMinions = 0;
        for (Entity minion : this.entity.getSummonedEntities()) {
            if (minion != null && minion.isAlive()) {
                aliveMinions++;
            }
        }
        return aliveMinions;
    }

    @Override
    public void start() {
        int minionCount = Math.min(MAX_MINIONS_AT_A_TIME, MAX_MINIONS - this.getAliveMinionCount());
        if (minionCount <= 0) return;
        
        double angle = 360.0 / minionCount;
        Vec3 v = new Vec3(1, 0, 0);
        
        for (int i = 0; i < minionCount; i++) {
            Vec3 pos = this.entity.position().add(v);
            v = v.yRot((float) Math.toRadians(angle));

            SpiderMinionEntity minion = new SpiderMinionEntity(ModEntities.SPIDER_MINION.get(), this.entity.level());
            minion.setPos(pos.x, pos.y, pos.z);
            this.entity.level().addFreshEntity(minion);
            
            this.entity.addSummonedEntityToList(minion);
        }
        
        this.cooldown = MIN_COOLDOWN + this.entity.getRandom().nextInt(MAX_COOLDOWN - MIN_COOLDOWN + 1);
        this.entity.triggerAnim("base_controller", "shoot");
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}
