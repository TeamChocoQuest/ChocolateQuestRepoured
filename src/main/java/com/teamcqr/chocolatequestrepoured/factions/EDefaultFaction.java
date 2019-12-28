package com.teamcqr.chocolatequestrepoured.factions;

public enum EDefaultFaction {

	UNDEAD(new String[] {"WALKERS", "VILLAGERS", "PLAYERS", "TRITONS"}, new String[] {"GOBLIN", "ENDERMEN"}, EReputationState.ENEMY),
	PIRATE(new String[] {"WALKERS", "VILLAGERS", "INQUISITION", "PLAYERS", "TRITONS"}, new String[] {"ILLAGERS"}, EReputationState.ENEMY),
	WALKERS(new String[] {"UNDEAD", "PIRATE", "DWARVES_AND_GOLEMS", "GOBLINS", "ENDERMEN", "PLAYERS", "OGRES_AND_GREMLINS", "INQUISITION", "ILLAGERS", "VILLAGERS", "NPC"}, new String[] {}, EReputationState.ARCH_ENEMY),
	DWARVES_AND_GOLEMS(new String[] {"WALKERS", "ENDERMEN", "ILLAGERS", "UNDEAD"}, new String[] {"VILLAGERS", "NPC", "INQUISITION"}, EReputationState.ACCEPTED),
	GOBLINS(new String[] {"OGRES_AND_GREMLINS", "WALKERS", "VILLAGERS", "INQUISITION", "PLAYERS"}, new String[] {"ENDERMEN", "ILLAGERS"}, EReputationState.ENEMY),
	ENDERMEN(new String[] {"WALKERS", "PLAYERS", "DWARVES_AND_GOLEMS", "VILLAGERS", "NPCS", "PIRATE", "TRITONS"}, new String[] {"ILLAGERS", "UNDEAD"}, EReputationState.NEUTRAL),
	//OGRES_AND_GREMLINS(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	INQUISITION(new String[] {"WALKERS", "ILLAGERS", "UNDEAD", "GOBLINS"}, new String[] {"DWARVES_AND_GOLEMS", "NPC", "VILLAGERS"}, EReputationState.NEUTRAL),
	BEASTS(new String[] {"WALKERS", "PLAYERS", "VILLAGERS", "NPC", "TRITONS", "UNDEAD"}, new String[] {"ENDERMEN", "PIRATE"}, EReputationState.NEUTRAL),
	VILLAGERS(new String[] {"WALKERS", "UNDEAD", "ILLAGERS"}, new String[] {"NPC", "TRITONS", "PLAYERS"}, EReputationState.NEUTRAL),
	NEUTRAL(new String[] {}, new String[] {}, EReputationState.NEUTRAL),
	TRITONS(new String[] {"WALKERS", "UNDEAD", "PIRATE", "ENDERMEN"}, new String[] {"NPC", "VILLAGERS"}, EReputationState.NEUTRAL),
	PLAYERS(new String[] {}, new String[] {"VILLAGERS", "NPC"}, EReputationState.NEUTRAL),
	;

	private String[] enemies;
	private String[] allies;
	private EReputationState defaultRepu;
	
	private EDefaultFaction(String[] enemies, String[] allies, EReputationState startState) {
		this.enemies = enemies;
		this.allies = allies;
		this.defaultRepu = startState;
	}
	
	public EReputationState getDefaultReputation() {
		return this.defaultRepu;
	}
	public String[] getAllies() {return allies;}
	public String[] getEnemies() {return enemies;}
}
