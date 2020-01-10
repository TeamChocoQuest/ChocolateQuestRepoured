package com.teamcqr.chocolatequestrepoured.client.models.armor;

import net.minecraft.client.model.ModelRenderer;

/**
 * ModelCustomArmor - Silentine
 * Created using Tabula 7.0.1
 */
public class ModelArmorHeavy extends ModelCustomArmorBase {
	public ModelRenderer pauldronR1;
	public ModelRenderer pauldronR2;
	public ModelRenderer skirtR;
	public ModelRenderer lowerHeadArmor;
	public ModelRenderer chestExtension;
	public ModelRenderer pauldronL1;
	public ModelRenderer pauldronL2;
	public ModelRenderer skirtL;

	public ModelArmorHeavy(float scale) {
		super(scale, 128, 128);

		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);

		this.pauldronR2 = new ModelRenderer(this, 0, 106);
		this.pauldronR2.setRotationPoint(1.0F, 2.5F, 0.0F);
		this.pauldronR2.addBox(-0.25F, -5.5F, -3.0F, 6, 6, 6, scale);
		this.setRotateAngle(this.pauldronR2, 0.0F, 0.0F, -0.17453292519943295F);

		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);

		this.lowerHeadArmor = new ModelRenderer(this, 0, 64);
		this.lowerHeadArmor.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.lowerHeadArmor.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9, scale);

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);

		this.skirtL = new ModelRenderer(this, 64, 64);
		this.skirtL.mirror = true;
		this.skirtL.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.skirtL.addBox(-2.0F, -3.0F, -3.0F, 6, 6, 6, scale);
		this.setRotateAngle(this.skirtL, 0.0F, 0.0F, -0.2617993877991494F);

		this.pauldronL1 = new ModelRenderer(this, 0, 96);
		this.pauldronL1.mirror = true;
		this.pauldronL1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.pauldronL1.addBox(-1.5F, -2.5F, -2.5F, 5, 5, 5, scale);
		this.setRotateAngle(this.pauldronL1, 0.0F, 0.0F, 0.08726646259971647F);

		this.pauldronL2 = new ModelRenderer(this, 0, 106);
		this.pauldronL2.mirror = true;
		this.pauldronL2.setRotationPoint(-1.0F, 2.5F, 0.0F);
		this.pauldronL2.addBox(-5.75F, -5.5F, -3.0F, 6, 6, 6, scale);
		this.setRotateAngle(this.pauldronL2, 0.0F, 0.0F, 0.17453292519943295F);

		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);

		this.chestExtension = new ModelRenderer(this, 24, 96);
		this.chestExtension.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.chestExtension.addBox(-4.5F, -0.5F, -2.5F, 9, 13, 5, scale);

		this.skirtR = new ModelRenderer(this, 64, 64);
		this.skirtR.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.skirtR.addBox(-4.0F, -3.0F, -3.0F, 6, 6, 6, scale);
		this.setRotateAngle(this.skirtR, 0.0F, 0.0F, 0.2617993877991494F);

		this.bipedLeftArm = new ModelRenderer(this, 32, 48);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);

		this.pauldronR1 = new ModelRenderer(this, 0, 96);
		this.pauldronR1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.pauldronR1.addBox(-3.5F, -2.5F, -2.5F, 5, 5, 5, scale);
		this.setRotateAngle(this.pauldronR1, 0.0F, 0.0F, -0.08726646259971647F);

		this.bipedBody = new ModelRenderer(this, 16, 16);
		this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);

		this.pauldronR1.addChild(this.pauldronR2);
		this.bipedHead.addChild(this.lowerHeadArmor);
		this.bipedLeftLeg.addChild(this.skirtL);
		this.bipedLeftArm.addChild(this.pauldronL1);
		this.pauldronL1.addChild(this.pauldronL2);
		this.bipedBody.addChild(this.chestExtension);
		this.bipedRightLeg.addChild(this.skirtR);
		this.bipedRightArm.addChild(this.pauldronR1);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}