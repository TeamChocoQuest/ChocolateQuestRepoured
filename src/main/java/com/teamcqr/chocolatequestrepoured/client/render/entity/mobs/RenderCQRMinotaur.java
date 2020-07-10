package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.models.entities.mobs.ModelCQRMinotaur;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRMinotaur;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRMinotaur extends RenderCQREntity<EntityCQRMinotaur> {

	public RenderCQRMinotaur(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRMinotaur(), 0.5F, "mob/minotaur", 1.0D, 1.0D);
	}

}
