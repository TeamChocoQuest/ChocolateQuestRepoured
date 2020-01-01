package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public enum ESpellType {
	
	NONE(0, null, 0, 0, 0),
	SUMMON_FANGS(1, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.4D, 0.3D, 0.35D),
	SUMMON_MINIONS(2, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.7D, 0.7D, 0.8D),
	POISON_TARGET(3, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.16D, 0.48D, 0.12),
	ACTIVATE_MAGIC_ARMOR(4, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.55D, 0.0D, 0.8D),
	SUMMON_FALLING_FIREBALLS(5, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.8D, 0.0D, 0.0D),
	SUMMON_EXPLOSION_RAY(6, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.0D, 0.0D, 0.4D), 
	CAST_EXPLOSION(7, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.0,0.6,0.0), 
	SUMMON_FIRE_WALL(8, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 0.5,0,0),
	BLIND_TARGET(9, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1, 1, 1),
	STEAL_HEALTH(10, SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1,0,1),
	;
	
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
