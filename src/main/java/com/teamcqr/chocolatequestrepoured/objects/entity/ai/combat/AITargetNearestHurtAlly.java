package com.teamcqr.chocolatequestrepoured.objects.entity.ai.combat;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.targetselectors.SelectorHurtAlly;

public class AITargetNearestHurtAlly extends EntityAINearestAttackableTarget {
      public AITargetNearestHurtAlly(EntityCQRHumanBase owner, Class targetClass) {
            super(owner, targetClass, 0, true, false, new SelectorHurtAlly(owner));
      }

      public boolean isSuitableTarget(EntityLivingBase par1EntityLivingBase, boolean par2) {
            return true;
      }
}
