package com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCQRMonkeyArmor extends ModelBiped {

	public ModelCQRMonkeyArmor(float scale) {
		// super(scale);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.setRotationPoint(2.2F, 12.0F, 0.0F);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);

		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.mirror = true;
		this.bipedRightLeg.setRotationPoint(-2.2F, 12.0F, 0.0F);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);

		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addBox(-4.0F, -0.4F, -6.5F, 8, 12, 4, scale);
		this.setRotateAngle(this.bipedBody, 0.39269908169872414F, 0.0F, 0.0F);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, -6.0F, -5.0F);
		this.bipedHead.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8, scale);

		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.setRotationPoint(-5.0F, 1.5F, -3.0F);
		this.bipedRightArm.addBox(-3.0F, 0.0F, -2.0F, 4, 12, 4, scale);
		this.setRotateAngle(this.bipedRightArm, -0.2617993877991494F, 0.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.setRotationPoint(5.0F, 1.5F, -3.0F);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, 0.0F, -2.0F, 4, 12, 4, scale);
		this.setRotateAngle(this.bipedLeftArm, -0.2617993877991494F, 0.0F, 0.0F);

	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.setRotateAngle(this.bipedBody, 0.39269908169872414F, 0.0F, 0.0F);

		this.bipedRightArm.setRotationPoint(-5.0F, 1.5F, -3.0F);
		this.bipedHead.setRotationPoint(0.0F, -6.0F, -5.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, 1.5F, -3.0F);
	}

}
