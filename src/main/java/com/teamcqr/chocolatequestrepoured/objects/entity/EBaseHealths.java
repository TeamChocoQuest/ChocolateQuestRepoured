package com.teamcqr.chocolatequestrepoured.objects.entity;

public enum EBaseHealths {
	
	NPC(20.0F),
	WALKER(40.0F),
	PIGMEN(50.0F),
	MACHINES(35.0F),
	UNDEAD(25.0F),
	MINOTAURS(30.0F),
	DWARVES(30.0F),
	ORCS(30.0F),
	GREMLINS(15.0F),
	OGRES(35.0F),
	BEES(15.0F),
	MOUNTS(25.0F),
	SPECTRES(30.0F),
	PIRATES(25.0F),
	MANDRILS(30.0F),
	
	NETHER_DRAGON(250.0F),
	;
	
	private float baseHealth;
	
	private EBaseHealths(float baseHealth) {
		this.baseHealth = baseHealth;
	}
	
	public float getValue() {
		return this.baseHealth;
	}

}
