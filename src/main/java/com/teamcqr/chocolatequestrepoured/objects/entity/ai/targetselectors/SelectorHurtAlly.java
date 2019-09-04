package com.teamcqr.chocolatequestrepoured.objects.entity.ai.targetselectors;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class SelectorHurtAlly extends EntitySelector {
      EntityCQRHumanBase taskOwner;

      public SelectorHurtAlly(EntityCQRHumanBase owner) {
            this.taskOwner = owner;
      }

      public boolean isEntityApplicable(Entity parEntity) {
            if (parEntity instanceof EntityLivingBase && parEntity != this.taskOwner) {
                  EntityLivingBase entity = (EntityLivingBase)parEntity;
                  if (this.taskOwner.isSuitableTargetAlly(entity)) {
                        return entity.getHealth() < entity.getMaxHealth();
                  } else {
                        return false;
                  }
            } else {
                  return false;
            }
      }
}
