package com.teamcqr.chocolatequestrepoured.client.models.entities;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelTriton - DerToaster98
 * Created using Tabula 7.0.1
 */
public class ModelCQRTriton extends ModelCQRBiped {
	public ModelRenderer mouthTentacle1;
	public ModelRenderer mouthTentacle2;
	public ModelRenderer mouthTentacle3;
	public ModelRenderer mouthTentacle4;
	public ModelRenderer tail1;
	public ModelRenderer tail2;
	public ModelRenderer tail3;
	public ModelRenderer tailEnd1;
	public ModelRenderer tailEnd2;
	public ModelRenderer tailEnd3;

	public ModelCQRTriton(float modelSize) {
		this(modelSize, 0, 64, 64);
	}

	public ModelCQRTriton(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn) {
		super(modelSize, p_i1149_2_, textureWidthIn, textureHeightIn, false);

		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;

		this.disableLegs();

		this.tailEnd1 = new ModelRenderer(this, 42, 57);
		this.tailEnd1.setRotationPoint(0.0F, 1.0F, 3.4F);
		this.tailEnd1.addBox(-1.5F, 0.0F, 0.5F, 3, 3, 4, modelSize);

		this.tail2 = new ModelRenderer(this, 0, 48);
		this.tail2.setRotationPoint(0.0F, 4.0F, 1.0F);
		this.tail2.addBox(-3.0F, 0.0F, 0.0F, 6, 4, 4, modelSize);

		this.tail1 = new ModelRenderer(this, 0, 56);
		this.tail1.setRotationPoint(0.0F, 12.0F, -1.55F);
		this.tail1.addBox(-4.0F, 0.0F, 0.0F, 8, 4, 4, modelSize);

		this.bipedBody = new ModelRenderer(this, 0, 16);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);

		this.mouthTentacle1 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle1.setRotationPoint(-2.0F, -3.5F, -3.0F);
		this.mouthTentacle1.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, modelSize);
		this.setRotateAngle(this.mouthTentacle1, -0.6981317007977318F, 0.0F, 0.0F);

		this.mouthTentacle3 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle3.setRotationPoint(0.0F, -3.5F, -3.0F);
		this.mouthTentacle3.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, modelSize);
		this.setRotateAngle(this.mouthTentacle3, -0.6981317007977318F, 0.0F, 0.0F);

		this.tailEnd2 = new ModelRenderer(this, 42, 57);
		this.tailEnd2.setRotationPoint(0.0F, 0.0F, 4.5F);
		this.tailEnd2.addBox(-1.5F, 0.0F, 0.0F, 3, 3, 4, modelSize);

		this.mouthTentacle2 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle2.setRotationPoint(-1.0F, -3.5F, -3.0F);
		this.mouthTentacle2.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, modelSize);
		this.setRotateAngle(this.mouthTentacle2, -0.6981317007977318F, 0.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 24, 16);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);

		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);

		this.tailEnd3 = new ModelRenderer(this, 42, 57);
		this.tailEnd3.setRotationPoint(0.0F, 0.0F, 4.0F);
		this.tailEnd3.addBox(-1.5F, 0.0F, 0.0F, 3, 3, 4, modelSize);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);

		this.mouthTentacle4 = new ModelRenderer(this, 32, 0);
		this.mouthTentacle4.setRotationPoint(1.0F, -3.5F, -3.0F);
		this.mouthTentacle4.addBox(0.0F, 1.5F, 0.0F, 1, 4, 1, modelSize);
		this.setRotateAngle(this.mouthTentacle4, -0.6981317007977318F, 0.0F, 0.0F);

		this.tail3 = new ModelRenderer(this, 24, 56);
		this.tail3.setRotationPoint(0.0F, 4.0F, 1.5F);
		this.tail3.addBox(-2.5F, 0.0F, 0.0F, 5, 4, 4, modelSize);

		// Children relations
		this.tail3.addChild(this.tailEnd1);
		this.tail1.addChild(this.tail2);
		this.bipedBody.addChild(this.tail1);
		this.bipedHead.addChild(this.mouthTentacle1);
		this.bipedHead.addChild(this.mouthTentacle3);
		this.tailEnd1.addChild(this.tailEnd2);
		this.bipedHead.addChild(this.mouthTentacle2);
		this.tailEnd2.addChild(this.tailEnd3);
		this.bipedHead.addChild(this.mouthTentacle4);
		this.tail2.addChild(this.tail3);
	}

	private void disableLegs() {
		this.bipedLeftLeg.isHidden = true;
		this.bipedLeftLeg.setTextureSize(0, 0);
		this.bipedLeftLegwear.isHidden = true;
		this.bipedLeftLegwear.setTextureSize(0, 0);

		this.bipedRightLeg.isHidden = true;
		this.bipedRightLeg.setTextureSize(0, 0);
		this.bipedRightLegwear.isHidden = true;
		this.bipedRightLegwear.setTextureSize(0, 0);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.bipedBody.render(f5);
		this.bipedLeftArm.render(f5);
		this.bipedRightArm.render(f5);
		this.bipedHead.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		// System.out.println("Limb swing: " + limbSwing);
		// System.out.println("Limb swing amount: " + limbSwingAmount);
		if (Math.abs(limbSwingAmount) > 0.1) {
			this.renderSlitheringTail(ageInTicks, 1.5F);
			this.renderSlitheringTailEnd(ageInTicks, 1.0F, 1.5F);
		} else {
			this.renderSlitheringTailEnd(ageInTicks, 0.5F, 1.0F);
		}

		this.renderFaceTentaclesAnimation(ageInTicks);
	}

	protected void renderSlitheringTail(float ageInTicks, float multiplierForAngle) {
		float speedMultiplier = 10;
		float sine = new Float(Math.sin(((2F * Math.PI) / speedMultiplier) * ageInTicks)) / 1.5F;
		sine *= multiplierForAngle;
		float sineTmp = sine;
		this.tail1.rotationPointX = sine;
		sine *= 0.85;
		this.tail2.rotationPointX = -sine - sineTmp;
		sineTmp = sine;
		sine *= 0.8;
		this.tail3.rotationPointX = sine + sineTmp;
	}

	protected void renderSlitheringTailEnd(float ageInTicks, float multiplierForAngle, float speedMultiplier) {
		float angleY = new Float(Math.sin(((2F * Math.PI) / (50 / speedMultiplier)) * ageInTicks)) / 4F;
		angleY *= multiplierForAngle;

		this.tailEnd1.rotateAngleY = angleY;
		angleY /= 1.125F;
		this.tailEnd2.rotateAngleY = angleY;
		angleY /= 1.125F;
		this.tailEnd3.rotateAngleY = angleY;
	}

	protected void renderFaceTentaclesAnimation(float ageInTicks) {
		float yAngle = new Float(Math.sin(((2F * Math.PI) / 75) * ageInTicks) + 1);
		yAngle /= 2.5F;

		this.mouthTentacle1.rotateAngleY = yAngle;
		this.mouthTentacle2.rotateAngleY = yAngle / 2.5F;
		this.mouthTentacle3.rotateAngleY = -(yAngle / 2.5F);
		this.mouthTentacle4.rotateAngleY = -yAngle;

		float xAngle = new Float(Math.sin(((2F * Math.PI) / 50) * ageInTicks + (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle1.rotateAngleX = xAngle - 0.6981317007977318F;

		xAngle = new Float(Math.sin(((2F * Math.PI) / 50) * ageInTicks + 3 * (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle2.rotateAngleX = xAngle - 0.6981317007977318F;

		xAngle = new Float(Math.sin(((2F * Math.PI) / 50) * ageInTicks + 5 * (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle3.rotateAngleX = xAngle - 0.6981317007977318F;

		xAngle = new Float(Math.sin(((2F * Math.PI) / 50) * ageInTicks + 7 * (Math.PI / 8)));
		xAngle /= 6F;
		this.mouthTentacle4.rotateAngleX = xAngle - 0.6981317007977318F;
	}
}
