package com.teamcqr.chocolatequestrepoured.client.models.entities.customarmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCQREndermanArmor extends ModelBiped {

	public boolean isCarrying;
	public boolean isAttacking;

	public ModelCQREndermanArmor(float scale, boolean legs) {
		super(scale);
		this.bipedHead.setRotationPoint(0.0F, -14.0F, 0.0F);
		this.bipedHeadwear.setRotationPoint(0.0F, -14.0F, 0.0F);
		this.bipedBody.setRotationPoint(0.0F, -14.0F, 0.0F);
		this.bipedRightArm.setRotationPoint(-3.0F, -12.0F, 0.0F);
		this.bipedLeftArm.setRotationPoint(5.0F, -12.0F, 0.0F);
		if (legs) {
			this.bipedRightLeg = new ModelRenderer(this, 0, 16);
			this.bipedRightLeg.addBox(-2.0F, 4.0F, -2.0F, 4, 12, 4, scale);
			this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
			this.bipedLeftLeg.addBox(-2.0F, 4.0F, -2.0F, 4, 12, 4, scale);
		} else {
			this.bipedRightLeg = new ModelRenderer(this, 0, 16);
			this.bipedRightLeg.addBox(-2.0F, 18.0F, -2.0F, 4, 12, 4, scale);
			this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
			this.bipedLeftLeg.addBox(-2.0F, 18.0F, -2.0F, 4, 12, 4, scale);
		}
		this.bipedRightLeg.setRotationPoint(-2.0F, -2.0F, 0.0F);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.setRotationPoint(2.0F, -2.0F, 0.0F);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		float f = -14.0F;
		this.bipedBody.rotateAngleX = 0.0F;
		this.bipedBody.rotationPointY = -14.0F;
		this.bipedBody.rotationPointZ = -0.0F;
		this.bipedRightLeg.rotateAngleX -= 0.0F;
		this.bipedLeftLeg.rotateAngleX -= 0.0F;
		this.bipedRightArm.rotateAngleX = (float) ((double) this.bipedRightArm.rotateAngleX * 0.5D);
		this.bipedLeftArm.rotateAngleX = (float) ((double) this.bipedLeftArm.rotateAngleX * 0.5D);
		this.bipedRightLeg.rotateAngleX = (float) ((double) this.bipedRightLeg.rotateAngleX * 0.5D);
		this.bipedLeftLeg.rotateAngleX = (float) ((double) this.bipedLeftLeg.rotateAngleX * 0.5D);
		float f1 = 0.4F;

		if (this.bipedRightArm.rotateAngleX > 0.4F) {
			this.bipedRightArm.rotateAngleX = 0.4F;
		}

		if (this.bipedLeftArm.rotateAngleX > 0.4F) {
			this.bipedLeftArm.rotateAngleX = 0.4F;
		}

		if (this.bipedRightArm.rotateAngleX < -0.4F) {
			this.bipedRightArm.rotateAngleX = -0.4F;
		}

		if (this.bipedLeftArm.rotateAngleX < -0.4F) {
			this.bipedLeftArm.rotateAngleX = -0.4F;
		}

		if (this.bipedRightLeg.rotateAngleX > 0.4F) {
			this.bipedRightLeg.rotateAngleX = 0.4F;
		}

		if (this.bipedLeftLeg.rotateAngleX > 0.4F) {
			this.bipedLeftLeg.rotateAngleX = 0.4F;
		}

		if (this.bipedRightLeg.rotateAngleX < -0.4F) {
			this.bipedRightLeg.rotateAngleX = -0.4F;
		}

		if (this.bipedLeftLeg.rotateAngleX < -0.4F) {
			this.bipedLeftLeg.rotateAngleX = -0.4F;
		}

		if (this.isCarrying) {
			this.bipedRightArm.rotateAngleX = -0.5F;
			this.bipedLeftArm.rotateAngleX = -0.5F;
			this.bipedRightArm.rotateAngleZ = 0.05F;
			this.bipedLeftArm.rotateAngleZ = -0.05F;
		}

		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointZ = 0.0F;
		this.bipedLeftLeg.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointY = -5.0F;
		this.bipedLeftLeg.rotationPointY = -5.0F;
		this.bipedHead.rotationPointZ = -0.0F;
		this.bipedHead.rotationPointY = -13.0F;
		this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
		this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
		this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;

		if (this.isAttacking) {
			float f2 = 1.0F;
			this.bipedHead.rotationPointY -= 5.0F;
		}
	}

}
