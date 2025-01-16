package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMandril;
import team.cqr.cqrepoured.util.PartialTicksUtil;

/**
 * ModelMonkey - Created by DerToaster Created using Tabula 7.0.1
 */
public class ModelCQRMandril extends ModelCQRBiped {

	public ModelRenderer tail1;
	public ModelRenderer snout;
	public ModelRenderer tail2;
	public ModelRenderer tail3;

	public ModelCQRMandril() {
		super(96, 96, true);

		this.snout = new ModelRenderer(this, 64, 0);
		this.snout.setRotationPoint(-2.0F, 5.0F, -7.0F);
		this.snout.addBox(0.0F, 0.0F, 0.0F, 4, 3, 3, 0.0F);

		this.tail1 = new ModelRenderer(this, 64, 16);
		this.tail1.setRotationPoint(-0.5F, 10.0F, -2.5F);
		this.tail1.addBox(0.0F, -0.2F, -1.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.tail1, 1.7453292519943295F, 0.31869712141416456F, 0.003490658503988659F);

		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addBox(-4.0F, -0.4F, -6.5F, 8, 12, 4, 0.0F);
		this.setRotateAngle(this.bipedBody, 0.39269908169872414F, 0.0F, 0.0F);

		this.tail3 = new ModelRenderer(this, 64, 32);
		this.tail3.setRotationPoint(0.0F, 3.5F, 0.0F);
		this.tail3.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.tail3, -0.36425021489121656F, 0.0F, 0.5918411493512771F);

		this.tail2 = new ModelRenderer(this, 64, 24);
		this.tail2.setRotationPoint(0.0F, 3.5F, -1.0F);
		this.tail2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1, 0.0F);
		this.setRotateAngle(this.tail2, 0.36425021489121656F, 0.0F, 0.5009094953223726F);

		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.setRotationPoint(2.2F, 12.0F, 0.0F);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, -6.0F, -5.0F);
		this.bipedHead.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		// this.bipedRightArm.mirror = true;
		this.bipedRightArm.setRotationPoint(-5.0F, 1.5F, -3.0F);
		this.bipedRightArm.addBox(-3.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.bipedRightArm, -0.2617993877991494F, 0.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.setRotationPoint(5.0F, 1.5F, -3.0F);
		this.bipedLeftArm.addBox(-1.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.setRotateAngle(this.bipedLeftArm, -0.2617993877991494F, 0.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		// this.bipedRightLeg.mirror = true;
		this.bipedRightLeg.setRotationPoint(-2.2F, 12.0F, 0.0F);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);

		// Children
		this.bipedHead.addChild(this.snout);
		// Tail
		this.bipedBody.addChild(this.tail1);
		this.tail2.addChild(this.tail3);
		this.tail1.addChild(this.tail2);

		// Clothing layer
		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHeadwear.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, -6.0F, -5.0F);

		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, -0.4F, -6.5F, 8, 12, 4, 0.25F);
		this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.addBox(-1.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftArmwear.setRotationPoint(5.0F, 1.5F, -3.0F);

		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.addBox(-3.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightArmwear.setRotationPoint(-5.0F, 1.5F, -3.0F);

		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedLeftLegwear.setRotationPoint(2.2F, 12.0F, 0.0F);

		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
		this.bipedRightLegwear.setRotationPoint(-2.2F, 12.0F, 0.0F);

		// Copying angles
		ModelBase.copyModelAngles(this.bipedBody, this.bipedBodyWear);
		ModelBase.copyModelAngles(this.bipedHead, this.bipedHeadwear);
		ModelBase.copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		ModelBase.copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		ModelBase.copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		ModelBase.copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.setRotateAngle(this.bipedBody, 0.39269908169872414F, 0.0F, 0.0F);

		this.bipedRightArm.setRotationPoint(-5.0F, 1.5F, -3.0F);
		this.bipedHead.setRotationPoint(0.0F, -6.0F, -5.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 1.5F, -3.0F);

		ModelBase.copyModelAngles(this.bipedBody, this.bipedBodyWear);
		ModelBase.copyModelAngles(this.bipedHead, this.bipedHeadwear);
		ModelBase.copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		ModelBase.copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		ModelBase.copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		ModelBase.copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);

		this.renderTailAnimation(((EntityCQRMandril) entityIn).getTailAnimationProgress(PartialTicksUtil.getCurrentPartialTicks()));
	}

	protected void renderTailAnimation(double tailAnimationProgress) {
		float angleY = MathHelper.sin(2.0F * (float) Math.PI * fastModulo(tailAnimationProgress, 50) / 50.0F) / 3.0F;
		float angleX = MathHelper.cos(2.0F * (float) Math.PI * fastModulo(tailAnimationProgress, 80) / 80.0F) / 4.0F;
		float angleZ = MathHelper.cos(2.0F * (float) Math.PI * fastModulo(tailAnimationProgress, 100) / 100.0F) / 3.0F;
		this.tail1.rotateAngleY = angleY;
		this.tail2.rotateAngleX = angleX * 0.75F;
		this.tail2.rotateAngleZ = -angleX * 0.5F;
		this.tail3.rotateAngleX = angleZ;
	}

	private static float fastModulo(double x, int y) {
		return (int) x % y + (float) (x - MathHelper.floor(x));
	}

}
