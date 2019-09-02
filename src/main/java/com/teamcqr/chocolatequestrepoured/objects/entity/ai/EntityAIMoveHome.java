package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIMoveHome extends EntityAIBase {
	
	private final EntityCreature creature;
	
	public EntityAIMoveHome(EntityCreature creature) {
		this.creature = creature;
	}
	
	@Override
	public boolean shouldExecute() {
		if (creature.getAttackTarget() == null && creature instanceof ICQREntity) {
			BlockPos pos = ((ICQREntity) creature).getHome();
			if (pos != null && creature.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 20.0D) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting() {
		BlockPos pos = ((ICQREntity) creature).getHome();
		creature.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0D);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !creature.getNavigator().noPath();
	}
	
}
