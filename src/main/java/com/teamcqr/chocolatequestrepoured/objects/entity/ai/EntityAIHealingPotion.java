package com.teamcqr.chocolatequestrepoured.objects.entity.ai;

import com.teamcqr.chocolatequestrepoured.objects.entity.mobs.AbstractEntityCQR;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIHealingPotion extends EntityAIBase {

	protected AbstractEntityCQR entity;
	
	protected static final float healingAmountPerPotion = 20.0F;
	
	public EntityAIHealingPotion(AbstractEntityCQR cqrEntity) {
		this.entity = cqrEntity;
	}

	@Override
	public boolean shouldExecute() {
		if(!this.entity.isDead) {
			//If the entity has <= 10% of its max health or it has less than 5 HP left and it has healing potions, it can heal itself
			if(this.entity.getHealth() <= Math.max(this.entity.getMaxHealth() * 0.1D, 5.0D)) {
				if(this.entity.getRemainingPotions() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return super.shouldContinueExecuting();
		
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
		
	}
	
	

}
