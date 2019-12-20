package com.teamcqr.chocolatequestrepoured.objects.entity.ai.spells;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public enum ESpellType {
	
	NONE(0, null),
	SUMMON_FANGS(1, SoundEvents.ENTITY_ILLAGER_CAST_SPELL),
	SUMMON_MINIONS(2, SoundEvents.ENTITY_ILLAGER_CAST_SPELL),
	POISON_PLAYER(3, SoundEvents.ENTITY_ILLAGER_CAST_SPELL);
	
	private int id;
	private SoundEvent spellSound;
	
	private ESpellType(int id, SoundEvent sound) {
		this.id = id;
		this.spellSound = sound;
	}
	
	public int getID() {
		return id;
	}
	
	public SoundEvent getSpellSound() {
		return this.spellSound;
	}

}
