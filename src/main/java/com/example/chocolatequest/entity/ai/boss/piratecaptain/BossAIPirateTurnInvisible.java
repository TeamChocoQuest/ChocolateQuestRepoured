package com.example.chocolatequest.entity.ai.boss.piratecaptain;

import com.example.chocolatequest.entity.ai.AbstractCQREntityAI;
import com.example.chocolatequest.entity.boss.PirateCaptainEntity;
import com.example.chocolatequest.registry.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class BossAIPirateTurnInvisible extends AbstractCQREntityAI<PirateCaptainEntity> {

    private int cooldown = 0;
    private int invisibleTime = 0;

    public BossAIPirateTurnInvisible(PirateCaptainEntity entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        if (this.entity != null && this.entity.getHealth() / this.entity.getMaxHealth() <= 0.5 && this.entity.getTarget() != null && !this.entity.isDeadOrDying()) {
            this.cooldown--;
            return this.cooldown <= 0;
        }
        return false;
    }

    @Override
    public void start() {
        this.invisibleTime = 200;
        this.entity.setInvisible(true);
    }

    @Override
    public boolean canContinueToUse() {
        return this.invisibleTime > 0;
    }

    public void tick() {
        boolean disInt = false;
        boolean reInt = false;
        
        if (this.invisibleTime <= PirateCaptainEntity.TURN_INVISIBLE_ANIMATION_TIME) {
            reInt = true;
            this.entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.CAPTAIN_REVOLVER.get()));
        } else if (this.invisibleTime >= 200 - PirateCaptainEntity.TURN_INVISIBLE_ANIMATION_TIME) {
            disInt = true;
            this.entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.PIRATE_DAGGER.get()));
        }
        
        this.invisibleTime--;
        
        if (this.invisibleTime <= 0) {
            disInt = false;
            reInt = false;
            this.cooldown = 100;
            this.entity.setInvisible(false);
        }
        
        this.entity.setIsReintegrating(reInt);
        this.entity.setIsDisintegrating(disInt);
    }
}
