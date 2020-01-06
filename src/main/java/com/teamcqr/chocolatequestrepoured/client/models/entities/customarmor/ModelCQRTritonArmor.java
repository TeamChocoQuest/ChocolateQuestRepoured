package com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCQRTritonArmor extends ModelBiped {

	public ModelCQRTritonArmor(float scale) {
		super(scale);

		this.bipedRightLeg = new ModelRenderer(this, 0, 0);
		this.bipedRightLeg.addBox(0, 0, 0, 0, 0, 0, scale);
		this.bipedRightLeg.setRotationPoint(0, 0, 0F);
		this.bipedRightLeg.setTextureSize(0, 0);
		this.bipedRightLeg.mirror = true;
		this.setRotation(this.bipedRightLeg, 0F, 0F, 0F);
		this.bipedRightLeg.showModel = false;

		this.bipedLeftLeg = new ModelRenderer(this, 0, 0);
		this.bipedLeftLeg.addBox(0, 0F, 0F, 0, 0, 0, scale);
		this.bipedLeftLeg.setRotationPoint(0F, 0F, 0F);
		this.bipedLeftLeg.setTextureSize(0, 0);
		this.bipedLeftLeg.mirror = true;
		this.setRotation(this.bipedLeftLeg, 0F, 0F, 0F);
		this.bipedLeftLeg.showModel = false;
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.bipedLeftLeg.showModel = false;
		this.bipedRightLeg.showModel = false;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
	}
}
