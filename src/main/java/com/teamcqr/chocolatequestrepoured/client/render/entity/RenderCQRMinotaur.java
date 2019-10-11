package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMinotaur;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRMinotaur extends RenderCQREntity<EntityCQRMinotaur> {

	public RenderCQRMinotaur(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMinotaur(0F), 0.5F, "entity_mob_cqrminotaur", 1.0D, 1.0D);
	}

}
