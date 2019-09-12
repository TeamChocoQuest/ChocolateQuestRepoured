package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQREnderman;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQREnderman extends RenderCQREntity<EntityCQREnderman> {

	public RenderCQREnderman(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBiped(), 0.5F, "entity_mob_cqrenderman", 1.0D, 1.0D);
	}

}
