package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRTriton;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRTriton;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRTriton extends RenderCQREntity<EntityCQRTriton> {

	public RenderCQRTriton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRTriton(0), 0.5F, "entity_mob_cqrtriton", 1.0D, 1.0D);
	}

}
