package com.teamcqr.chocolatequestrepoured.factions;

import com.teamcqr.chocolatequestrepoured.factions.EReputationState.EReputationStateRough;
import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;

public enum EFaction {
	
	UNDEAD(new String[] {"WALKERS", "VILLAGERS"}, new String[] {"ILLAGERS", "ENDERMEN"}, EReputationState.ENEMY),
	PIRATE(new String[] {"WALKERS", "VILLAGERS", "INQUISITION"}, new String[] {"ILLAGERS"}, EReputationState.ENEMY),
	WALKERS(new String[] {"UNDEAD", "PIRATE", "DWARVES_AND_GOLEMS", "GOBLINS", "ENDERMEN", "OGRES_AND_GREMLINS", "INQUISITION", "ILLAGERS", "VILLAGERS", "PLAYER", "NPC"}, new String[] {}, EReputationState.ARCH_ENEMY),
	DWARVES_AND_GOLEMS(new String[] {"WALKERS", "ENDERMEN", "ILLAGERS"}, new String[] {"VILLAGERS", "NPC", "INQUISITION"}, EReputationState.ACCEPTED),
	GOBLINS(new String[] {"OGRES_AND_GREMLINS", "WALKERS", "VILLAGERS", "INQUISITION"}, new String[] {"ENDERMEN", "ILLAGERS"}, EReputationState.ENEMY),
	ENDERMEN(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	OGRES_AND_GREMLINS(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	INQUISITION(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	ILLAGERS(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	VILLAGERS(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	NEUTRAL(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	;
	
	
	private EFaction(String[] enemies, String[] allies, EReputationState startState) {
		//TODO: Create fields and fill them
	}
	
	public boolean isEnemy(EFaction otherFac) {
		//TODO work this out
		return false;
	}
	public boolean isAlly(EFaction otherFac) {
		//TODO work this out
		return false;
	}
	
	//DONE: Methods to check wether a faction is an ally or an enemy
	public EReputationStateRough getRelation(ICQREntity e1, ICQREntity e2) {
		
		EFaction e1Fac = e1.getFaction();
		EFaction e2Fac = e2.getFaction();
		
		if(e1Fac.isAlly(e2Fac) || e2Fac.isAlly(e1Fac)) {
			return EReputationStateRough.ALLY;
		}
		
		if(e1Fac.isEnemy(e2Fac) || e2Fac.isEnemy(e1Fac)) {
			return EReputationStateRough.ENEMY;
		}
		
		return EReputationStateRough.NEUTRAL;
	}

}
