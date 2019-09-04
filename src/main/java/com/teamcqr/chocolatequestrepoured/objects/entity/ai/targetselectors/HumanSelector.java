package com.teamcqr.chocolatequestrepoured.objects.entity.ai.targetselectors;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class HumanSelector extends EntitySelector {
      EntityCQRHumanBase taskOwner;

      public HumanSelector(EntityCQRHumanBase owner) {
            this.taskOwner = owner;
      }

      public boolean isEntityApplicable(Entity parEntity) {
            if (!(parEntity instanceof EntityLivingBase)) {
                  return false;
            } else {
                  EntityLivingBase entity = (EntityLivingBase)parEntity;
                  return this.taskOwner.isSuitableTargetAlly(entity) ? false : this.taskOwner.getEntitySenses().canSee(entity);
            }
      }
}
