package team.cqr.cqrepoured.client.model.entity.mobs.animation;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;

public class GreatswordAnimation implements IModelBipedAnimation {

	@Override
	public boolean canApply(AbstractEntityCQR entity) {
		return entity.getHeldItemMainhand().getItem() instanceof ItemGreatSword;
	}

	@Override
	public void apply(ModelCQRBiped model, float ageInTicks, AbstractEntityCQR entity) {
		float f3 = MathHelper.sin(model.swingProgress * (float) Math.PI * 2.0F);
		float f = (float) Math.toRadians(20.0F + 30.0F * f3);
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
		model.bipedRightArm.rotateAngleX += (float) Math.toRadians(-40.0F - 60.0F * f1);
		model.bipedRightArm.rotateAngleY += (float) Math.toRadians(-40.0F);
		model.bipedRightArm.rotateAngleZ += (float) Math.toRadians(0.0F * f1);
		model.bipedLeftArm.rotateAngleX += (float) Math.toRadians(-35.0F - 60.0F * f1);
		model.bipedLeftArm.rotateAngleY += (float) Math.toRadians(50.0F);
		model.bipedLeftArm.rotateAngleZ += (float) Math.toRadians(0.0F * f1);
	}

}
