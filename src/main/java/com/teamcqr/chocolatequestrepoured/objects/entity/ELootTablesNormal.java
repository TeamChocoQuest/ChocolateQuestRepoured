package com.teamcqr.chocolatequestrepoured.objects.entity;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;

public enum ELootTablesNormal {

	// CQR mobs
	EMPTY("empty"), ENTITY_BOARMAN("boarman"), ENTITY_DUMMY("dummy"), ENTITY_DWARF("dwarf"), ENTITY_ENDERMAN("enderman"), ENTITY_GOBLIN("goblin"), ENTITY_GOLEM("golem_small"), ENTITY_MINOTAUR("minotaur"), ENTITY_MONKEY("monkey"),
	ENTITY_MUMMY("mummy"), ENTITY_NPC("npc"), ENTITY_OGRE("ogre"), ENTITY_ORC("orc"), ENTITY_PIRATE("pirate"), ENTITY_SKELETON("skeleton"), ENTITY_SPECTRE("spectre"), ENTITY_TRITON("triton"), ENTITY_WALKER("walker"),
	ENTITY_ZOMBIE("zombie"), ENTITY_ZOMBIE_TRITON("zombie_triton"),

	// "Ambient" mobs and mounts
	ENTITY_BEE("mounts/bee"), ENTITY_GIANT_ENDERMITE("mounts/endermite"), ENTITY_POLLO("mounts/pollo"), ENTITY_GIANT_SILVERFISH("mounts/silverfish"), ENTITY_GIANT_SILVERFISH_RED("mounts/silverfish_red"),
	ENTITY_GIANT_SILVERFISH_GREEN("mounts/silverfish_green"),;

	private final ResourceLocation loottable;

	private ELootTablesNormal(String name) {
		this.loottable = new ResourceLocation(Reference.MODID, "entities/" + name);
	}

	public ResourceLocation getLootTable() {
		return this.loottable;
	}
}
