package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.models.entities.boss.ModelPirateCaptain;
import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRPirateCaptain;

import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRPirateCaptain extends RenderCQREntity<EntityCQRPirateCaptain> {

	public RenderCQRPirateCaptain(RenderManager rendermanagerIn, String textureName) {
		super(rendermanagerIn, new ModelPirateCaptain(0, true), 0.5F, textureName, 1D, 1D);
	}

}
