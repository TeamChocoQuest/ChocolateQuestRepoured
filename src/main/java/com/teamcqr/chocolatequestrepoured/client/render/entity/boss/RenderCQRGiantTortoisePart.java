package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.subparts.EntityCQRGiantTortoisePart;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCQRGiantTortoisePart extends Render<EntityCQRGiantTortoisePart> {

	public RenderCQRGiantTortoisePart(RenderManager manager) {
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCQRGiantTortoisePart entity) {
		return null;
	}
	
	@Override
	public void doRender(EntityCQRGiantTortoisePart entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
	}

}
