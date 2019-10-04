package com.teamcqr.chocolatequestrepoured.client.models.entities;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelCQRBiped extends ModelBiped {

	public ModelRenderer bipedLeftArmwear = null;
	public ModelRenderer bipedRightArmwear = null;
	public ModelRenderer bipedLeftLegwear = null;
	public ModelRenderer bipedRightLegwear = null;
	public ModelRenderer bipedBodyWear = null;
	public ModelRenderer bipedCape = null;

	public boolean hasExtraLayers = true;

	public ModelCQRBiped(float modelSize, boolean hasExtraLayer) {
		/*
		 * super(modelSize); this.bipedCape = new ModelRenderer(this, 0, 0);
		 * this.bipedCape.setTextureSize(64, 32); this.bipedCape.addBox(-5.0F, 0.0F,
		 * -1.0F, 10, 16, 1, modelSize);
		 * 
		 * initExtraLayer(modelSize);
		 */
		this(modelSize, 0, 64, 64, hasExtraLayer);
	}

	public ModelCQRBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn,
			boolean hasExtraLayer) {
		super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn);
		this.hasExtraLayers = hasExtraLayer;
		
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);

		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

		initExtraLayer(modelSize);
		setClothingLayerVisible(!hasExtraLayer);
	}

	private void initExtraLayer(float modelSize) {
		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
		this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
		this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
		this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
		this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);

		this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
		this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
		this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
		this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
		this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
		this.bipedBodyWear = new ModelRenderer(this, 16, 32);
		this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
		this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public void renderCape(float scale) {
		this.bipedCape.render(scale);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used
	 * for animating the movement of arms and legs, where par1 represents the
	 * time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
		copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		copyModelAngles(this.bipedBody, this.bipedBodyWear);

		if (entityIn.isSneaking()) {
			this.bipedCape.rotationPointY = 2.0F;
		} else {
			this.bipedCape.rotationPointY = 0.0F;
		}
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.bipedCape.showModel = visible;

		if (hasExtraLayers) {
			setClothingLayerVisible(visible);
		}
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		GlStateManager.pushMatrix();

		if (hasExtraLayers) {
			this.bipedLeftLegwear.render(scale);
			this.bipedRightLegwear.render(scale);
			this.bipedLeftArmwear.render(scale);
			this.bipedRightArmwear.render(scale);
			this.bipedBodyWear.render(scale);
		}
		if(entityIn instanceof AbstractEntityCQR && ((AbstractEntityCQR)entityIn).hasCape()) {
			this.bipedCape.render(scale);
		}

		GlStateManager.popMatrix();
	}

	protected void setClothingLayerVisible(boolean visible) {
		this.bipedLeftArmwear.showModel = visible;
		this.bipedRightArmwear.showModel = visible;
		this.bipedLeftLegwear.showModel = visible;
		this.bipedRightLegwear.showModel = visible;
		this.bipedBodyWear.showModel = visible;
	}

}
