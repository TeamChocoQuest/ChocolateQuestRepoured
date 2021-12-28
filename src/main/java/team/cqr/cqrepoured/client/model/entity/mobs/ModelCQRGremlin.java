package team.cqr.cqrepoured.client.model.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import team.cqr.cqrepoured.client.model.entity.ModelCQRBiped;

public class ModelCQRGremlin extends ModelCQRBiped {

	public ModelRenderer nose;
	public ModelRenderer hornR1;
	public ModelRenderer hornL1;
	public ModelRenderer hornR2;
	public ModelRenderer hornL2;

	public ModelCQRGremlin() {
		super(128, 64);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.125F);
		this.bipedHead.setRotationPoint(0.0F, 10.0F, -4.0F);

		this.nose = new ModelRenderer(this, 0, 32);
		this.nose.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 2, 0.0F);
		this.nose.setRotationPoint(0.0F, -1.0F, -4.0F);
		this.setRotationDeg(this.nose, -25.0D, 0.0D, 0.0D);
		this.bipedHead.addChild(this.nose);

		this.hornR1 = new ModelRenderer(this, 8, 32);
		this.hornR1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
		this.hornR1.setRotationPoint(-3.5F, -3.5F, -3.5F);
		this.setRotationDeg(this.hornR1, 45.0D, 45.0D, 0.0D);
		this.bipedHead.addChild(this.hornR1);

		this.hornL1 = new ModelRenderer(this, 16, 32);
		this.hornL1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
		this.hornL1.setRotationPoint(3.5F, -3.5F, -3.5F);
		this.setRotationDeg(this.hornL1, 45.0D, -45.0D, 0.0D);
		this.bipedHead.addChild(this.hornL1);

		this.hornR2 = new ModelRenderer(this, 24, 32);
		this.hornR2.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
		this.hornR2.setRotationPoint(0.0F, -0.7F, 0.0F);
		this.setRotationDeg(this.hornR2, -45.0D, 45.0D, 0.0D);
		this.hornR1.addChild(this.hornR2);

		this.hornL2 = new ModelRenderer(this, 28, 32);
		this.hornL2.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
		this.hornL2.setRotationPoint(0.0F, -0.7F, 0.0F);
		this.setRotationDeg(this.hornL2, -39.13D, 5.22D, -45.0D);
		this.hornL1.addChild(this.hornL2);

		this.bipedBody = new ModelRenderer(this, 32, 0);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.bipedBody.setRotationPoint(0.0F, 13.0F, -4.0F);
		this.setRotationDeg(this.bipedBody, 60.0D, 0.0D, 0.0D);

		this.bipedRightArm = new ModelRenderer(this, 56, 0);
		this.bipedRightArm.addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedRightArm.setRotationPoint(-4.0F, 14.5F, -2.5F);
		this.setRotationDeg(this.bipedRightArm, -22.5D, 0.0D, 0.0D);

		this.bipedLeftArm = new ModelRenderer(this, 72, 0);
		this.bipedLeftArm.addBox(0.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
		this.bipedLeftArm.setRotationPoint(4.0F, 14.5F, -1.5F);
		this.setRotationDeg(this.bipedLeftArm, -22.0D, 0.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 88, 0);
		this.bipedRightLeg.addBox(-4.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 6.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 104, 0);
		this.bipedLeftLeg.addBox(0.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 6.0F);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor,
			Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.bipedLeftArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.bipedRightArm.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;

		this.bipedBody.setRotationPoint(0.0F, 13.0F, -4.0F);
		this.bipedRightArm.setRotationPoint(-4.0F, 14.5F, -2.5F);
		this.bipedLeftArm.setRotationPoint(4.0F, 14.5F, -1.5F);
		this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 6.0F);
		this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 6.0F);
		this.bipedHead.setRotationPoint(0.0F, 10.0F, -4.0F);
		this.setRotationDeg(this.bipedBody, 1.0471975511965976F, 0.0F, 0.0F);
	}

}
