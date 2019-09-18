package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.entity.ai.EntityAIBase;

public abstract class AbstractCQREntityAI extends EntityAIBase {

	protected final AbstractEntityCQR entity;

	public AbstractCQREntityAI(AbstractEntityCQR entity) {
		this.entity = entity;
	}

}
