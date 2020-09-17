package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.models.entities.mobs.ModelCQRGoblin;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGoblin;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;

public class RenderCQRGoblin extends RenderCQREntity<EntityCQRGoblin> {

	public RenderCQRGoblin(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGoblin(), 0.3F, "mob/goblin", 1.0D, 1.0D);
	}

	@Override
	protected void renderModel(EntityCQRGoblin entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		if (entitylivingbaseIn.isSitting()) {
			GlStateManager.translate(0, -0.25, 0);
		}
		super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
	}

	@Override
	public void setupHeadOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.8F, 0.8F, 0.8F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupHeadwearOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.8F, 0.8F, 0.8F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupBodyOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.CHEST) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.8F, 0.6F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		} else if (slot == EntityEquipmentSlot.LEGS) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.8F, 0.7F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		}
	}

	@Override
	public void setupLeftArmOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.85F, 1.0F, 0.85F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupRightArmOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.85F, 1.0F, 0.85F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupLeftLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.LEGS) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		} else if (slot == EntityEquipmentSlot.FEET) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		}
	}

	@Override
	public void setupRightLegOffsets(ModelRenderer modelRenderer, EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.LEGS) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		} else if (slot == EntityEquipmentSlot.FEET) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		}
	}

	@Override
	public void setupPotionOffsets(ModelRenderer modelRenderer) {
		GlStateManager.translate(0.04F, 0.225F, 0.0F);
	}

}
