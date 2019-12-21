package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public enum ESpellType {
	
	NONE(0, null, 0, 0, 0),
	SUMMON_FANGS(1, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.4D, 0.3D, 0.35D),
	SUMMON_MINIONS(2, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.7D, 0.7D, 0.8D),
	POISON_PLAYER(3, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.16D, 0.48D, 0.12),
	ACTIVATE_MAGIC_ARMOR(4, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.55D, 0.0D, 0.8D);
	
	private int id;
	private SoundEvent spellSound;
	
	private double dx;
	private double dy;
	private double dz;
	
	private ESpellType(int id, SoundEvent sound, double sx, double sy, double sz) {
		this.id = id;
		this.spellSound = sound;
		
		this.dx = sx;
		this.dy = sy;
		this.dz = sz;
	}
	
	public int getID() {
		return id;
	}
	
	public double getDX() {
		return this.dx;
	}
	public double getDY() {
		return this.dy;
	}
	public double getDZ() {
		return this.dz;
	}
	
	public SoundEvent getSpellSound() {
		return this.spellSound;
	}

}
