package com.teamcqr.chocolatequestrepoured.client.models.armor;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerCQREntityArmor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;

/**
 * ModelCrown - DerToaster Created using Tabula 7.0.1
 */
public class ModelCrown extends ModelCustomArmorBase {

	public ModelRenderer crownBorderFront;
	public ModelRenderer crownBorderBack;
	public ModelRenderer crownBorderRight;
	public ModelRenderer crownBorderLeft;
	public ModelRenderer crownUpper;
	public ModelRenderer crownJewelFrontBase;
	public ModelRenderer crownJewelFront;

	public ModelCrown(float scale) {
		super(scale, 32, 32);

		this.crownBorderLeft = new ModelRenderer(this, 18, 16);
		this.crownBorderLeft.setRotationPoint(3.0F, 1.3F, 0.0F);
		this.crownBorderLeft.addBox(0.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
		this.crownJewelFrontBase = new ModelRenderer(this, 0, 0);
		this.crownJewelFrontBase.setRotationPoint(0.0F, -9.2F, -0.5F);
		this.crownJewelFrontBase.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 1, 0.0F);
		this.crownBorderRight = new ModelRenderer(this, 18, 24);
		this.crownBorderRight.setRotationPoint(-3.0F, 1.3F, 0.0F);
		this.crownBorderRight.addBox(-1.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
		this.crownUpper = new ModelRenderer(this, 0, 0);
		this.crownUpper.setRotationPoint(0.0F, -2.7F, 0.0F);
		this.crownUpper.addBox(-4.0F, -7.0F, -4.0F, 8, 2, 8, 0.0F);
		this.crownJewelFront = new ModelRenderer(this, 0, 3);
		this.crownJewelFront.setRotationPoint(0.0F, 0.0F, -0.7F);
		this.crownJewelFront.addBox(-0.5F, 0.5F, -0.5F, 1, 1, 1, 0.0F);
		this.crownBorderBack = new ModelRenderer(this, 0, 24);
		this.crownBorderBack.setRotationPoint(0.0F, 1.3F, 2.0F);
		this.crownBorderBack.addBox(-4.0F, -9.0F, 1.0F, 8, 2, 1, 0.0F);
		this.bipedHead = new ModelRenderer(this, 0, 10);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHead.addBox(-3.0F, -7.0F, -3.0F, 6, 1, 6, 0.0F);
		this.crownBorderFront = new ModelRenderer(this, 0, 29);
		this.crownBorderFront.setRotationPoint(0.0F, 1.3F, -3.0F);
		this.crownBorderFront.addBox(-4.0F, -9.0F, -1.0F, 8, 2, 1, 0.0F);

		this.bipedHead.addChild(this.crownBorderLeft);
		this.crownBorderFront.addChild(this.crownJewelFrontBase);
		this.bipedHead.addChild(this.crownBorderRight);
		this.bipedHead.addChild(this.crownUpper);
		this.crownJewelFrontBase.addChild(this.crownJewelFront);
		this.bipedHead.addChild(this.crownBorderBack);
		this.bipedHead.addChild(this.crownBorderFront);
		this.bipedHeadwear.isHidden = true;
		this.bipedBody.isHidden = true;
		this.bipedLeftArm.isHidden = true;
		this.bipedRightArm.isHidden = true;
		this.bipedLeftLeg.isHidden = true;
		this.bipedRightLeg.isHidden = true;
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		float f = 1.3F;
		float f1 = f - 1.0F;

		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
		} else if (entityIn.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		GlStateManager.translate(-this.bipedHead.rotationPointX * 0.0625F * f1, -this.bipedHead.rotationPointY * 0.0625F * f1, -this.bipedHead.rotationPointZ * 0.0625F * f1);
		GlStateManager.scale(f, f, f);
		this.bipedHead.render(scale);

		GlStateManager.popMatrix();
	}

	@Override
	public void render(Entity entityIn, float scale, RenderCQREntity<?> renderer, LayerCQREntityArmor layer, ModelBiped model, EntityEquipmentSlot slot) {
		this.render(entityIn, scale, renderer, model);
	}

	public void render(Entity entityIn, float scale, Render<?> renderer, ModelBiped model) {
		float f = 1.3F;
		float f1 = f - 1.0F;

		this.applyRotations(this.bipedHead, model.bipedHead);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
		} else if (entityIn.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		if (renderer instanceof RenderCQREntity) {
			((RenderCQREntity<?>) renderer).setupHeadOffsets(this.bipedHead, EntityEquipmentSlot.HEAD);
		}
		GlStateManager.translate(-this.bipedHead.rotationPointX * 0.0625F * f1, -this.bipedHead.rotationPointY * 0.0625F * f1, -this.bipedHead.rotationPointZ * 0.0625F * f1);
		GlStateManager.scale(f, f, f);
		this.bipedHead.render(scale);

		GlStateManager.popMatrix();
		this.resetRotations(this.bipedHead);
	}

}
