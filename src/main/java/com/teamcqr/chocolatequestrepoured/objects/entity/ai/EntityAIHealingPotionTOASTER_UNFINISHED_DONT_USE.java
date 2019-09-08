package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class EntityAIHealingPotionTOASTER_UNFINISHED_DONT_USE extends EntityAIBase {

	protected AbstractEntityCQR entity;
	
	protected static final float healingAmountPerPotion = 20.0F;
	protected int cooldown = 0;
	protected static final int borderToWhenUsePotion = 100;
	protected static final int cooldownIncreaseNormal = 1;
	protected static final int cooldownIncreaseWhenPathBlocked = 10;
	protected static final int borderWhenToBurp = borderToWhenUsePotion +50;
	
	public EntityAIHealingPotionTOASTER_UNFINISHED_DONT_USE(AbstractEntityCQR cqrEntity) {
		this.entity = cqrEntity;
	}

	@Override
	public boolean shouldExecute() {
		if(!this.entity.isDead) {
			//If the entity has <= 10% of its max health or it has less than 5 HP left and it has healing potions, it can heal itself
			if(this.entity.getHealth() <= Math.max(this.entity.getMaxHealth() * 0.1D, 5.0D)) {
				if(this.entity.getHealingPotions() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	//If entity is dead or has no more potions -> dont continue to execute
	@Override
	public boolean shouldContinueExecuting() {
		return !(entity.isDead || entity.getHealingPotions() <= 0);
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		
		//First step: Walk away about 10-15 blocks from the attacker if that is possible
		//Once the entity is far away enough or it hits an obstacle -> start drinking your potion
		//While it is drinking the potion, it should play the "eating" animation of vanilla whilst holding the potion
		//Also it should play the drinking sound
		//Once it is done drinking, it plays the "burp" sound of the player and starts attacking again
		//While it is running away, it faces the last attacker and only blocks with its shield (if it has one)
		//If the entity is in a party, the party should attack the last attacker of this mob
		
		cooldown = 0;
		this.entity.getNavigator().clearPath();
		
		//TODO: Set entity to not eat
		//TODO: Set entity to defend itself
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		//When the entity has an target or the cooldown is active -> run away
		if(entity.getAttackTarget() != null || cooldown < borderToWhenUsePotion) {
			//It has a target -> Stare at it
			entity.getLookHelper().setLookPositionWithEntity(entity.getAttackTarget(), 30.0F, 30.0F);
			//Then set body rotation to head rotation (yaw)
			entity.rotationYaw = entity.rotationYawHead;
			
			double angleOfEntityBackwards = Math.toRadians((double)entity.rotationYaw - 180.0D);
			
			//Location 3 blocks behind the entity
			BlockPos locBehind = new BlockPos(Math.floor(entity.posX - Math.sin(
					angleOfEntityBackwards) * 3.0D), 
					Math.floor(entity.posY) - 1,
					Math.floor(entity.posZ + Math.cos(angleOfEntityBackwards) * 3.0D));
			
			//Now check the material there to check if it is a possible floor
			Material material = entity.getEntityWorld().getBlockState(locBehind).getMaterial();
			boolean mayMove = false;
			if(material.isSolid() || (material.isSolid() && material != Material.AIR && material != Material.FIRE && material != Material.LAVA)) {
				mayMove = true;
			}
			//Set movement speed?
			if(mayMove) {
				entity.moveForward = -0.25F;
			} else {
				entity.moveForward = 0.0F;
			}
			
			//If the target is far away enough or out of sight -> you can heal yourself
			if(entity.getDistance(entity.getAttackTarget()) > 100 || entity.getEntitySenses().canSee(entity.getAttackTarget())) {
				cooldown = borderToWhenUsePotion;
			}
			
			//If we collided with something, we increment the value higher
			if(entity.collided) {
				cooldown += cooldownIncreaseWhenPathBlocked;
			}
		}
		//We dont have a target or we're ready cooldown wise
		cooldown++;
		
		if(cooldown > borderToWhenUsePotion) {
			//We are ready to heal ourselves :D
			
			//If we're on the floor we need to stop moving on x & z axis
			if(entity.onGround) {
				entity.motionX = 0.0D;
				entity.motionZ = 0.0D;
			}
			
			//If we're not eating -> start eating; Then if we're defending, dont do so as you're busy with drinking (beer)
			//NYI, need to wait for updates on CQREntity
			
			//Now swing your right hand to show that you're drinking
			entity.swingArm(EnumHand.MAIN_HAND);
			
			//Play sounds at actually heal as you're done
			if(cooldown > borderWhenToBurp) {
				//Play burp sound
				
				//Spawn few heart particles
				
				//heal entity
				entity.heal(healingAmountPerPotion);
				//decrement potion count
				entity.removeHealingPotion();
				//stop eating
				
			} else {
				//Play drink sound
				
			}
		}
	}
	
	

}
