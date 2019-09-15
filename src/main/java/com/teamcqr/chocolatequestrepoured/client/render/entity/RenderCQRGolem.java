package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRGolem;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRGolem extends RenderCQREntity<EntityCQRGolem> {

	public RenderCQRGolem(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelIronGolem(), 0.5F, "entity_mob_cqrgolem", 1.0D, 1.0D);
	}

}
