package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRZombie;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRZombie extends RenderCQREntity<EntityCQRZombie> {

	public RenderCQRZombie(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/zombie", true);
	}

}
