package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemDagger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;

public class EntityAIBackstab extends EntityAIAttack {

	public EntityAIBackstab(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDagger && super.shouldExecute();
	}

	@Override
	protected boolean canMoveToEntity(EntityLivingBase target) {
		double distance = Math.min(2.0D, this.entity.getDistance(target.posX, target.posY, target.posZ));
		double rad = Math.toRadians(target.rotationYaw);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		this.path = this.entity.getNavigator().getPathToXYZ(target.posX + sin * distance, target.posY, target.posZ - cos * distance);
		return this.path != null;
	}

}
