package com.teamcqr.chocolatequestrepoured.objects.entity.ai.general;

import com.teamcqr.chocolatequestrepoured.objects.entity.EntityCQRHumanBase;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EnumAiState;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class AIHumanIdleTalkClosest extends EntityAIWatchClosest {
	EntityCQRHumanBase owner;

	@SuppressWarnings("unchecked")
	public AIHumanIdleTalkClosest(EntityCQRHumanBase par1EntityLiving, @SuppressWarnings("rawtypes") Class par2Class,
			float par3) {
		super(par1EntityLiving, par2Class, par3);
		this.owner = par1EntityLiving;
		this.setMutexBits(4);
	}

	public boolean shouldExecute() {
		boolean flag = super.shouldExecute();
		if (this.owner.getNavigator().getPath() == null && this.owner.getAttackTarget() == null && flag) {
			if (this.owner.AIMode == EnumAiState.WARD.ordinal()) {
				return false;
			} else {
				return this.owner.isSuitableTargetAlly((EntityLiving) this.closestEntity)
						&& this.owner.getDistance(this.closestEntity) < 5.0F;
			}
		} else {
			return false;
		}
	}

	public void startExecuting() {
		super.startExecuting();
		this.owner.setSpeaking(true);
		this.handShake(60);
	}

	public void handShake(int chance) {
		if (this.closestEntity != null) {
			int rnd = this.owner.getRNG().nextInt(chance);
			if (rnd == 0) {
				this.owner.swingItem();
			} else if (rnd == 1) {
				this.owner.swingItem();
			} else if (rnd > chance - 10 && this.owner.getDistance(this.closestEntity) < 3.0F) {
				this.owner.swingItem();
				((EntityLivingBase) this.closestEntity).swingItem();
			}

		}
	}

	public void resetTask() {
		super.resetTask();
		this.owner.setSpeaking(false);
		this.handShake(40);
	}
}
