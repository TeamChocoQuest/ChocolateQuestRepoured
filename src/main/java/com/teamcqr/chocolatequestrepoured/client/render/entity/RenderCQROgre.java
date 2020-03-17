package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQROgre;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROgre;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQROgre extends RenderCQREntity<EntityCQROgre> {

	public RenderCQROgre(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQROgre(0F), 0.5F, "entity_mob_cqrogre", 1.0D, 1.0D);
	}

}
