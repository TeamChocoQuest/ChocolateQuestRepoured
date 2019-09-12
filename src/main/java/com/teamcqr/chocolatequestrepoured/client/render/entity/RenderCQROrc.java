package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQROrc;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQROrc extends RenderCQREntity<EntityCQROrc> {

	public RenderCQROrc(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelBiped(), 0.5F, "entity_mob_cqrorc", 1.0D, 1.0D);
	}

}
