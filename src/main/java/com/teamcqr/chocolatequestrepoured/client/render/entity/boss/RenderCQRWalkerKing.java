package com.teamcqr.chocolatequestrepoured.client.render.entity.boss;

import com.teamcqr.chocolatequestrepoured.client.render.entity.RenderCQREntity;
import com.teamcqr.chocolatequestrepoured.client.render.entity.layers.LayerBossDeath;
import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRWalkerBoss;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderCQRWalkerKing extends RenderCQREntity<EntityCQRWalkerBoss> {

	public RenderCQRWalkerKing(RenderManager rendermanagerIn, ModelBase model, String entityName) {
		super(rendermanagerIn, model, 0.5F, entityName, 1D, 1D);
		
		this.addLayer(new LayerBossDeath(191,0,255));
	}

}
