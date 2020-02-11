package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntityBubble;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBubble extends Render<EntityBubble> {

	public RenderBubble(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
		return;
	}
	
	@Override
	public void doRender(EntityBubble entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		//TODO: Make a model for this that represents a blocky sphere, it will get scaled up
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBubble entity) {
		return null;
	}
	
	

}
