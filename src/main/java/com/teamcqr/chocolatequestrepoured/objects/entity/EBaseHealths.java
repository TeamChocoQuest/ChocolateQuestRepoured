package com.teamcqr.chocolatequestrepoured.objects.entity;

public enum EBaseHealths {

	DUMMY(1.0F),
	DWARF(30.0F),
	ENDERMAN(40.0F),
	GOBLIN(20.0F),
	GOLEM(40.0F),
	GREMLIN(30.0F),
	ILLAGER(25.0F),
	MINOTAUR(30.0F),
	MUMMY(20.0F),
	NPC(20.0F),
	OGRE(35.0F),
	ORC(30.0F),
	PIGMAN(50.0F),
	PIRATE(25.0F),
	SKELETON(20.0F),
	SPECTRE(30.0F),
	TRITON(30.0F),
	WALKER(40.0F),
	ZOMBIE(25.0F),

	WASPS(15.0F),
	MANDRILS(30.0F),

	MOUNTS(25.0F),

	NETHER_DRAGON(250.0F),
	GIANT_TORTOISE(500.0F),
	LICH(200.0F),
	NECROMANCER(150.0F),
	BOAR_MAGE(250.0F), 
	;

	private float baseHealth;

	private EBaseHealths(float baseHealth) {
		this.baseHealth = baseHealth;
	}

	public float getValue() {
		return this.baseHealth;
	}

}
