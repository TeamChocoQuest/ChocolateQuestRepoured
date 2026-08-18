package com.example.chocolatequest.entity.ai.goal;

import com.example.chocolatequest.entity.bases.AbstractEntityCQR;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CQRPatrolPathGoal extends Goal {
    private final AbstractEntityCQR mob;
    private final double speed;
    private int waitTicks;
    private int repathTicks;

    public CQRPatrolPathGoal(AbstractEntityCQR mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.hasPatrolPath() && mob.getTarget() == null && !mob.isSitting() && !mob.hasLeader();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        waitTicks = 0;
        repathTicks = 0;
        moveToCurrentNode();
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
        waitTicks = 0;
    }

    @Override
    public void tick() {
        BlockPos target = mob.getCurrentPatrolNode();
        if (target == null) return;

        double distance = mob.distanceToSqr(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);
        if (distance <= 2.25D) {
            mob.getNavigation().stop();
            if (waitTicks <= 0) waitTicks = 20;
            if (--waitTicks <= 0) {
                mob.advancePatrolNode();
                moveToCurrentNode();
            }
            return;
        }

        if (--repathTicks <= 0 || mob.getNavigation().isDone()) {
            repathTicks = 20;
            moveToCurrentNode();
        }
    }

    private void moveToCurrentNode() {
        BlockPos target = mob.getCurrentPatrolNode();
        if (target != null) {
            mob.getNavigation().moveTo(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, speed);
        }
    }
}
