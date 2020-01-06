package com.teamcqr.chocolatequestrepoured.objects.entity;

import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.util.ResourceLocation;

public enum ELootTablesBoss {

	BOSS_DRAGON("dragon_normal"),
	BOSS_DRAGON_NETHER("dragon_nether"),
	BOSS_DRAGON_NETHER_SKELETAL("dragon_nether_skeletal"),
	BOSS_DRAGON_LAND("dragon_land"),
	BOSS_NECROMANCER("necromancer"),
	BOSS_LICH("lich"),
	BOSS_BOARMAGE("boar_mage"),
	BOSS_PHARAO("pharao"),
	BOSS_ENDERMAN_LORD("enderman_boss"),
	BOSS_WALKER_KING("walker_king"),
	BOSS_WALKER_QUEEN("walker_queen"),
	BOSS_PIRATE_CAPTAIN("pirate_captain"),
	BOSS_GOBLIN_SHAMAN("goblin_shaman"),
	BOSS_SPECTRE_LORD("spectre_lord"),
	BOSS_TURTLE("giant_turtle"),
	BOSS_SPIDER("shelob"),
	BOSS_SLIME("slime_frog"),
	BOSS_MONKING("monking"),
	BOSS_BULL("bull"),
	BOSS_BULL_ICE("bull_ice"),
	BOSS_SECRET("secret_boss"),
	BOSS_EXTERMINATOR("golem_boss"),
	BOSS_SPHINX("sphinx_boss"),
	BOSS_DWARF_ENGINEER("dwarf_engineer");

	private final ResourceLocation loottable;

	private ELootTablesBoss(String name) {
		this.loottable = new ResourceLocation(Reference.MODID, "entities/boss/" + name);
	}

	public ResourceLocation getLootTable() {
		return this.loottable;
	}

}
