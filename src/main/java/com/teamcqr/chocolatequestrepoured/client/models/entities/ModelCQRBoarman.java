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

		righttusk = new ModelRenderer(this, 56, 16);
		righttusk.addBox(-1F, -1F, -2F, 1, 2, 2);
		righttusk.setRotationPoint(-2F, -2F, -4F);
		righttusk.setTextureSize(64, 64);
		righttusk.mirror = true;
		setRotation(righttusk, 0F, 0.7853982F, 0F);
		lefttusk = new ModelRenderer(this, 56, 16);
		lefttusk.addBox(0F, -1F, -2F, 1, 2, 2);
		lefttusk.setRotationPoint(2F, -2F, -4F);
		lefttusk.setTextureSize(64, 64);
		lefttusk.mirror = true;
		setRotation(lefttusk, 0F, -0.7853982F, 0F);
		mane01 = new ModelRenderer(this, 56, 18);
		mane01.addBox(0F, 0F, 0F, 0, 12, 2);
		mane01.setRotationPoint(0F, 0F, 2F);
		mane01.setTextureSize(64, 64);
		mane01.mirror = true;
		setRotation(mane01, 0F, 0.2617994F, 0F);
		mane02 = new ModelRenderer(this, 60, 18);
		mane02.addBox(0F, 0F, 0F, 0, 12, 2);
		mane02.setRotationPoint(0F, 0F, 2F);
		mane02.setTextureSize(64, 64);
		mane02.mirror = true;
		setRotation(mane02, 0F, -0.2617994F, 0F);

		this.bipedBody.addChild(mane01);
		this.bipedBody.addChild(mane02);
		this.bipedHead.addChild(righttusk);
		this.bipedHead.addChild(lefttusk);
	}

	// Model from techne
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
