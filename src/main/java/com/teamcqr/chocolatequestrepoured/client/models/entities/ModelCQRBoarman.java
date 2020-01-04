package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelRenderer;

public class ModelCQRBoarman extends ModelCQRBiped {

	protected ModelRenderer righttusk;
	protected ModelRenderer lefttusk;
	protected ModelRenderer mane01;
	protected ModelRenderer mane02;

	public ModelCQRBoarman(float modelSize) {
		this(modelSize, 0F, 64, 64);
	}

	public ModelCQRBoarman(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
		super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, true);

		this.righttusk = new ModelRenderer(this, 56, 16);
		this.righttusk.addBox(-1F, -1F, -2F, 1, 2, 2, modelSize);
		this.righttusk.setRotationPoint(-2F, -2F, -4F);
		this.righttusk.setTextureSize(64, 64);
		this.righttusk.mirror = true;
		this.setRotation(this.righttusk, 0F, 0.7853982F, 0F);
		this.lefttusk = new ModelRenderer(this, 56, 16);
		this.lefttusk.addBox(0F, -1F, -2F, 1, 2, 2, modelSize);
		this.lefttusk.setRotationPoint(2F, -2F, -4F);
		this.lefttusk.setTextureSize(64, 64);
		this.lefttusk.mirror = true;
		this.setRotation(this.lefttusk, 0F, -0.7853982F, 0F);
		this.mane01 = new ModelRenderer(this, 56, 18);
		this.mane01.addBox(0F, 0F, 0F, 0, 12, 2, modelSize);
		this.mane01.setRotationPoint(0F, 0F, 2F);
		this.mane01.setTextureSize(64, 64);
		this.mane01.mirror = true;
		this.setRotation(this.mane01, 0F, 0.2617994F, 0F);
		this.mane02 = new ModelRenderer(this, 60, 18);
		this.mane02.addBox(0F, 0F, 0F, 0, 12, 2, modelSize);
		this.mane02.setRotationPoint(0F, 0F, 2F);
		this.mane02.setTextureSize(64, 64);
		this.mane02.mirror = true;
		this.setRotation(this.mane02, 0F, -0.2617994F, 0F);

		this.bipedBody.addChild(this.mane01);
		this.bipedBody.addChild(this.mane02);
		this.bipedHead.addChild(this.righttusk);
		this.bipedHead.addChild(this.lefttusk);
	}

	// Model from techne
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
