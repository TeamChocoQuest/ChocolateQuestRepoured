package com.teamcqr.chocolatequestrepoured.client.render.entity;

import org.lwjgl.opengl.GL11;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRTriton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRTriton extends RenderCQREntity<EntityCQRTriton> {

	public RenderCQRTriton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRTriton(0F), 0.5F, "entity_mob_cqrtriton", 1.0D, 1.0D);
	}
	
	@Override
	public void doRender(EntityCQRTriton entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180, 0F, 0F, 1F);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

}
