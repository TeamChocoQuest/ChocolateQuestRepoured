package com.teamcqr.chocolatequestrepoured.client.render.entity.layers;

import com.teamcqr.chocolatequestrepoured.objects.items.guns.ItemRevolver;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHandSide;

public class LayerRevolver implements LayerRenderer<EntityLivingBase> {

	private RenderLiving<? extends EntityLivingBase> livingEntityRenderer;

	public LayerRevolver(RenderLiving<? extends EntityLivingBase> livingEntityRendererIn) {
		super();
		this.livingEntityRenderer = livingEntityRendererIn;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!(this.livingEntityRenderer.getMainModel() instanceof ModelBiped)) {
			return;
		}
		Item itemMain = entitylivingbaseIn.getHeldItemMainhand().getItem();
		Item itemOff = entitylivingbaseIn.getHeldItemOffhand().getItem();
		if (itemMain instanceof ItemRevolver) {
			GlStateManager.pushMatrix();
			if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.LEFT) {
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedLeftArm.postRender(1F);
			} else {
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedRightArm.postRender(1F);
			}
			GlStateManager.popMatrix();
		}
		if (itemOff instanceof ItemRevolver) {
			GlStateManager.pushMatrix();
			if (!(entitylivingbaseIn.getPrimaryHand() == EnumHandSide.LEFT)) {
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedLeftArm.rotateAngleX -= new Float(Math.toRadians(90));
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedLeftArm.postRender(1F);
			} else {
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedRightArm.rotateAngleX -= new Float(Math.toRadians(90));
				((ModelBiped) this.livingEntityRenderer.getMainModel()).bipedRightArm.postRender(1F);
			}
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
