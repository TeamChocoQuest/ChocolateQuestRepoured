package com.teamcqr.chocolatequestrepoured.client.render.entity.mounts;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantSilverfishRed;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGiantSilverfishRed extends RenderLiving<EntityGiantSilverfishRed> {

	static ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/mounts/giant_silverfish_red.png");

	public RenderGiantSilverfishRed(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelSilverfish(), 1.5F);
	}

	@Override
	protected void preRenderCallback(EntityGiantSilverfishRed entitylivingbaseIn, float partialTickTime) {
		double width = 4D;
		double height = 4D;
		GL11.glScaled(width, height, width);

		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGiantSilverfishRed entity) {
		return TEXTURE;
	}
}
