package team.cqr.cqrepoured.entity.ai.navigator;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class MoveHelperDirectFlight extends MoveControl {

	public MoveHelperDirectFlight(Mob entitylivingIn) {
		super(entitylivingIn);
	}

	final Vec3 DIRECTION_UP = new Vec3(0,1,0);
	
	@Override
	public void tick() {
		if (this.operation == MoveControl.Operation.MOVE_TO) {
			this.operation = MoveControl.Operation.WAIT;
			this.mob.setNoGravity(true);
			double d0 = this.wantedX - this.mob.getX();
			double d1 = this.wantedY - this.mob.getY();
			double d2 = this.wantedZ - this.mob.getZ();
			/*
			 * double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			 * 
			 * if (d3 < 2.500000277905201E-7D) { this.entity.setMoveVertical(0.0F); this.entity.setMoveForward(0.0F); return; }
			 */

			float f = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
			this.mob.setYRot(f);// this.limitAngle(this.entity.rotationYaw, f, 10.0F);
			float f1;

			if (this.mob.onGround()) {
				f1 = (float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
			} else {
				f1 = (float) (this.speedModifier * this.mob.getAttribute(Attributes.FLYING_SPEED).getValue());
			}

			this.mob.setSpeed(f1);
			double d4 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
			float f2 = (float) (-(Mth.atan2(d1, d4) * (180D / Math.PI)));
			this.mob.setXRot(f2);// this.limitAngle(this.entity.rotationPitch, f2, 10.0F);
			//this.mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
			//Is this the correct replacement?
			this.mob.moveRelative(d1 > 0.0D ? f1 : -f1, DIRECTION_UP);
		}
		/*
		 * } else { this.entity.setNoGravity(false); this.entity.setMoveVertical(0.0F); this.entity.setMoveForward(0.0F); }
		 */
	}

}
