package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public class EntityAIIdleSit extends AbstractCQREntityAI {
	
	private int cooldown = 0;
	
	protected static final int cooldownIdleBorder = 50;
	
	
	public EntityAIIdleSit(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(!this.entity.isDead && this.entity.onGround && !this.entity.isBurning() && !this.entity.isRiding()) {
			if(entity.getAttackTarget() != null && entity.getEntitySenses().canSee(entity.getAttackTarget()) && entity.getNavigator().getPathToEntityLiving(entity.getAttackTarget()) != null) {
				return false;
			}
			Entity attacker = entity.getAttackingEntity();
			if(attacker == null) {
				return true;
			}
			if(attacker instanceof EntityLiving) {
				EntityLiving attackerLiving = (EntityLiving)attacker;
				if(attackerLiving.getEntitySenses().canSee(entity) || attackerLiving.getNavigator().getPathToEntityLiving(entity) != null) {
					return false;
				}
			}
			if(entity.getDistance(attacker) >= 35 && attacker instanceof EntityPlayer) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}
	
	@Override
	public void updateTask() {
		if(shouldExecute()) {
			cooldown++;
			if(cooldown >= cooldownIdleBorder) {
				//TODO: Make entity sit
				int friendsFound = 0;
				for(Entity ent : entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(entity.getPosition().subtract(new Vec3i(3,1,3)), entity.getPosition().add(3,1,3)), AbstractEntityCQR.MOB_SELECTOR)) {
					if(entity.getFaction().isEntityAlly((AbstractEntityCQR)ent)) {
						friendsFound++;
					}
				}
				if( friendsFound > 0) {
					//we have someone to talk to, yay :D
				}
				
				//TODO: Orient entity to random friend
				//TODO: Make them "chat" / "play cards"
			}
		} else {
			resetTask();
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		cooldown = 0;
		//TODO: Make entity stand up and stop talking
	}

}
