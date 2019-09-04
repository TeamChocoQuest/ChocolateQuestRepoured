package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;

public class AILavaSwim extends EntityAIBase {
      EntityCreature owner;

      public AILavaSwim(EntityCreature owner) {
            this.owner = owner;
            this.setMutexBits(4);
      }

      public boolean shouldExecute() {
            return this.owner.handleWaterMovement();
      }

      public void updateTask() {
            if (this.owner.getRNG().nextFloat() < 0.8F) {
                  this.owner.getJumpHelper().setJumping();
                  this.owner.motionY = 1.0D;
                  this.owner.motionX = -Math.sin(Math.toRadians((double)this.owner.rotationYaw)) * 0.5D;
                  this.owner.motionZ = Math.cos(Math.toRadians((double)this.owner.rotationYaw)) * 0.5D;
            }

      }
}
