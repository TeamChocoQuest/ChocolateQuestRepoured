package team.cqr.cqrepoured.objects.entity.ai.boss.endercalamity;

import net.minecraft.init.Blocks;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.EntityCQREnderCalamity.E_CALAMITY_HAND;
import team.cqr.cqrepoured.objects.entity.boss.endercalamity.phases.EEnderCalamityPhase;

public class BossAIEnderCalamityBuilder extends BossAIBlockThrower {

	private int buildingCycles = 3;
	private int teleportCooldown = 10;
	private int blockEquipTimer = 5;
	private int blockThrowTimer = 5;
	private boolean waitingForAnimationEnd = false;
	
	public BossAIEnderCalamityBuilder(EntityCQREnderCalamity entity) {
		super(entity);
	}

	@Override
	protected boolean canExecuteDuringPhase(EEnderCalamityPhase currentPhase) {
		return currentPhase == EEnderCalamityPhase.PHASE_BUILDING;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return this.buildingCycles >= 0;
	}
	
	@Override
	protected void execHandStateBlockWhenDone(E_CALAMITY_HAND hand) {
	}
	
	@Override
	protected void execHandStateNoBlockWhenDone(E_CALAMITY_HAND hand) {
	}
	
	@Override
	protected void execHandStateThrowingWhenDone(E_CALAMITY_HAND hand) {
		this.waitingForAnimationEnd = false;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		this.forceDropAllBlocks();
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		if(this.blockEquipTimer > 0 && this.getCountOfEquippedHands() < 6) {
			this.blockEquipTimer--;
			
			if(this.blockEquipTimer <= 0) {
				//Equip random hand
				
				for(EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
					if(this.getStateOfHand(hand) != E_HAND_STATE.NO_BLOCK) {
						continue;
					}
					
					this.entity.equipBlock(hand, Blocks.END_BRICKS);
					this.setStateOfHand(hand, E_HAND_STATE.BLOCK);
					
					this.spawnEquipParticlesForHand(hand);
					
					break;
				}
				this.blockEquipTimer = 5;
			}
		}
		
		if(this.getCountOfEquippedHands() >= 6) {
			this.blockThrowTimer--;
			if(this.blockThrowTimer <= 0) {
				//Throw all the blocks
				for (EntityCQREnderCalamity.E_CALAMITY_HAND hand : EntityCQREnderCalamity.E_CALAMITY_HAND.values()) {
					this.throwBlockOfHand(hand);
				}
				this.waitingForAnimationEnd = true;
				this.blockThrowTimer = 0;
			}
		}
		
		if(!this.waitingForAnimationEnd && this.blockThrowTimer <= 0 && this.getCountOfEquippedHands() <= 0) {
			this.teleportCooldown--;
			if(this.teleportCooldown <= 0) {
				this.buildingCycles--;
				this.entity.forceTeleport();
				this.blockThrowTimer = 5;
			}
		}
	}
	
	@Override
	public void resetTask() {
		this.buildingCycles = 3;
		this.teleportCooldown = 10;
		this.blockEquipTimer = 5;
		this.blockThrowTimer = 5;
		this.waitingForAnimationEnd = false;
		
		this.entity.forcePhaseChangeToNextOf(EEnderCalamityPhase.PHASE_BUILDING.getPhaseObject());
	}

}
