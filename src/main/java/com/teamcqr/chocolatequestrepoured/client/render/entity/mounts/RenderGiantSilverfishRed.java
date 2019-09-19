package com.teamcqr.chocolatequestrepoured.client.render.entity.mounts;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityRedGiantSilverfish;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSilverfish;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGiantSilverfishRed extends RenderLiving<EntityRedGiantSilverfish> {

	static ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/entity/mounts/giant_silverfish_red.png");

	public RenderGiantSilverfishRed(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, new ModelSilverfish(), shadowsizeIn);
	}

	@Override
	protected void preRenderCallback(EntityRedGiantSilverfish entitylivingbaseIn, float partialTickTime) {
		double width = 4D;
		double height = 4D;
		GL11.glScaled(width, height, width);

		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRedGiantSilverfish entity) {
		return TEXTURE;
	}
}
