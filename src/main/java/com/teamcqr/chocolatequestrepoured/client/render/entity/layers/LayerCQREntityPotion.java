package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.init.ModItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCQREntityPotion extends AbstractLayerCQR {

	public LayerCQREntityPotion(RenderCQREntity<?> renderCQREntity) {
		super(renderCQREntity);
	}

	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (entity.getHealingPotions() > 0 && this.entityRenderer.getMainModel() instanceof ModelBiped) {
			ModelBiped model = (ModelBiped) this.entityRenderer.getMainModel();
			ModelRenderer body = model.bipedBody;

			if (body.cubeList.size() > 0) {
				ModelBox box = body.cubeList.get(0);

				if (box != null) {
					ItemStack stack = new ItemStack(ModItems.POTION_HEALING);
					float x = 0.0625F * (body.rotationPointX + box.posX1);
					float y = 0.0625F * (body.rotationPointY + box.posY2 - box.posY1);
					float z = 0.0625F * (body.rotationPointZ + box.posZ1);
					GlStateManager.pushMatrix();
					GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleX), 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleY), 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate((float) Math.toDegrees(body.rotateAngleZ), 0.0F, 0.0F, 1.0F);
					GlStateManager.translate(x, y, z);

					if (entity.isSneaking()) {
						GlStateManager.translate(0.0F, 0.2F, 0.0F);
					}

					float f = 0.0F;
					if (!entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) {
						f = 1.0F;
					} else if (!entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()) {
						f = 0.5F;
					}
					GlStateManager.translate(-0.0625F * (f + 0.2F), 0.0F, 0.5F * 0.0625F * (box.posZ2 - box.posZ1));
					float f1 = 0.4F;
					GlStateManager.scale(f1, f1, f1);
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
