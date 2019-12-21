package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.objects.items.swords.ItemDagger;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAIBackstab extends EntityAIAttack {

	public EntityAIBackstab(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.getHeldItemMainhand().getItem() instanceof ItemDagger && super.shouldExecute();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.entity.getHeldItemMainhand().getItem() instanceof ItemDagger && super.shouldContinueExecuting();
	}

	@Override
	protected void updatePath(EntityLivingBase target) {
		double distance = Math.min(4.0D, this.entity.getDistance(target.posX, target.posY, target.posZ) * 0.5D);
		double rad = Math.toRadians(target.rotationYaw);
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		PathNavigate navigator = this.entity.getNavigator();
		Path path = null;
		for (int i = 4; path == null && i >= 0; i--) {
			double d = distance * (double) i / 4.0D;
			path = navigator.getPathToXYZ(target.posX + sin * d, target.posY, target.posZ - cos * d);
		}
		navigator.setPath(path, 1.0D);
	}

}
