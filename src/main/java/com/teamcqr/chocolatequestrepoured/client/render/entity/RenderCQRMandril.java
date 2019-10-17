package com.teamcqr.chocolatequestrepoured.client.render.entity;

import com.teamcqr.chocolatequestrepoured.client.models.entities.ModelCQRMonkey;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMandril;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRMandril extends RenderCQREntity<EntityCQRMandril> {
	
	public RenderCQRMandril(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMonkey(0F), 0.5F, "entity_mob_cqrmandril", 1.0D, 1.0D);
	}

}
