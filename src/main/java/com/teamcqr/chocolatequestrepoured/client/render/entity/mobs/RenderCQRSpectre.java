package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSpectre;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRSpectre extends RenderCQREntity<EntityCQRSpectre> {

	public RenderCQRSpectre(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/spectre", true);
	}

	@Override
	public void doRender(EntityCQRSpectre entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}
