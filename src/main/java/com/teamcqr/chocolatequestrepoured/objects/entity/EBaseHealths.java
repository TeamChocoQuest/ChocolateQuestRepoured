package com.teamcqr.chocolatequestrepoured.objects.entity;

public enum EBaseHealths {
	
	NPC(20.0D),
	WALKER(40.0D),
	PIGMEN(50.0D),
	MACHINES(35.0D),
	UNDEAD(25.0D),
	MINOTAURS(30.0D),
	DWARVES(30.0D),
	ORCS(30.0D),
	GREMLINS(15.0D),
	OGRES(35.0D),
	BEES(15.0D),
	MOUNTS(25.0D),
	SPECTRES(30.0D),
	MANDRILS(30.0D);
	
	private double baseHealth;
	
	private EBaseHealths(double baseHealth) {
		this.baseHealth = baseHealth;
	}
	
	public double getValue() {
		return baseHealth;
	}

}
