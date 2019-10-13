package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import java.util.ArrayList;
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
		if(shouldContinueExecuting()) {
			cooldown++;
			if(cooldown >= cooldownIdleBorder) {
				if(!entity.isSitting()) {
					entity.setSitting(true);
				}
				//DONE: Make entity sit -> Renderer needs work for that
				//int friendsFound = 0;
				List<Entity> friends = new ArrayList<>();
				List<Entity> nearbyEntities = entity.getEntityWorld().getEntitiesInAABBexcluding(entity, new AxisAlignedBB(entity.getPosition().subtract(new Vec3i(6,3,6)), entity.getPosition().add(6,3,6)), AbstractEntityCQR.MOB_SELECTOR);
				//System.out.println("NearbyEntities: " + nearbyEntities.size());
				if(!nearbyEntities.isEmpty()) {
					for(Entity ent : nearbyEntities) {
						if(ent instanceof AbstractEntityCQR) {
							if(entity.getFaction().isEntityAlly((AbstractEntityCQR)ent)) {
								friends.add(ent);
								//friendsFound++;
							}
						}
					}
					//System.out.println("Friend list size: " + friends.size());
					if(!friends.isEmpty()) {
						cooldwonForPartnerCycle++;
						if(talkingPartner == null || cooldwonForPartnerCycle >= cooldownCyclePartnerBorder || talkingPartner.isDead || entity.getDistance(talkingPartner) > 8) {
							talkingPartner = friends.get(random.nextInt(friends.size()));
							//System.out.println("I found a partner!");
							cooldwonForPartnerCycle = 0;
						}
						if(talkingPartner != null && talkingPartner != entity && !talkingPartner.isDead) {
							//we have someone to talk to, yay :D
							//DONE: Orient entity to random friend
							//DONE: Make them "chat" / "play cards"
							entity.setChatting(true);
							entity.getLookHelper().setLookPosition(talkingPartner.posX, talkingPartner.posY + talkingPartner.getEyeHeight(), talkingPartner.posZ, (float)this.entity.getHorizontalFaceSpeed(), (float)this.entity.getVerticalFaceSpeed());
							/*if(talkingPartner instanceof AbstractEntityCQR) {
								//((AbstractEntityCQR)talkingPartner).setSitting(true);
								//((AbstractEntityCQR)talkingPartner).setChatting(true);
							}*/
						}
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
		/*if(talkingPartner != null && !talkingPartner.isDead && talkingPartner instanceof AbstractEntityCQR) {
			//((AbstractEntityCQR)talkingPartner).setSitting(false);
			((AbstractEntityCQR)talkingPartner).setChatting(false);
		}*/
		talkingPartner = null;
		if(entity.isSitting()) {
			entity.setSitting(false);
			entity.setChatting(false);
		}
	}

}
