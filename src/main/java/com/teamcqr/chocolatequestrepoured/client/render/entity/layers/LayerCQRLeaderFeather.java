package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class LayerCQRLeaderFeather extends AbstractLayerCQR {

	private ModelRenderer bipedHead;
	
	public LayerCQRLeaderFeather(RenderCQREntity<?> livingEntityRendererIn, ModelRenderer headBox) {
		super(livingEntityRendererIn);
		this.bipedHead = headBox;
	}
	
	@Override
	public void doRenderLayer(AbstractEntityCQR entity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(entity.isLeader() || !entity.hasLeader()) {
			GlStateManager.pushMatrix();
			
			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.scale(scale +0.4, scale +0.4, scale +0.4);
			float yaw = netHeadYaw +90F;
			GlStateManager.rotate(headPitch, 1, 0, 0);
			GlStateManager.rotate(yaw, 0, 1, 0);
			//GlStateManager.rotate(0, 0, -90, 0);
			float height = entity.getEyeHeight();
			GlStateManager.translate(bipedHead.offsetX, height, bipedHead.offsetZ);
			//GlStateManager.translate(0, headHeight /5, 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.FEATHER, 1), ItemCameraTransforms.TransformType.FIXED);
			
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
