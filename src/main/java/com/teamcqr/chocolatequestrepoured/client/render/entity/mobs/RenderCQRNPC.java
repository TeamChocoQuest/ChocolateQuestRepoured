package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRNPC;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRNPC extends RenderCQREntity<EntityCQRNPC> {

	public RenderCQRNPC(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/lunk", true);
	}

}
