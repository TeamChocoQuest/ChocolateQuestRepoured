package team.cqr.cqrepoured.client.model.armor;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;

/**
 * ModelCrown - DerToaster Created using Tabula 7.0.1
 */
public class ModelCrown<T extends LivingEntity> extends ModelCustomArmorBase<T> {

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
		this.crownBorderLeft.setPos(3.0F, 1.3F, 0.0F);
		this.crownBorderLeft.addBox(0.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
		this.crownJewelFrontBase = new ModelRenderer(this, 0, 0);
		this.crownJewelFrontBase.setPos(0.0F, -9.2F, -0.5F);
		this.crownJewelFrontBase.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 1, 0.0F);
		this.crownBorderRight = new ModelRenderer(this, 18, 24);
		this.crownBorderRight.setPos(-3.0F, 1.3F, 0.0F);
		this.crownBorderRight.addBox(-1.0F, -9.0F, -3.0F, 1, 2, 6, 0.0F);
		this.crownUpper = new ModelRenderer(this, 0, 0);
		this.crownUpper.setPos(0.0F, -2.7F, 0.0F);
		this.crownUpper.addBox(-4.0F, -7.0F, -4.0F, 8, 2, 8, 0.0F);
		this.crownJewelFront = new ModelRenderer(this, 0, 3);
		this.crownJewelFront.setPos(0.0F, 0.0F, -0.7F);
		this.crownJewelFront.addBox(-0.5F, 0.5F, -0.5F, 1, 1, 1, 0.0F);
		this.crownBorderBack = new ModelRenderer(this, 0, 24);
		this.crownBorderBack.setPos(0.0F, 1.3F, 2.0F);
		this.crownBorderBack.addBox(-4.0F, -9.0F, 1.0F, 8, 2, 1, 0.0F);
		this.head = new ModelRenderer(this, 0, 10);
		this.head.setPos(0.0F, 0.0F, 0.0F);
		this.head.addBox(-3.0F, -7.0F, -3.0F, 6, 1, 6, 0.0F);
		this.crownBorderFront = new ModelRenderer(this, 0, 29);
		this.crownBorderFront.setPos(0.0F, 1.3F, -3.0F);
		this.crownBorderFront.addBox(-4.0F, -9.0F, -1.0F, 8, 2, 1, 0.0F);

		this.head.addChild(this.crownBorderLeft);
		this.crownBorderFront.addChild(this.crownJewelFrontBase);
		this.head.addChild(this.crownBorderRight);
		this.head.addChild(this.crownUpper);
		this.crownJewelFrontBase.addChild(this.crownJewelFront);
		this.head.addChild(this.crownBorderBack);
		this.head.addChild(this.crownBorderFront);
		this.hat.visible = true;
		this.body.visible = true;
		this.leftArm.visible = true;
		this.rightArm.visible = true;
		this.leftLeg.visible = true;
		this.rightLeg.visible = true;
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
		GlStateManager.translate(-this.head.rotationPointX * 0.0625F * f1, -this.head.rotationPointY * 0.0625F * f1, -this.head.rotationPointZ * 0.0625F * f1);
		GlStateManager.scale(f, f, f);
		this.head.render(scale);

		GlStateManager.popMatrix();
	}

	@Override
	public void render(Entity entityIn, float scale, RenderCQREntity<?> renderer, ModelBiped model, EquipmentSlotType slot) {
		this.render(entityIn, scale, renderer, model);
	}

	public void render(Entity entityIn, float scale, EntityRenderer<?> renderer, ModelBiped model) {
		float f = 1.3F;
		float f1 = f - 1.0F;

		this.applyRotations(this.head, model.bipedHead);
		GlStateManager.pushMatrix();

		if (this.isChild) {
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
		} else if (entityIn.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}
		if (renderer instanceof RenderCQREntity) {
			((RenderCQREntity<?>) renderer).setupHeadOffsets(this.head, EquipmentSlotType.HEAD);
		}
		GlStateManager.translate(-this.head.rotationPointX * 0.0625F * f1, -this.head.rotationPointY * 0.0625F * f1, -this.head.rotationPointZ * 0.0625F * f1);
		GlStateManager.scale(f, f, f);
		this.head.render(scale);

		GlStateManager.popMatrix();
		this.resetRotations(this.head);
	}

}
