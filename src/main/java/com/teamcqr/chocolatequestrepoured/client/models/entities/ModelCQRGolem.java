package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCQRGolem extends ModelCQRBiped {
	
	// fields
	ModelRenderer nose;
	ModelRenderer bodyLower;

	public ModelCQRGolem(float modelSize) {
		//super(modelSize, false);
		this(modelSize, 0, 64, 64);
	}

	public ModelCQRGolem(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
		super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, false);
		
		/*textureWidth = 64;
		textureHeight = 64;*/

		//Biped components that need to be "adjusted"
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.setTextureSize(64, 64);
		bipedHead.mirror = true;
		setRotation(bipedHead, 0F, 0F, 0F);
		
		this.bipedHeadwear.showModel = false;

		bipedRightArm = new ModelRenderer(this, 40, 32);
		bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4);
		bipedRightArm.setRotationPoint(-5F, 2F, 0F);
		bipedRightArm.setTextureSize(64, 64);
		bipedRightArm.mirror = true;
		setRotation(bipedRightArm, 0F, 0F, 0F);

		bipedLeftArm = new ModelRenderer(this, 40, 16);
		bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4);
		bipedLeftArm.setRotationPoint(5F, 2F, 0F);
		bipedLeftArm.setTextureSize(64, 64);
		bipedLeftArm.mirror = true;
		setRotation(bipedLeftArm, 0F, 0F, 0F);

		bipedRightLeg = new ModelRenderer(this, 0, 32);
		bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		bipedRightLeg.setRotationPoint(-3F, 12F, 0F);
		bipedRightLeg.setTextureSize(64, 64);
		bipedRightLeg.mirror = true;
		setRotation(bipedRightLeg, 0F, 0F, 0F);

		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		bipedLeftLeg.setRotationPoint(3F, 12F, 0F);
		bipedLeftLeg.setTextureSize(64, 64);
		bipedLeftLeg.mirror = true;
		setRotation(bipedLeftLeg, 0F, 0F, 0F);
		//
		
		nose = new ModelRenderer(this, 32, 0);
		nose.addBox(-1F, -3F, -6F, 2, 4, 2);
		nose.setRotationPoint(0F, 0F, 0F);
		nose.setTextureSize(64, 64);
		nose.mirror = true;
		setRotation(nose, 0F, 0F, 0F);
		bipedBody = new ModelRenderer(this, 16, 16);
		bipedBody.addBox(-4F, 0F, -2F, 8, 6, 4);
		bipedBody.setRotationPoint(0F, 0F, 0F);
		bipedBody.setTextureSize(64, 64);
		bipedBody.mirror = true;
		setRotation(bipedBody, 0F, 0F, 0F);
		bodyLower = new ModelRenderer(this, 18, 26);
		bodyLower.addBox(-3F, 6F, -2F, 6, 6, 4);
		bodyLower.setRotationPoint(0F, 0F, 0F);
		bodyLower.setTextureSize(64, 64);
		bodyLower.mirror = true;
		setRotation(bodyLower, 0F, 0F, 0F);
		
		this.bipedBody.addChild(bodyLower);
		this.bipedHead.addChild(nose);
	}


	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
