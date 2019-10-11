package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.List;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public class EntityAIIdleSit extends AbstractCQREntityAI {
	
	private int cooldown = 0;
	private Entity talkingPartner = null;
	private int cooldwonForPartnerCycle = 0;
	protected static final int cooldownIdleBorder = 50;
	protected static final int cooldownCyclePartnerBorder = 100;
	
	public EntityAIIdleSit(AbstractEntityCQR entity) {
		super(entity);
	}

	@Override
	public boolean shouldExecute() {
		if(!this.entity.isDead && this.entity.onGround && !this.entity.isBurning() && !this.entity.isRiding() && notMoving(this.entity)) {
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
			if(entity.getDistance(attacker) >= 35 && !(attacker instanceof EntityPlayer)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean notMoving(AbstractEntityCQR entity) {
		return !(Math.abs(entity.moveForward) > 0.05F || Math.abs(entity.moveStrafing) > 0.05F);
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
				if(!entity.isSitting()) {
					entity.setSitting(true);
				}
				//DONE: Make entity sit -> Renderer needs work for that
				int friendsFound = 0;
				List<Entity> friends = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(entity.getPosition().subtract(new Vec3i(3,1,3)), entity.getPosition().add(3,1,3)), AbstractEntityCQR.MOB_SELECTOR);
				for(Entity ent : friends) {
					if(entity.getFaction().isEntityAlly((AbstractEntityCQR)ent)) {
						friendsFound++;
					}
				}
				cooldwonForPartnerCycle++;
				if((talkingPartner == null || cooldwonForPartnerCycle >= cooldownCyclePartnerBorder) && !friends.isEmpty()) {
					talkingPartner = friends.get(random.nextInt(friends.size()));
					cooldwonForPartnerCycle = 0;
				}
				if( friendsFound > 0 && talkingPartner != null) {
					//we have someone to talk to, yay :D
					//DONE: Orient entity to random friend
					//DONE: Make them "chat" / "play cards"
					entity.setChatting(true);
					entity.getLookHelper().setLookPositionWithEntity(talkingPartner, 0, 0);
					if(talkingPartner instanceof AbstractEntityCQR) {
						((AbstractEntityCQR)talkingPartner).setSitting(true);
						((AbstractEntityCQR)talkingPartner).setChatting(true);
					}
				}
			}
		} else {
			resetTask();
		}
	}
	
	@Override
	public void resetTask() {
		super.resetTask();
		cooldown = 0;
		cooldwonForPartnerCycle = 0;
		talkingPartner = null;
		if(entity.isSitting()) {
			entity.setSitting(false);
			entity.setChatting(false);
		}
	}

}
