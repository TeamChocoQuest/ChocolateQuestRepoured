package com.teamcqr.chocolatequestrepoured.client.models.entities.mobs;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRBiped;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCQRGoblin extends ModelCQRBiped {

	public ModelRenderer Nose1;
	public ModelRenderer Nose2;
	public ModelRenderer EarRight1;
	public ModelRenderer EarLeft1;
	public ModelRenderer Body2;
	public ModelRenderer LegOver1;

	public ModelCQRGoblin() {
		super(74, 64, true);

		this.bipedBodyWear = new ModelRenderer(this, 16, 33);
		this.bipedBodyWear.setRotationPoint(-0.5F, 8.0F, -0.5F);
		this.bipedBodyWear.addBox(-3.5F, 0.0F, -1.5F, 8, 8, 3, 0.1F);
		this.bipedBody = new ModelRenderer(this, 16, 17);
		this.bipedBody.setRotationPoint(0.0F, 8.0F, -0.5F);
		this.bipedBody.addBox(-4.0F, 0.0F, -1.5F, 8, 4, 3, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 16, 49);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.setRotationPoint(2.0F, 16.0F, -0.5F);
		this.bipedLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 0, 17);
		this.bipedRightLeg.setRotationPoint(-2.0F, 16.0F, -0.5F);
		this.bipedRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.0F);
		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.setRotationPoint(-4.0F, 10.0F, -0.5F);
		this.bipedRightArmwear.addBox(-3.0F, -2.0F, -1.5F, 3, 12, 3, 0.1F);
		this.EarLeft1 = new ModelRenderer(this, 68, 7);
		this.EarLeft1.setRotationPoint(3.5F, -3.5F, -1.1F);
		this.EarLeft1.addBox(0.0F, -3.0F, 0.0F, 0, 4, 3, 0.0F);
		this.setRotateAngle(this.EarLeft1, 0.0F, 0.4363323129985824F, 0.0F);
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 8.0F, -0.5F);
		this.bipedHead.addBox(-3.5F, -6.0F, -3.5F, 7, 6, 7, 0.0F);
		this.Nose2 = new ModelRenderer(this, 66, 4);
		this.Nose2.setRotationPoint(0.0F, -2.5F, -3.5F);
		this.Nose2.addBox(-0.5F, -1.0F, -1.0F, 1, 1, 1, 0.0F);
		this.Body2 = new ModelRenderer(this, 16, 24);
		this.Body2.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.Body2.addBox(-3.5F, 0.0F, -1.5F, 7, 4, 3, 0.0F);
		this.LegOver1 = new ModelRenderer(this, 52, 21);
		this.LegOver1.setRotationPoint(0.0F, 16.0F, -0.5F);
		this.LegOver1.addBox(-4.0F, 0.0F, -1.5F, 8, 8, 3, 0.2F);
		this.bipedLeftArm = new ModelRenderer(this, 32, 49);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.setRotationPoint(4.0F, 10.0F, -0.5F);
		this.bipedLeftArm.addBox(0.0F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.EarRight1 = new ModelRenderer(this, 68, 3);
		this.EarRight1.setRotationPoint(-3.5F, -3.5F, -1.1F);
		this.EarRight1.addBox(0.0F, -3.0F, 0.0F, 0, 4, 3, 0.0F);
		this.setRotateAngle(this.EarRight1, 0.0F, -0.4363323129985824F, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 40, 17);
		this.bipedRightArm.setRotationPoint(-4.0F, 10.0F, -0.5F);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -1.5F, 3, 12, 3, 0.0F);
		this.bipedHeadwear = new ModelRenderer(this, 32, 1);
		this.bipedHeadwear.setRotationPoint(0.0F, 8.0F, -0.5F);
		this.bipedHeadwear.addBox(-3.5F, -6.0F, -3.5F, 7, 6, 7, 0.1F);
		this.bipedLeftArmwear = new ModelRenderer(this, 48, 49);
		this.bipedLeftArmwear.mirror = true;
		this.bipedLeftArmwear.setRotationPoint(4.0F, 10.0F, -0.5F);
		this.bipedLeftArmwear.addBox(0.0F, -2.0F, -1.5F, 3, 12, 3, 0.1F);
		this.bipedLeftLegwear = new ModelRenderer(this, 0, 49);
		this.bipedLeftLegwear.mirror = true;
		this.bipedLeftLegwear.setRotationPoint(2.0F, 16.0F, -0.5F);
		this.bipedLeftLegwear.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.1F);
		this.Nose1 = new ModelRenderer(this, 66, 0);
		this.Nose1.setRotationPoint(0.0F, -2.5F, -3.5F);
		this.Nose1.addBox(-0.5F, 0.0F, -3.0F, 1, 1, 3, 0.0F);
		this.bipedRightLegwear = new ModelRenderer(this, 0, 33);
		this.bipedRightLegwear.setRotationPoint(-2.0F, 16.0F, -0.5F);
		this.bipedRightLegwear.addBox(-1.5F, 0.0F, -1.5F, 3, 8, 3, 0.1F);

		this.bipedHead.addChild(this.EarLeft1);
		this.bipedHead.addChild(this.Nose2);
		this.bipedBody.addChild(this.Body2);
		this.bipedHead.addChild(this.EarRight1);
		this.bipedHead.addChild(this.Nose1);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.bipedHead.setRotationPoint(0.0F, 8.0F, -0.5F);
		this.bipedBody.setRotationPoint(0.0F, 8.0F, -0.5F);
		this.bipedLeftArm.setRotationPoint(4.0F, 10.0F, -0.5F);
		this.bipedLeftLeg.setRotationPoint(2.0F, 16.0F, -0.5F);
		this.bipedRightLeg.setRotationPoint(-2.0F, 16.0F, -0.5F);
		this.bipedRightArm.setRotationPoint(-4.0F, 10.0F, -0.5F);

		this.bipedHeadwear.setRotationPoint(0.0F, 8.0F, -0.5F);
		this.bipedBodyWear.setRotationPoint(-0.5F, 8.0F, -0.5F);
		this.bipedRightArmwear.setRotationPoint(-4.0F, 10.0F, -0.5F);
		this.bipedLeftArmwear.setRotationPoint(4.0F, 10.0F, -0.5F);
		this.bipedRightLegwear.setRotationPoint(-2.0F, 16.0F, -0.5F);
		this.bipedLeftLegwear.setRotationPoint(2.0F, 16.0F, -0.5F);

		ModelBase.copyModelAngles(this.bipedBody, this.bipedBodyWear);
		ModelBase.copyModelAngles(this.bipedHead, this.bipedHeadwear);
		ModelBase.copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		ModelBase.copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		ModelBase.copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		ModelBase.copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
	}

}
