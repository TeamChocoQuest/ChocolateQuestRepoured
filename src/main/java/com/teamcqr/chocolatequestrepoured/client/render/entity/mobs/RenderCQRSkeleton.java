package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.models.entities.mobs.ModelCQRSkeleton;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRSkeleton;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRSkeleton extends RenderCQREntity<EntityCQRSkeleton> {

	public RenderCQRSkeleton(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCQRSkeleton(), 0.5F, "mob/skeleton", 1.0D, 1.0D);
	}

}
