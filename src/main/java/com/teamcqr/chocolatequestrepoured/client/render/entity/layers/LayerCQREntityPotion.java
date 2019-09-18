package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;

public class LayerCQREntityPotion extends AbstractLayerCQR {

	public LayerCQREntityPotion(RenderCQREntity renderCQREntity) {
		super(renderCQREntity);
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (this.entityRenderer.getMainModel() instanceof ModelBiped && entity.getHealingPotions() > 0) {
			ModelBiped model = (ModelBiped) this.entityRenderer.getMainModel();

			if (model.bipedBody.cubeList.size() > 0) {
				ModelBox box = model.bipedBody.cubeList.get(0);
				if (box != null) {
					ItemStack stack = new ItemStack(ModItems.POTION_HEALING);

					GlStateManager.pushMatrix();
					if (entity.isSneaking()) {
						GlStateManager.translate(0.0F, 0.2F, 0.0F);
					}

					float f = 0.25F;
					GlStateManager.scale(f, f, f);
					GlStateManager.translate(0.5F * 0.0625F * (box.posX1 - box.posX2) / f, 0.0625F * (box.posY2 - box.posY1) / f, 0.0F);
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
					Minecraft.getMinecraft().getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.NONE);
					GlStateManager.popMatrix();
				}
			}
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
