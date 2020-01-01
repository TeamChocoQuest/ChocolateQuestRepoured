package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCQRGolem extends ModelCQRBiped {

	// fields
	ModelRenderer nose;
	ModelRenderer bodyLower;

	public ModelCQRGolem(float modelSize) {
		// super(modelSize, false);
		this(modelSize, 0, 64, 64);
	}

	public ModelCQRGolem(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
		super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, false);

		/*
		 * textureWidth = 64;
		 * textureHeight = 64;
		 */

		// Biped components that need to be "adjusted"
		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, modelSize);
		this.bipedHead.setRotationPoint(0F, 0F, 0F);
		this.bipedHead.setTextureSize(64, 64);
		this.bipedHead.mirror = true;
		this.setRotation(this.bipedHead, 0F, 0F, 0F);

		this.bipedHeadwear.showModel = false;

		this.bipedRightArm = new ModelRenderer(this, 40, 32);
		this.bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4, modelSize);
		this.bipedRightArm.setRotationPoint(-5F, 2F, 0F);
		this.bipedRightArm.setTextureSize(64, 64);
		this.bipedRightArm.mirror = true;
		this.setRotation(this.bipedRightArm, 0F, 0F, 0F);

		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4, modelSize);
		this.bipedLeftArm.setRotationPoint(5F, 2F, 0F);
		this.bipedLeftArm.setTextureSize(64, 64);
		this.bipedLeftArm.mirror = true;
		this.setRotation(this.bipedLeftArm, 0F, 0F, 0F);

		this.bipedRightLeg = new ModelRenderer(this, 0, 32);
		this.bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, modelSize);
		this.bipedRightLeg.setRotationPoint(-3F, 12F, 0F);
		this.bipedRightLeg.setTextureSize(64, 64);
		this.bipedRightLeg.mirror = true;
		this.setRotation(this.bipedRightLeg, 0F, 0F, 0F);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, modelSize);
		this.bipedLeftLeg.setRotationPoint(3F, 12F, 0F);
		this.bipedLeftLeg.setTextureSize(64, 64);
		this.bipedLeftLeg.mirror = true;
		this.setRotation(this.bipedLeftLeg, 0F, 0F, 0F);
		//

		this.nose = new ModelRenderer(this, 32, 0);
		this.nose.addBox(-1F, -3F, -6F, 2, 4, 2, modelSize);
		this.nose.setRotationPoint(0F, 0F, 0F);
		this.nose.setTextureSize(64, 64);
		this.nose.mirror = true;
		this.setRotation(this.nose, 0F, 0F, 0F);

		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.addBox(-4F, 0F, -2F, 8, 6, 4, modelSize);
		this.bipedBody.setRotationPoint(0F, 0F, 0F);
		this.bipedBody.setTextureSize(64, 64);
		this.bipedBody.mirror = true;
		this.setRotation(this.bipedBody, 0F, 0F, 0F);

		this.bodyLower = new ModelRenderer(this, 18, 26);
		this.bodyLower.addBox(-3F, 6F, -2F, 6, 6, 4, modelSize);
		this.bodyLower.setRotationPoint(0F, 0F, 0F);
		this.bodyLower.setTextureSize(64, 64);
		this.bodyLower.mirror = true;
		this.setRotation(this.bodyLower, 0F, 0F, 0F);

		this.bipedBody.addChild(this.bodyLower);
		this.bipedHead.addChild(this.nose);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
