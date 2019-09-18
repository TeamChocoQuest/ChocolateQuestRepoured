package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRIllager;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRIllager extends RenderCQREntity<EntityCQRIllager> {

	public RenderCQRIllager(RenderManager rendermanagerIn) {
		//TODO: Own illager model that extends the biped model
		super(rendermanagerIn, new ModelBiped(), 0.5F, "entity_mob_cqrillager", 1.0D, 1.0D);
	}

}
