package com.example.chocolatequest.entity.ai.boss.piratecaptain;

import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.boss.PirateCaptainEntity;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BossAIPirateTeleportBehindEnemy extends AbstractCQREntityAI<PirateCaptainEntity> {

    private static final double MIN_ATTACK_DISTANCE = 8;
    private static final int MAX_COOLDOWN = 60;
    private int cooldown = MAX_COOLDOWN / 2;
    private int timer = 0;

    public BossAIPirateTeleportBehindEnemy(PirateCaptainEntity entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        this.cooldown--;
        return this.cooldown <= 0 
                && this.entity.getTarget() != null 
                && this.entity.distanceTo(this.entity.getTarget()) >= MIN_ATTACK_DISTANCE 
                && !this.entity.isInvisible() 
                && !this.entity.isReintegrating() 
                && !this.entity.isDisintegrating();
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer < 36 && this.entity.getTarget() != null;
    }
    
    @Override
    public void start() {
        this.timer = 0;
    }

    public void tick() {
        this.timer++;
        if (this.timer == 4) {
            this.entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.PIRATE_DAGGER.get())); // Using Pirate Dagger instead of Ninja
        }

        if (this.timer == 18 && this.entity.getTarget() != null) {
            Vec3 v = this.entity.getTarget().getLookAngle().normalize().scale(2);
            Vec3 p = this.entity.getTarget().position().subtract(v).add(0, 0.5, 0);
            this.entity.randomTeleport(p.x, p.y, p.z, true);
            this.entity.getLookControl().setLookAt(this.entity.getTarget(), 30, 30);
            
            this.cooldown = MAX_COOLDOWN;
        }
    }

    @Override
    public void stop() {
        this.timer = 0;
        this.entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.CAPTAIN_REVOLVER.get()));
    }
}
