package com.teamcqr.chocolatequestrepoured.factions;

import com.teamcqr.chocolatequestrepoured.factions.EReputationState.EReputationStateRough;
import com.teamcqr.chocolatequestrepoured.objects.entity.ICQREntity;

public enum EFactions {
	
	UNDEAD(new String[] {"WALKERS", "VILLAGERS"}, new String[] {"ILLAGERS", "ENDERMEN"}, EReputationState.ENEMY),
	PIRATE(new String[] {"WALKERS", "VILLAGERS", "INQUISITION"}, new String[] {"ILLAGERS"}, EReputationState.ENEMY),
	WALKERS(new String[] {"UNDEAD", "PIRATE", "DWARVES_AND_GOLEMS", "GOBLINS", "ENDERMEN", "OGRES_AND_GREMLINS", "INQUISITION", "ILLAGERS", "VILLAGERS", "PLAYER", "NPC"}, new String[] {}, EReputationState.ARCH_ENEMY),
	DWARVES_AND_GOLEMS(new String[] {"WALKERS", "ENDERMEN", "ILLAGERS"}, new String[] {"VILLAGERS", "NPC", "INQUISITION"}, EReputationState.ACCEPTED),
	GOBLINS(new String[] {"OGRES_AND_GREMLINS", "WALKERS", "VILLAGERS", "INQUISITION"}, new String[] {"ENDERMEN", "ILLAGERS"}, EReputationState.ENEMY),
	ENDERMEN(),
	OGRES_AND_GREMLINS(),
	INQUISITION(),
	ILLAGERS(),
	VILLAGERS();
	
	
	private EFactions(String[] enemies, String[] allies, EReputationState startState) {
		// TODO Auto-generated constructor stub
	}
	
	public EReputationStateRough getRelation(ICQREntity e1, ICQREntity e2) {
		
	}

}
