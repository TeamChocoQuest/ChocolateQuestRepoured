package team.cqr.cqrepoured.client.render.entity.mobs;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.EquipmentSlotType;
import team.cqr.cqrepoured.client.model.entity.mobs.ModelCQRGoblin;
import team.cqr.cqrepoured.client.render.entity.RenderCQREntity;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGoblin;

public class RenderCQRGoblin extends RenderCQREntity<EntityCQRGoblin> {

	public RenderCQRGoblin(EntityRendererManager rendermanagerIn) {
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
	public void setupHeadOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.8F, 0.8F, 0.8F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupHeadwearOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.8F, 0.8F, 0.8F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupBodyOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		if (slot == EquipmentSlotType.CHEST) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.8F, 0.6F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		} else if (slot == EquipmentSlotType.LEGS) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.8F, 0.7F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		}
	}

	@Override
	public void setupLeftArmOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.85F, 1.0F, 0.85F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupRightArmOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		this.applyTranslations(modelRenderer);
		this.applyRotations(modelRenderer);
		GlStateManager.scale(0.85F, 1.0F, 0.85F);
		this.resetRotations(modelRenderer);
		this.resetTranslations(modelRenderer);
	}

	@Override
	public void setupLeftLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		if (slot == EquipmentSlotType.LEGS) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		} else if (slot == EquipmentSlotType.FEET) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		}
	}

	@Override
	public void setupRightLegOffsets(ModelRenderer modelRenderer, EquipmentSlotType slot) {
		if (slot == EquipmentSlotType.LEGS) {
			this.applyTranslations(modelRenderer);
			this.applyRotations(modelRenderer);
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			this.resetRotations(modelRenderer);
			this.resetTranslations(modelRenderer);
		} else if (slot == EquipmentSlotType.FEET) {
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
