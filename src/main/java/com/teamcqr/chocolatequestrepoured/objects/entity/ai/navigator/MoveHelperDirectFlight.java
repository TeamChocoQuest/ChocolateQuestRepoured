package com.teamcqr.chocolatequestrepoured.objects.entity.ai.navigator;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class MoveHelperDirectFlight extends EntityMoveHelper {

	public MoveHelperDirectFlight(EntityLiving entitylivingIn) {
		super(entitylivingIn);
	}

	public void onUpdateMoveHelper() {
		if (this.action == EntityMoveHelper.Action.MOVE_TO) {
			this.action = EntityMoveHelper.Action.WAIT;
			this.entity.setNoGravity(true);
			double d0 = this.posX - this.entity.posX;
			double d1 = this.posY - this.entity.posY;
			double d2 = this.posZ - this.entity.posZ;
			/*
			 * double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			 * 
			 * if (d3 < 2.500000277905201E-7D) { this.entity.setMoveVertical(0.0F); this.entity.setMoveForward(0.0F); return; }
			 */

			float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
			this.entity.rotationYaw = f;// this.limitAngle(this.entity.rotationYaw, f, 10.0F);
			float f1;

			if (this.entity.onGround) {
				f1 = (float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
			} else {
				f1 = (float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue());
			}

			this.entity.setAIMoveSpeed(f1);
			double d4 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
			float f2 = (float) (-(MathHelper.atan2(d1, d4) * (180D / Math.PI)));
			this.entity.rotationPitch = f2;// this.limitAngle(this.entity.rotationPitch, f2, 10.0F);
			this.entity.setMoveVertical(d1 > 0.0D ? f1 : -f1);
		}
		/*
		 * } else { this.entity.setNoGravity(false); this.entity.setMoveVertical(0.0F); this.entity.setMoveForward(0.0F); }
		 */
	}

}
