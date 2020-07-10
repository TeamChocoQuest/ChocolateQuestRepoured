package com.teamcqr.chocolatequestrepoured.client.render.entity.mobs;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.EntityCQRWalker;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRWalker extends RenderCQREntity<EntityCQRWalker> {

	public RenderCQRWalker(RenderManager rendermanagerIn) {
		super(rendermanagerIn, "mob/walker", true);
	}

}
