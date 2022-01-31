package team.cqr.cqrepoured.entity.ai.navigator;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class MoveHelperDirectFlight extends MovementController {

	public MoveHelperDirectFlight(MobEntity entitylivingIn) {
		super(entitylivingIn);
	}

	final Vector3d DIRECTION_UP = new Vector3d(0,1,0);
	
	@Override
	public void tick() {
		if (this.operation == MovementController.Action.MOVE_TO) {
			this.operation = MovementController.Action.WAIT;
			this.mob.setNoGravity(true);
			double d0 = this.wantedX - this.mob.getX();
			double d1 = this.wantedY - this.mob.getY();
			double d2 = this.wantedZ - this.mob.getZ();
			/*
			 * double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			 * 
			 * if (d3 < 2.500000277905201E-7D) { this.entity.setMoveVertical(0.0F); this.entity.setMoveForward(0.0F); return; }
			 */

			float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
			this.mob.yRot = f;// this.limitAngle(this.entity.rotationYaw, f, 10.0F);
			float f1;

			if (this.mob.isOnGround()) {
				f1 = (float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
			} else {
				f1 = (float) (this.speedModifier * this.mob.getAttribute(Attributes.FLYING_SPEED).getValue());
			}

			this.mob.setSpeed(f1);
			double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2);
			float f2 = (float) (-(MathHelper.atan2(d1, d4) * (180D / Math.PI)));
			this.mob.xRot = f2;// this.limitAngle(this.entity.rotationPitch, f2, 10.0F);
			//this.mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
			//Is this the correct replacement?
			this.mob.moveRelative(d1 > 0.0D ? f1 : -f1, DIRECTION_UP);
		}
		/*
		 * } else { this.entity.setNoGravity(false); this.entity.setMoveVertical(0.0F); this.entity.setMoveForward(0.0F); }
		 */
	}

}
