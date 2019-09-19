package com.teamcqr.chocolatequestrepoured.client.render.entity.mounts;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.objects.mounts.EntityGiantEndermite;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelEnderMite;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderGiantEndermite extends RenderLiving<EntityGiantEndermite> {
	
	private static final ResourceLocation ENDERMITE_TEXTURES = new ResourceLocation(Reference.MODID,"textures/entity/mounts/giant_endermite.png");

	public RenderGiantEndermite(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelEnderMite(), 1.5F);
	}
	
	@Override
	protected void preRenderCallback(EntityGiantEndermite entitylivingbaseIn, float partialTickTime) {
		double width = 2.5D;
		double height = 2.5D;
		GL11.glScaled(width, height, width);
		
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}
	
	protected float getDeathMaxRotation(EntityGiantEndermite entityLivingBaseIn)
    {
        return 180.0F;
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityGiantEndermite entity) {
		return ENDERMITE_TEXTURES;
	}

}
