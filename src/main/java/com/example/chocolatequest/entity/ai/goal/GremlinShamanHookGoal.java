package com.example.chocolatequest.entity.ai.goal;

import com.example.chocolatequest.entity.boss.EntityCQRGremlinShaman;
import com.example.chocolatequest.item.ItemSpiderHook;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class GremlinShamanHookGoal extends Goal {
    private final EntityCQRGremlinShaman mob;
    private int cooldown = 0;

    public GremlinShamanHookGoal(EntityCQRGremlinShaman mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        }
        LivingEntity target = this.mob.getTarget();
        if (target != null && target.isAlive()) {
            double dist = this.mob.distanceToSqr(target);
            return dist > 64.0D && dist < 400.0D; // Between 8 and 20 blocks away
        }
        return false;
    }

    @Override
    public void start() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (this.mob.getOffhandItem().getItem() instanceof ItemSpiderHook hook) {
                this.mob.swing(InteractionHand.OFF_HAND);
                hook.entityAIshoot(this.mob.level(), this.mob, target, InteractionHand.OFF_HAND);
            }
        }
        this.cooldown = 100; // 5 seconds cooldown
    }
}
