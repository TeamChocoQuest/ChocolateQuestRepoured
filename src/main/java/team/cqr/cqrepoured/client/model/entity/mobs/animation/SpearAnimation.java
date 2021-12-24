package team.cqr.cqrepoured.client.model.entity.mobs.animation;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;

public class SpearAnimation implements IModelBipedAnimation {

	@Override
	public boolean canApply(AbstractEntityCQR entity) {
		return entity.getHeldItemMainhand().getItem() instanceof ItemSpearBase;
	}

	@Override
	public void apply(ModelCQRBiped model, float ageInTicks, AbstractEntityCQR entity) {
		float f = (float) Math.toRadians(40.0F);
		model.bipedBody.rotateAngleX = 0.0F;
		model.bipedBody.rotateAngleY = f;
		model.bipedBody.rotateAngleZ = 0.0F;

		Vec3d v = new Vec3d(-5.0F, 0.0F, 0.0F).rotateYaw(f);
		model.bipedRightArm.rotationPointX = (float) v.x;
		model.bipedRightArm.rotationPointZ = (float) v.z;
		model.bipedRightArm.rotateAngleX = 0.0F;
		model.bipedRightArm.rotateAngleY = f;
		model.bipedRightArm.rotateAngleZ = 0.0F;

		Vec3d v1 = new Vec3d(5.0F, 0.0F, 0.0F).rotateYaw(f);
		model.bipedLeftArm.rotationPointX = (float) v1.x;
		model.bipedLeftArm.rotationPointZ = (float) v1.z;
		model.bipedLeftArm.rotateAngleX = 0.0F;
		model.bipedLeftArm.rotateAngleY = f;
		model.bipedLeftArm.rotateAngleZ = 0.0F;

		float f1 = MathHelper.sin(model.swingProgress * (float) Math.PI);
		model.bipedRightArm.rotateAngleX += (float) Math.toRadians(-10.0F - 20.0F * f1);
		model.bipedRightArm.rotateAngleY += (float) Math.toRadians(-45.0F);
		model.bipedLeftArm.rotateAngleX += (float) Math.toRadians(-45.0F - 25.0F * f1);
		model.bipedLeftArm.rotateAngleY += (float) Math.toRadians(30.0F - 10.0F * f1);
	}

}
