package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRGolem;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGolem;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRGolem extends RenderCQREntity<EntityCQRGolem> {

	public RenderCQRGolem(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRGolem(0F), 0.5F, "entity_mob_cqrgolemsmall", 1.0D, 1.0D);
	}

}
